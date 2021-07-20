package com.github.lucasaquiles.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.github.lucasaquiles.config.QueueConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

public abstract class AbstractMessageConsumer<P> implements Consumer{

    private final Logger log = LoggerFactory.getLogger(AbstractMessageConsumer.class);

    private final Class<P> clazz;

    public AbstractMessageConsumer(Class<P> clazz) {
        this.clazz = clazz;
    }

    abstract public DeclaredQueuesEnum getQueue();
    abstract void consumes(P payload);

    @Inject
    private ObjectMapper objectMapper;

    public void onApplicationStart(@Observes StartupEvent event, QueueConfig queueConfig) {
        queueConfig.appendConsumer(getQueue(), this);
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

        try {
            final P payload = objectMapper.readValue(body, clazz);
            consumes(payload);
            log.info("M=handleDelivery, I=consumindo mensagem={}", payload);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
