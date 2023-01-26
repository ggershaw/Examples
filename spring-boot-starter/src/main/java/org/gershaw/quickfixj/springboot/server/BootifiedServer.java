package org.gershaw.quickfixj.springboot.server;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableQuickFixJServer
@SpringBootApplication
@Slf4j
public class BootifiedServer {

    public static void main(String[] args) {
        SpringApplication.run(BootifiedServer.class, args);
    }

}
