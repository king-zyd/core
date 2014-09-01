package com.zyd.core.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * User: Leon.wu
 * Date: 9/4/13
 */
public class AMQPClient {

    private final Logger logger = LoggerFactory.getLogger(AMQPClient.class);

    ConnectionFactory connectionFactory;

    @Inject
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @SuppressWarnings("PMD")
    public void sendMessage(String queue, String message) {
        Connection conn = null;
        Channel channel = null;
        try {
            conn = connectionFactory.newConnection();
            channel = conn.createChannel();
            channel.queueDeclare(queue, true, false, false, null);
            channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.error("send message failed", e);
        } finally {
            close(channel);
            close(conn);
        }
    }

    void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException e) {
                logger.error("close conn failed", e);
            }
        }
    }

    void close(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                logger.error("close channel failed", e);
            }
        }
    }

}
