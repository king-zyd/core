package com.zyd.core.http;

import com.zyd.core.util.StopWatch;
import com.zyd.core.util.TimeLength;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author neo, chi
 */
public class HTTPClient {
    public static final TimeLength DEFAULT_TIME_OUT = TimeLength.minutes(2);

    public static final TimeLength NO_TIME_OUT = TimeLength.ZERO;

    private static final int CONNECTION_POOL_MAX_SIZE = 200;

    private final Logger logger = LoggerFactory.getLogger(HTTPClient.class);

    private final ReentrantLock lock = new ReentrantLock();

    private transient HttpClient httpClient;

    private boolean connectionPoolEnabled = false;

    private boolean ignoreCookie = true;

    private boolean redirectEnabled = false;

    private boolean validateStatusCode = false;

    private boolean keepAlive = false;

    private TimeLength timeout = DEFAULT_TIME_OUT;

    public HTTPClient enableConnectionPool() {
        this.connectionPoolEnabled = true;
        return this;
    }

    public HTTPClient enableKeepAlive() {
        this.keepAlive = true;
        return this;
    }

    public HTTPClient enableValidateStatus() {
        this.validateStatusCode = true;
        return this;
    }

    public HTTPClient enableCookie() {
        this.ignoreCookie = false;
        return this;
    }

    public HTTPClient enableRedirect() {
        this.redirectEnabled = true;
        return this;
    }

    public HTTPClient setTimeout(TimeLength timeout) {
        this.timeout = timeout;
        return this;
    }

    public HTTPResponse execute(HTTPRequest request) {
        StopWatch watch = new StopWatch();
        try {
            logger.debug("send request, url={}, method={}", request.url(), request.method());
            logger.debug("====== http request begin ======");
            logRequest(request);

            HttpRequestBase httpRequest = request.getRawRequest();
            HttpResponse response = getHttpClient().execute(httpRequest);

            logger.debug("====== http request end ======");
            logger.debug("received response, statusCode={}", response.getStatusLine().getStatusCode());

            HTTPStatusCode statusCode = new HTTPStatusCode(response.getStatusLine().getStatusCode());

            HTTPContentType contentType = new HTTPContentType();
            contentType.setContentType(ContentType.get(response.getEntity()));

            byte[] content = decodeResponse(response.getEntity());
            httpRequest.releaseConnection();
            HTTPResponse httpResponse = new HTTPResponse(statusCode, createResponseHeaders(response), contentType, content);
            logResponse(httpResponse);

            if (validateStatusCode) {
                validateStatusCode(statusCode);
            }

            return httpResponse;
        } catch (IOException e) {
            throw new HTTPException(e);
        } finally {
            logger.debug("execute finished, elapsedTime={}(ms)", watch.elapsedTime());
        }
    }

    byte[] decodeResponse(HttpEntity entity) throws IOException, HTTPException {
        Header encodingHeader = entity.getContentEncoding();
        if (encodingHeader != null) {
            if (HTTPConstants.CONTENT_ENCODING_GZIP.equalsIgnoreCase(encodingHeader.getValue())) {
                return EntityUtils.toByteArray(new GzipDecompressingEntity(entity));
            } else if (HTTPConstants.CONTENT_ENCODING_DEFLATE.equalsIgnoreCase(encodingHeader.getValue())) {
                return EntityUtils.toByteArray(new DeflateDecompressingEntity(entity));
            }
            throw new HTTPException("Unsupported Content-Coding: " + encodingHeader.getValue());
        }

        return EntityUtils.toByteArray(entity);
    }

    HTTPHeaders createResponseHeaders(HttpResponse response) {
        HTTPHeaders headers = new HTTPHeaders();
        for (Header header : response.getAllHeaders()) {
            headers.add(header.getName(), header.getValue());
        }
        return headers;
    }

    void validateStatusCode(HTTPStatusCode statusCode) {
        if (statusCode.isSuccess() || statusCode.isRedirect()) {
            return;
        }
        throw new HTTPException("invalid response status code, statusCode=" + statusCode);
    }

    void logRequest(HTTPRequest httpRequest) {
        if (httpRequest.headers() != null)
            for (HTTPHeader header : httpRequest.headers()) {
                logger.debug("[header] {}={}", header.name(), header.value());
            }

        if (httpRequest.parameters() != null) {
            for (NameValuePair parameter : httpRequest.parameters()) {
                logger.debug("[param] {}={}", parameter.getName(), parameter.getValue());
            }
        }

        logger.debug("body={}", httpRequest.body());
    }

    void logResponse(HTTPResponse httpResponse) {
        if (httpResponse.headers() != null)
            for (HTTPHeader header : httpResponse.headers()) {
                logger.debug("[header] {}={}", header.name(), header.value());
            }

        logger.debug("responseText={}", httpResponse.responseText());
    }

    HttpClient getHttpClient() {
        if (httpClient == null) {
            try {
                lock.lock();
                if (httpClient == null)
                    httpClient = createDefaultHttpClient();
            } finally {
                lock.unlock();
            }
        }
        return httpClient;
    }

    HttpClient createDefaultHttpClient() {
        BasicHttpParams params = new SyncBasicHttpParams();

        // set default time out
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, (int) timeout.toMilliseconds());
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, (int) timeout.toMilliseconds());
        params.setParameter(CoreConnectionPNames.SO_KEEPALIVE, keepAlive);

        if (redirectEnabled) {
            params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
        }

        if (ignoreCookie) {
            params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        }

        ClientConnectionManager connectionManager = createClientConnectionManager();

        connectionManager.getSchemeRegistry().register(new Scheme(HTTPConstants.SCHEME_HTTP, HTTPConstants.STANDARD_PORT_HTTP, PlainSocketFactory.getSocketFactory()));
        registerHttpsScheme(connectionManager);

        return new DefaultHttpClient(connectionManager, params);
    }

    private ClientConnectionManager createClientConnectionManager() {
        if (connectionPoolEnabled) {
            PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
            connectionManager.setMaxTotal(CONNECTION_POOL_MAX_SIZE);
            connectionManager.setDefaultMaxPerRoute(CONNECTION_POOL_MAX_SIZE);
            return connectionManager;
        } else {
            return new BasicClientConnectionManager();
        }
    }

    void registerHttpsScheme(ClientConnectionManager connectionManager) {
        TrustManager[] trustManagers = new TrustManager[]{new SelfSignedX509TrustManager()};
        try {
            SSLContext context = SSLContext.getInstance(SSLSocketFactory.TLS);
            context.init(null, trustManagers, null);

            X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLSocketFactory socketFactory = new SSLSocketFactory(context, hostnameVerifier);

            Scheme scheme = new Scheme(HTTPConstants.SCHEME_HTTPS, HTTPConstants.STANDARD_PORT_HTTPS, socketFactory);
            connectionManager.getSchemeRegistry().register(scheme);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new HTTPException(e);
        }
    }
}
