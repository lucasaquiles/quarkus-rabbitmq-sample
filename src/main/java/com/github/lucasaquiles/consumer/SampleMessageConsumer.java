package com.github.lucasaquiles.consumer;

import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;


@Superior
@Singleton
public class SampleMessageConsumer extends AbstractMessageConsumer<String> {

    private final Logger log = LoggerFactory.getLogger(SampleMessageConsumer.class);

    public SampleMessageConsumer() {
        super(String.class);
    }

    @Override
    public DeclaredQueuesEnum getQueue() {
        return DeclaredQueuesEnum.SAMPLE_QUEUE;
    }

    @Override
    void consumes(String payload) {
        log.info("M=consumes, I=mensagem. payload={}", payload);
    }
}
