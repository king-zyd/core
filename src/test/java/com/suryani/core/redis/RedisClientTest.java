package com.zyd.core.redis;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import static org.junit.Assert.assertEquals;

/**
 * User: Leon.wu
 * Date: 9/11/13
 */
public class RedisClientTest extends EasyMockSupport {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    RedisClient redisClient;

    @Before
    public void setUp() throws Exception {
        redisClient = new RedisClient();
    }

    @Test
    public void testJedisSet() throws Exception {
        JedisPool jedisPool = createMock(JedisPool.class);
        Jedis jedis = createMock(Jedis.class);

        EasyMock.expect(jedisPool.getResource()).andReturn(jedis);
        jedisPool.returnResource(jedis);
        EasyMock.expect(jedis.set("key1", "value1")).andReturn("OK");

        replayAll();

        redisClient.jedisPool = jedisPool;
        redisClient.createProxy();

        JedisCommands jedisCommands = redisClient.jedis();
        String ret = jedisCommands.set("key1", "value1");

        assertEquals(ret, "OK");

        verifyAll();
    }

    @Test
    public void testJedisThrowException() throws Exception {
        exception.expect(JedisException.class);
        exception.expectMessage("my test error");

        JedisPool jedisPool = createMock(JedisPool.class);
        Jedis jedis = createMock(Jedis.class);

        EasyMock.expect(jedisPool.getResource()).andReturn(jedis);
        jedisPool.returnResource(jedis);
        EasyMock.expect(jedis.set("key1", "value1")).andThrow(new JedisException("my test error"));

        replayAll();

        redisClient.jedisPool = jedisPool;
        redisClient.createProxy();

        JedisCommands jedisCommands = redisClient.jedis();
        jedisCommands.set("key1", "value1");

        verifyAll();
    }
}
