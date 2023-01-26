package org.gershaw.quickfixj.springboot.client;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
import lombok.extern.slf4j.Slf4j;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@EnableQuickFixJClient
@SpringBootApplication
@Slf4j
public class BootifiedClient
{
    public static void main( String[] args ) throws IOException {
        SpringApplication.run(BootifiedClient.class);
        Utils.printMessage();
        System.in.read();
    }


}
