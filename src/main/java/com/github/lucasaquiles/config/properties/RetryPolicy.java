package com.github.lucasaquiles.config.properties;

public interface RetryPolicy {

    int maxRetry();
    String retryStrategy();
}
