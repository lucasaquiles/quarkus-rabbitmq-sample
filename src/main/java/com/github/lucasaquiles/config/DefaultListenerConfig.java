package com.github.lucasaquiles.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class DefaultListenerConfig  implements Consumer{

    private final Logger log = LoggerFactory.getLogger(DefaultListenerConfig.class);

    public void onApplicationStart(@Observes StartupEvent event, QueueConfig queueConfig) {
        queueConfig.appendConsumer(DeclaredQueuesEnum.SAMPLE_QUEUE, this);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        final String message = new String(body, StandardCharsets.UTF_8);

        log.info("M=handleDelivery, I=consumindo mensagem={}", message);
    }
}
