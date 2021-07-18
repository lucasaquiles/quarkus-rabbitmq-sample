package com.github.lucasaquiles.producer;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageFormatter {

    private final Logger log = LoggerFactory.getLogger(MessageFormatter.class);

    @Incoming("analytics")
    public String format(final String message) {

        log.info("M=format, I=received message={}", message);
        return message;
    }
}
