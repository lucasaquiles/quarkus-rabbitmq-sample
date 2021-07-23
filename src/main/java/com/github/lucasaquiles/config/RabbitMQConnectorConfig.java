package com.github.lucasaquiles.config;

import io.quarkiverse.rabbitmqclient.RabbitMQClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RabbitMQConnectorConfig {

    @Inject
    private RabbitMQClient rabbitMQClient;

    
}
