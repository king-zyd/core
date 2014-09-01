package com.zyd.core.platform.web.rest.security;

import com.zyd.core.platform.exception.UserAuthorizationException;
import com.zyd.core.util.Convert;
import com.zyd.core.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author neo
 */
public class ClientService {
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private static final TimeLength REQUEST_VALIDITY_PERIOD = TimeLength.minutes(5);

    private ClientRepository clientRepository;

    public void authenticateClient(String clientId, RequestMessage message, String signature) {
        Date serverTimestamp = new Date();
        if (message.isExpired(serverTimestamp, REQUEST_VALIDITY_PERIOD))
            throw new UserAuthorizationException(String.format("request expired, clientTime=%s, serverTime=%s",
                    Convert.toString(message.getTimestamp(), Convert.DATE_FORMAT_DATETIME),
                    Convert.toString(serverTimestamp, Convert.DATE_FORMAT_DATETIME)));

        String secretKey = clientRepository.findClientSecretKey(clientId);
        if (secretKey == null)
            throw new UserAuthorizationException("client does not exist or be inactive, clientId=" + clientId);

        String expectedSignature = message.sign(secretKey);

        if (!expectedSignature.equals(signature)) {
            //TODO(chi): remove after client upgrade
            expectedSignature = message.signCompatible(secretKey);
            if (!expectedSignature.equals(signature)) {
                logger.debug("messageToBeSigned={}", message.constructToBeSignedMessage());
                logger.debug("expectedSignature={}", expectedSignature);
                throw new UserAuthorizationException("signature does not match");
            }
        }
    }

    public void authorizeClient(String clientId, String system, String operation) {
        boolean hasPermission = clientRepository.hasPermission(clientId, system, operation);
        if (!hasPermission)
            throw new UserAuthorizationException("client does not have permission");
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
