package org.gershaw.quickfixj.springboot.client;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableQuickFixJClient
@SpringBootApplication
public class BootifiedClient
{
    public static void main( String[] args )
    {
        SpringApplication.run(BootifiedClient.class);
    }
}
