package com.zyd.core.redis;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;

/**
 * User: Leon.wu
 * Date: 9/10/13
 */
public class RedisClient implements MethodInterceptor {

    JedisPool jedisPool;
    JedisCommands proxy;

    @PreDestroy
    public void cleanup() {
        jedisPool.destroy();
    }

    public JedisCommands jedis() {
        return proxy;
    }

    public void init(String host, int port) {
        jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
        createProxy();
    }

    void createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[] {JedisCommands.class});
        enhancer.setCallback(this);
        this.proxy = (JedisCommands) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return methodProxy.invoke(jedis, objects);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}
