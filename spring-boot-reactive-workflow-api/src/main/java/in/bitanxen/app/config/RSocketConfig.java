package in.bitanxen.app.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.rsocket.core.Resume;
import io.rsocket.frame.ResumeFrameCodec;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.resume.InMemoryResumableFramesStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import io.netty.buffer.ByteBuf;

@Configuration
@Slf4j
public class RSocketConfig {

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        ArgumentResolverConfigurer args = messageHandler.getArgumentResolverConfigurer();
        //args.addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

<<<<<<< HEAD
    /*
=======


>>>>>>> 8e24f36a13af0db718094080f209e53590f96732
    @Bean
    RSocketServerCustomizer rSocketResume() {
        Resume resume = new Resume()
                .sessionDuration(Duration.ofMinutes(20))
                .streamTimeout(Duration.ofMinutes(10))
                .cleanupStoreOnKeepAlive()
                .storeFactory(byteBuf -> {
                    return new InMemoryResumableFramesStore("1", byteBuf, 100);
                });
        return rSocketServer -> rSocketServer.resume(resume);
    }

     */
}
