package org.gershaw.quickfixj.ssl.client;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Client {

  private final ApplicationContext context;

  public static void main(String[] args) throws IOException {
    ApplicationContext context = SpringApplication.run(Client.class, args);
    Client client = context.getBean(Client.class);
    client.printMessage();
    System.in.read();
    client.stop();
  }

  private void stop() {
    FixInitiator initiator = context.getBean(FixInitiator.class);
    initiator.stop();
  }

  private void printMessage() {
    log.info("");
    log.info(
        "*************************************************************************************");
    log.info(
        "*************************************************************************************");
    log.info(
        "*****************************Press <enter> twice to quit*****************************");
    log.info(
        "*************************************************************************************");
    log.info(
        "*************************************************************************************");
    log.info("");
  }
}
