package mw.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class MainClass {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {

        // sendMessage();
         receiveMessage();
    }

    private static void receiveMessage() throws IOException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "utf-8");
                    log.info("Message received: {}", message);
                }
            };

            channel.basicConsume(QUEUE_NAME, true, consumer);

            log.info("Done");
        }finally {
            if (channel != null) {
                channel.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }

    private static void sendMessage() throws IOException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, "Hello World!".getBytes(Charset.forName("utf-8")));

            log.info("Done");
        }finally {
            if (channel != null) {
                channel.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
