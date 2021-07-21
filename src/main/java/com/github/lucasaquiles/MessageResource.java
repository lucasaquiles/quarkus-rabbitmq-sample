package com.github.lucasaquiles;

import com.github.lucasaquiles.config.DeclaredQueuesEnum;
import com.github.lucasaquiles.domain.BasicInformation;
import com.github.lucasaquiles.producer.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/message")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageSender messageSender;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public BasicInformation sendToSimpleQueue(final BasicInformation message) {
        log.info("M=sendToSimpleQueue, I=receiving message to send. message={}", message);

        messageSender.send(DeclaredQueuesEnum.SAMPLE_QUEUE, message);

        return message;
    }


    @POST
    @Path("/retriable")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicInformation sendToRetriable(final BasicInformation message) {
        log.info("M=hello, I=receiving message to send. message={}", message);

        messageSender.send(DeclaredQueuesEnum.SAMPLE_QUEUE_TWO, message);

        return message;
    }




}