package com.github.lucasaquiles.consumer;

import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.github.lucasaquiles.domain.BasicInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Superior
@Singleton
public class SimpleRetriableMessageConsumer extends AbstractMessageConsumer<BasicInformation> {

    private Logger log = LoggerFactory.getLogger(SimpleRetriableMessageConsumer.class);

    public SimpleRetriableMessageConsumer() {
        super(BasicInformation.class);
    }

    @Override
    public DeclaredQueuesEnum getQueue() {
        return DeclaredQueuesEnum.SAMPLE_QUEUE_TWO;
    }

    @Override
    void consumes(BasicInformation payload) {
        log.info("M=consumer, I=recebeu a mensagem no consumer. payload={}", payload);
        throw new RuntimeException("away exception");
    }
}
