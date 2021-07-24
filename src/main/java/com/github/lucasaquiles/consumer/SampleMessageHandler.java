package com.github.lucasaquiles.consumer;

import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.github.lucasaquiles.domain.BasicInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;


@Superior
@Singleton
public class SampleMessageHandler extends MessageHandler<BasicInformation> {

    private final Logger log = LoggerFactory.getLogger(SampleMessageHandler.class);

    public SampleMessageHandler() {
        super(BasicInformation.class);
    }

    @Override
    public DeclaredQueuesEnum getQueue() {
        return DeclaredQueuesEnum.SAMPLE_QUEUE;
    }

    @Override
    void consumes(BasicInformation payload) {
        log.info("M=consumes, I=mensagem. payload={}", payload);
    }
}
