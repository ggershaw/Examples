package org.gershaw.quickfixj.server;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class Server {

  private final ApplicationContext context;


  public static void main(String[] args) throws IOException {
    ApplicationContext context = SpringApplication.run(Server.class, args);
    Server server = context.getBean(Server.class);
    server.printMessage();
    System.in.read();
    server.stop();
  }

  private void stop() {
    FixAcceptor acceptor = context.getBean(FixAcceptor.class);
    acceptor.stop();
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
