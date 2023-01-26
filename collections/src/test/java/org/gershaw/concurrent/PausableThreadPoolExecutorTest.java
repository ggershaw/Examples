package org.gershaw.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PausableThreadPoolExecutorTest {
  private PausableThreadPoolExecutor threadPoolExecutor;

  @AfterEach
  void tearDown() {
    threadPoolExecutor.shutdownNow();
  }

  @Test
  @Disabled
  void pauseAndResume() {
    BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();

    threadPoolExecutor = new PausableThreadPoolExecutor(2,4,10, TimeUnit.SECONDS,blockingQueue);
    threadPoolExecutor.pause();
    threadPoolExecutor.execute(PausableThreadPoolExecutorTest::sleepingRunnable);

    Assertions.assertEquals(1, threadPoolExecutor.getQueue().size());

    threadPoolExecutor.resume();
    threadPoolExecutor.execute(PausableThreadPoolExecutorTest::sleepingRunnable);

    Assertions.assertEquals(0, threadPoolExecutor.getQueue().size());

  }
  private static Runnable sleepingRunnable() {
    return new Runnable() {
      @SneakyThrows
      @Override
      public void run() {
        Thread.sleep(1000);
      }
    };
  }
}