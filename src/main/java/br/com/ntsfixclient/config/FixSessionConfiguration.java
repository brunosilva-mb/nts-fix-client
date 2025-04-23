package br.com.ntsfixclient.config;

import br.com.ntsfixclient.app.FixApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;

@Configuration
@Slf4j
public class FixSessionConfiguration {

    @Bean
    public SessionSettings sessionSettings() {
        try {
            // You'll need to create this file with your specific FIX session settings
            return new SessionSettings("resources/fixSession.cfg");
        } catch (ConfigError e) {
            log.error("Error loading FIX session settings", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MessageStoreFactory messageStoreFactory() {
        return new FileStoreFactory(sessionSettings());
    }

    @Bean
    public LogFactory logFactory() {
        return new SLF4JLogFactory(sessionSettings());
    }

    @Bean
    public MessageFactory messageFactory() {
        return new DefaultMessageFactory();
    }

    @Bean
    public Initiator initiator(MessageStoreFactory messageStoreFactory,
                             SessionSettings sessionSettings,
                             LogFactory logFactory,
                             MessageFactory messageFactory) throws ConfigError {
        return new SocketInitiator(
                fixApplication(),
                messageStoreFactory,
                sessionSettings,
                logFactory,
                messageFactory
        );
    }

    @Bean
    public FixApplication fixApplication() {
        return new FixApplication();
    }
}