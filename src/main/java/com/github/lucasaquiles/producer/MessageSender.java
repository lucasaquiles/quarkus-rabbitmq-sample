package com.github.lucasaquiles.producer;

import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class MessageSender {

    private Logger log = LoggerFactory.getLogger(MessageSender.class);

    @Inject
    private RabbitMQClient rabbitMQClient;
    private Channel channel;

    public void send(final String message) {
        Connection connection = rabbitMQClient.connect();
        try {
            channel = connection.createChannel();

            final DeclaredQueuesEnum sampleQueue = DeclaredQueuesEnum.SAMPLE_QUEUE;

            channel.basicPublish(sampleQueue.getExchangeName(), sampleQueue.getQueueName(), null, message.getBytes());

            log.info("M=send, I=enviando mensagem. mensagem={}", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
