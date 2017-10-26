package org.nashorn.server;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;


import java.io.IOException;
;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

public class CommandExecutor implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(CommandExecutor.class);

    private ExecutorService executor;

    private final String srcQueueName;
    private final String dstQueueName;
    private final Connection conn;
    private final Channel channel;

    private volatile boolean done;


    public CommandExecutor(ConnectionFactory factory, String srcQueueName, String dstQueueName, ExecutorService executor)
            throws IOException, TimeoutException {
        this.conn = factory.newConnection();
        this.channel = this.conn.createChannel();
        this.srcQueueName = srcQueueName;
        this.dstQueueName = dstQueueName;
        this.executor = executor;

        this.channel.queueDeclare(this.srcQueueName, false, false, false, null);
    }

    @Override
    public void run() {
        LOGGER.info("COMMAND EXECUTOR RUN()");
        final Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                LOGGER.info("Handle delivery");
                String message = new String(body, "UTF-8");

                LOGGER.info("Received " + message);
            }
        };


            try {
                while (!done) {
                    channel.basicConsume(srcQueueName, true, consumer);
                }
            } catch (IOException ex) {
               LOGGER.error("Error during receiving message", ex);
            } finally {
                done = true;
                close(channel);
                close(conn);
            }
        }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException ex) {
                LOGGER.error("Cannot close connection", ex);
            }
        }
    }

    private void close(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException ex) {
                LOGGER.error("Cannot close channel", ex);
            } catch (TimeoutException tex) {
                LOGGER.error("Timeout has expired during closing the channel", tex);
            }
        }
    }
}
