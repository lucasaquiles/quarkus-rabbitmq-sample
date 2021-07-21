package com.github.lucasaquiles.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

@ApplicationScoped
public class MessageSender {

    private Logger log = LoggerFactory.getLogger(MessageSender.class);

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private RabbitMQClient rabbitMQClient;
    private Channel channel;

    public void send(final DeclaredQueuesEnum queue, final Serializable message) {
            final AMQP.BasicProperties basicProperties = new AMQP.BasicProperties("application/json",
                    "UTF-8",
                    new HashMap<>(),
                    1,
                    0, null, null, null,
                    null, null, null, null,
                    null, null);

            log.info("M=send, I=mensagem enviada. fila={}, mensagem={}", queue.getQueueName(), message);

            send(queue.getQueueName(), queue.getExchangeName(), message, basicProperties);
    }

    public void send(final String queueName, final String exchangeName, final Serializable message, final AMQP.BasicProperties basicProperties) {
        Connection connection = rabbitMQClient.connect();

        try {
            channel = connection.createChannel();
            channel.basicPublish(exchangeName, queueName, basicProperties, objectMapper.writeValueAsBytes(message));

            log.info("M=send, I=mensagem enviada. fila={}, mensagem={}", queueName, message);
        } catch (IOException e) {

            log.error("M=send, E=mesagem nao publicada, e={}", e.getMessage());
        }
    }
}
