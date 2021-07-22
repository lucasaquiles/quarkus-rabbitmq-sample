package com.github.lucasaquiles.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.github.lucasaquiles.config.QueueConfig;
import com.github.lucasaquiles.producer.MessageSender;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractMessageConsumer<P> implements Consumer {

    private final Logger log = LoggerFactory.getLogger(AbstractMessageConsumer.class);
    private final String X_DEATH = "x-death";
    private final Class<P> clazz;

    public AbstractMessageConsumer(Class<P> clazz) {
        this.clazz = clazz;
    }

    abstract public DeclaredQueuesEnum getQueue();
    abstract void consumes(P payload);

    @Inject
    private MessageSender messageSender;

    @Inject
    private ObjectMapper objectMapper;

    public void onApplicationStart(@Observes StartupEvent event, QueueConfig queueConfig) {
        queueConfig.appendConsumer(getQueue(), this);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        log.info("M=handleConsumeOk, I=handler ok. consumerTag={}", consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        log.info("M=handleCancelOk, I=handler cancel ok. consumerTag={}", consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        log.info("M=handleCancel, I=handler cancel. consumerTag={}", consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        log.info("M=handleShutdownSignal, I=handler shutdown signal. consumerTag={}", consumerTag);
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        log.info("M=handleRecoverOk, I=handler recover. consumerTag={}", consumerTag);
    }

    @Override
    public void handleDelivery(final String consumerTag, final Envelope envelope, final BasicProperties properties, final byte[] body) {

        try {
            final P payload = objectMapper.readValue(body, clazz);
            consumes(payload);
            log.info("M=handleDelivery, I=mensagem consumida. mensagem={}", payload);
        } catch (Exception e) {

            log.error("M=handleDelivery, E=erro durante consumo. message={}", e.getMessage());

            retry(body, envelope, properties);
        }
    }

    private void retry(byte[] body, Envelope envelope, AMQP.BasicProperties properties) {

        log.info("M=retry, I=iniciando retentativa da fila, queue={}", envelope.getRoutingKey());
        long count = calcRetryCount(properties);

        if (count < getQueue().getMaxRetry()) {

            long retryInterval = getQueue().calculateRetryInterval(count);

            log.info("M=retry, I=deve retentar em, ttl={}", Duration.of(retryInterval, ChronoUnit.SECONDS).toSeconds());
            final BasicProperties updatedMessagePropertie = updateMessagePropertie(properties, retryInterval);

            messageSender.send(getQueue().getRetryName(), getQueue().getExchangeName(), new String(body, StandardCharsets.UTF_8), updatedMessagePropertie);
        } else {

            properties.getHeaders().remove(X_DEATH);
            messageSender.send(getQueue().getDLQName(), getQueue().getExchangeName(), new String(body, StandardCharsets.UTF_8), properties);
        }
    }

    private long calcRetryCount(AMQP.BasicProperties properties) {
        long count = 0L;

        final Map<String, Object> headers = properties.getHeaders();

        if (headers.containsKey(X_DEATH)) {

            final List list = (List) Collections.singletonList( headers.get(X_DEATH) ).get(0);
            count = Long.parseLong( ((Map) list.get(0)).get("count").toString());
        }

        return count++;
    }

    private AMQP.BasicProperties updateMessagePropertie(final AMQP.BasicProperties properties, final long ttl) {
        log.info("M=updateMessagePropertie, I=update ttlMessage");
        return new AMQP.BasicProperties("application/json",
                "UTF-8",
                properties.getHeaders(),
                properties.getDeliveryMode(),
                properties.getPriority(),
                properties.getCorrelationId(),
                properties.getReplyTo(),
                String.valueOf(ttl),
                properties.getMessageId(),
                properties.getTimestamp(),
                properties.getType(),
                properties.getUserId(),
                properties.getAppId(),
                properties.getClusterId()
        );
    }
}
