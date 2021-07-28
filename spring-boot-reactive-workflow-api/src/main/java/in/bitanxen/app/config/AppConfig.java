package in.bitanxen.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
//@EnableReactiveMongoRepositories
public class AppConfig {

    @Bean
    public LoggingEventListener mongoEventListener() {
        return new LoggingEventListener();
    }
}
