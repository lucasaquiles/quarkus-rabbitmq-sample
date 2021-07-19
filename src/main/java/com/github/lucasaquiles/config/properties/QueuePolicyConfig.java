package com.github.lucasaquiles.config.properties;

import com.github.lucasaquiles.config.properties.RabbitMQPropertiesConfig;
import io.smallrye.config.ConfigMapping;

import java.util.Map;

@ConfigMapping(prefix = "queue-policy", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface QueuePolicyConfig {

    Map<String, RabbitMQPropertiesConfig> myQueueName();
}
