package com.github.lucasaquiles;

import com.github.lucasaquiles.config.QueueConfig;
import com.github.lucasaquiles.producer.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private final Logger log = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    private MessageSender messageSender;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(final String message) {
        log.info("M=hello, I=receiving message to send. message={}", message);

        messageSender.send(message);

        return message;
    }




}