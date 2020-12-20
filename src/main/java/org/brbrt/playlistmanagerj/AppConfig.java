package org.brbrt.playlistmanagerj;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class AppConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    Logger logger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

    @Bean(destroyMethod = "stop")
    StreamPlayer streamPlayer() {
        return new StreamPlayer();
    }

}
