package com.github.lucasaquiles.config.properties;

public interface RabbitMQPropertiesConfig {

    RetryPolicy retryPolicy();

    String exchange();
    boolean dlq();




//    @Override
//    public String toString() {
//        return "RabbitMQPropertiesConfig{" +
//                "retryable=" + retryable +
//                ", maxRetry=" + maxRetry +
//                ", dlq=" + dlq +
//                '}';
//    }
}
