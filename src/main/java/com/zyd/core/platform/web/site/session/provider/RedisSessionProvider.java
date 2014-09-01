package com.zyd.core.platform.web.site.session.provider;

import com.zyd.core.platform.monitor.ServiceStatus;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.redis.RedisClient;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Protocol;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * User: Leon.wu
 * Date: 9/10/13
 */
public class RedisSessionProvider implements SessionProvider {

    private SiteSettings siteSettings;

    RedisClient redisClient;

    @PostConstruct
    public void prepare() {
        redisClient = new RedisClient();
        redisClient.init(siteSettings.getRemoteSessionServers(), Protocol.DEFAULT_PORT);
    }

    @PreDestroy
    public void cleanup() {
        redisClient.cleanup();
    }

    @Override
    public String getAndRefreshSession(String sessionId) {
        String key = key(sessionId);
        JedisCommands jedis = redisClient.jedis();
        String sessionData = jedis.get(key);
        jedis.expire(key, expirationTime());
        return sessionData;
    }

    @Override
    public void saveSession(String sessionId, String sessionData) {
        String key = key(sessionId);
        JedisCommands jedis = redisClient.jedis();
        jedis.setex(key, expirationTime(), sessionData);
    }

    @Override
    public void clearSession(String sessionId) {
        String key = key(sessionId);
        JedisCommands jedis = redisClient.jedis();
        jedis.del(key);
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        return ServiceStatus.UP;
    }

    @Override
    public String getServiceName() {
        return "RedisSession";
    }

    private String key(String sessionId) {
        return String.format("session:%s", sessionId);
    }

    private int expirationTime() {
        return (int) siteSettings.getSessionTimeOut().toSeconds();
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }
}
