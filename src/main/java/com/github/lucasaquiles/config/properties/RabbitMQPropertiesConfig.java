package com.github.lucasaquiles.config.properties;

public class RabbitMQPropertiesConfig {

    public boolean retryable;

    public int maxRetry;

    public boolean dlq;

    @Override
    public String toString() {
        return "RabbitMQPropertiesConfig{" +
                "retryable=" + retryable +
                ", maxRetry=" + maxRetry +
                ", dlq=" + dlq +
                '}';
    }
}
