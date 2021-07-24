package com.github.lucasaquiles.config;

import com.github.lucasaquiles.config.properties.QueuePolicyConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class QueueConfig {

    private final Logger log = LoggerFactory.getLogger(QueueConfig.class);

    @Inject
    private RabbitMQClient rabbitMQClient;

    @Inject
    private QueuePolicyConfig queuePolicyConfig;

    private Channel channel;

    public void onApplicationStart(@Observes StartupEvent startupEvent) {

        log.info("M=onApplicationStart, I=starting queue config. conf={}", queuePolicyConfig);
        createQueues();
    }

    public void createQueues() {
        try {

            Connection connection = rabbitMQClient.connect();
            channel = connection.createChannel();

            Stream.of(DeclaredQueuesEnum.values())
                    .forEach((i) -> {
                        log.info("M=createQueues, I=criando item, item={}", i);
                        createQueue(i, channel);
                    });

        } catch (IOException e) {
            log.error("M=createQueues, I=Erro criando queue. error={}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createQueue(final DeclaredQueuesEnum queueDetail, Channel channel) {
        try {

            final String exchangeName = queueDetail.getExchangeName();

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);

            log.info("M=createQueue, I=exchange criada. exchange={}", exchangeName);

            createQueue(channel, queueDetail.getQueueName(), queueDetail.getExchangeName(), queueDetail.getQueueName(), false);
            channel.queueDeclare();

            if (queueDetail.isWithDlq()) {
                createQueue(channel, queueDetail.getDLQName(), queueDetail.getExchangeName(), queueDetail.getQueueName(), false);
            }

            if (queueDetail.isRetryableQueue()) {
                createQueue(channel, queueDetail.getRetryName(), queueDetail.getExchangeName(), queueDetail.getQueueName(), true);
            }
        } catch (IOException e) {
            log.error("M=createQueue, I=Erro criando queue. error={}", e.getMessage());
        }
    }

    private void createQueue(Channel channel, String queueName, String exchangeName, String originalQueueName, boolean dlq) throws IOException {
        final Map<String, Object> queueArgs = new HashMap<>();
        log.info("M=createQueue, I=criando queue, queue={}", queueName);

        if (dlq) {
            queueArgs.put("x-dead-letter-exchange", exchangeName);
            queueArgs.put("x-dead-letter-routing-key", originalQueueName);
        }

        channel.queueDeclare(queueName, true, false, false, queueArgs);
        channel.queueBind(queueName, exchangeName, queueName);
    }


    public void appendConsumer(DeclaredQueuesEnum declaredQueuesEnum, Consumer consumer) {
        try{

            channel = rabbitMQClient.connect().createChannel();
            channel.basicConsume(declaredQueuesEnum.getQueueName(), true, consumer);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
