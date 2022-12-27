package org.gershaw.quickfixj.springboot.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BootifiedClientTest {

    @Test
    void testMain() throws IOException {
        BootifiedClient.main(new String[]{});
        Assertions.assertTrue(true);
    }
}