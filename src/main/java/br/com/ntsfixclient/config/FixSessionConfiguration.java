package br.com.ntsfixclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;

import java.io.*;
import java.net.URL;
import java.util.Properties;

@Configuration
@Slf4j
public class FixSessionConfiguration {

    @Bean
    public SessionSettings sessionSettings() throws ConfigError, IOException {
        final Properties variableValues = new Properties();
        variableValues.setProperty("brokerOrderPort", "10095");
        variableValues.setProperty("marketDataPort", "10096");
        variableValues.setProperty("exchangeOrderPort", "10097");
        variableValues.putAll(System.getProperties());
        final SessionSettings settings;
        try (final InputStream is = getConfigAsStream()) {
            settings = new SessionSettings(is);
        }
        settings.setVariableValues(variableValues);
        return settings;
    }

    @Bean
    public MessageStoreFactory messageStoreFactory(SessionSettings sessionSettings) {
        //return new FileStoreFactory(sessionSettings);
        return new NoopStoreFactory();
    }

    @Bean
    public LogFactory logFactory(SessionSettings sessionSettings) {
        return new ScreenLogFactory(true, true, true);
    }

    @Bean
    public MessageFactory messageFactory() {
        return new DefaultMessageFactory();
    }

    @Bean
    public Initiator initiator(Application application, MessageStoreFactory messageStoreFactory,
                               SessionSettings sessionSettings, LogFactory logFactory, MessageFactory messageFactory)
            throws ConfigError {
        SocketInitiator initiator = new SocketInitiator(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
        initiator.start();
        return initiator;
    }

    private static InputStream getConfigAsStream() throws IOException {
        final URL configResource = FixSessionConfiguration.class.getResource("/fixSession.cfg");
        final File configFile = (configResource == null) ? new File("/fixSession.cfg") : new File(configResource.getFile());
        log.info("Using config: {}", configFile.getCanonicalPath());
        return new BufferedInputStream(new FileInputStream(configFile));

    }

}