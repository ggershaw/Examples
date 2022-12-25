package org.gershaw.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;

public class HashedThreadPoolFactory implements  ThreadFactory {

  private final ConcurrentMap<Runnable, Thread> queueConcurrentMap;


  public HashedThreadPoolFactory() {
    queueConcurrentMap = new ConcurrentHashMap<>();
  }



  @Override
  public Thread newThread(Runnable r) {

        return null;
  }
}
