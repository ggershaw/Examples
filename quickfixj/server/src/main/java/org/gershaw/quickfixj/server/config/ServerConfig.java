package org.gershaw.quickfixj.server.config;

import org.gershaw.quickfixj.server.FixAcceptor;
import org.gershaw.quickfixj.server.NoOpApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.MemoryStoreFactory;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

@Configuration
@SuppressWarnings("unused")
class ServerConfig {

  @Bean
  SessionSettings sessionSettings(@Value("${quickfixj.config}") final String configFile)
      throws ConfigError {
    return new SessionSettings(configFile);
  }

  @Bean
  Acceptor acceptor(final SessionSettings sessionSettings,
      final Application application) throws ConfigError {
    return new SocketAcceptor(
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
  FixAcceptor fixAcceptor(final Acceptor acceptor) {
    return new FixAcceptor(acceptor);
  }

}
