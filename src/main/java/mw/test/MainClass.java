package mw.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class MainClass {
    private final static String QUEUE_NAME = "message-queue";
    public static final boolean DURABLE = true;
    public static final String EXCHANGE = "logs";

    public static void main(String[] args) throws IOException {

/*        String[] test = new String[] {"test1", "test2"};
        for (String arg : test) {
            sendMessage(arg);
        }*/

          receiveMessage();
    }

    private static void receiveMessage() throws IOException {
        Connection connection;
        final Channel channel;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicQos(1);

            channel.exchangeDeclare(EXCHANGE, "fanout");
            channel.queueBind(QUEUE_NAME, EXCHANGE, "");
            channel.queueDeclare(QUEUE_NAME, DURABLE, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "utf-8");
                    log.info("Message received: {}", message);

                    try{
                        doWork(message);
                    } finally {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            channel.basicConsume(QUEUE_NAME, false, consumer);

        } finally {
            log.info("Started");
        }
    }

    private static void sendMessage(String arg) throws IOException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE, "fanout");
            channel.queueDeclare(QUEUE_NAME, DURABLE, false, false, null);
            channel.basicPublish(
                    EXCHANGE,
                    QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    getMessage(arg).getBytes(Charset.forName("utf-8"))
            );

            log.info("Done");
        } finally {
            if (channel != null) {
                channel.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }

    private static void doWork(String message) {
        for (char ch : message.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("InterruptedException", e);
                }
            }
        }
    }

    private static String getMessage(String arg) {

        int count = 0;
        for (char c : arg.toCharArray()) {
            if (c == '.') {
                count++;
            }
        }

        return String.format("[%s] %s", count, arg);
    }
}
