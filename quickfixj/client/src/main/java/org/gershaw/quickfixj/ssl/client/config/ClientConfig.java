package org.gershaw.quickfixj.ssl.client.config;

import org.gershaw.quickfixj.ssl.client.FixInitiator;
import org.gershaw.quickfixj.ssl.client.NoOpApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.Initiator;
import quickfix.MemoryStoreFactory;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

@Configuration
@SuppressWarnings("unused")
class ClientConfig {

  @Bean
  SessionSettings sessionSettings(@Value("${quickfixj.config}") final String configFile)
      throws ConfigError {
    return new SessionSettings(configFile);
  }

  @Bean
  Initiator initiator(final SessionSettings sessionSettings,
      final Application application) throws ConfigError {
    return new SocketInitiator(
        application,
        new MemoryStoreFactory(),
        sessionSettings,
        new SLF4JLogFactory(sessionSettings),
        new DefaultMessageFactory());
  }

  @Bean
  Application application() {
    return new NoOpApplication();
  }

  @Bean
  FixInitiator fixInitiator(final Initiator initiator) {
    return new FixInitiator(initiator);
  }

}
