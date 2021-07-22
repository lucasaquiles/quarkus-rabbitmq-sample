package com.github.lucasaquiles.config.properties;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;

import java.util.Map;

@ConfigMapping(prefix = "rabbit-queue")
public interface QueuePolicyConfig {

    @WithParentName
    Map<String, RabbitMQPropertiesConfig> queuePolicy();
}
