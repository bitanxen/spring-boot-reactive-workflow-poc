package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@Controller
public class RSocketController {

    @MessageMapping("rsocket.realtime")
    public Flux<String> getInteger() {
        return Flux.interval(Duration.ofMillis(500))
                .map(aLong -> UUID.randomUUID().toString());
    }
}
