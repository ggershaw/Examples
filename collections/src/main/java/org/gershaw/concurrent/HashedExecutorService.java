package org.gershaw.concurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashedExecutorService implements ExecutorService {

  private final ExecutorService[] executorServices;

  public HashedExecutorService(final int numThreads) {
    executorServices = new ExecutorService[numThreads];
    Arrays.fill(executorServices, Executors.newSingleThreadExecutor());
    log.info("Initializing {} with {} Threads", this.getClass().getSimpleName(), executorServices.length);
  }

  public void execute(Runnable runnable) {
    ExecutorService executorService = getExecutorService(runnable);
    executorService.execute(runnable);
  }

  @Override
  public void shutdown() {
    Arrays.stream(executorServices)
        .forEach(ExecutorService::shutdown);
  }

  @Override
  public List<Runnable> shutdownNow() {
    return Arrays.stream(executorServices)
        .flatMap(executorService -> executorService.shutdownNow().stream())
        .collect(Collectors.toList());
  }

  @Override
  public boolean isShutdown() {
    return Arrays.stream(executorServices)
        .map(ExecutorService::isShutdown)
        .allMatch(b -> b.equals(Boolean.TRUE));
  }

  @Override
  public boolean isTerminated() {
    return Arrays.stream(executorServices)
        .map(ExecutorService::isTerminated)
        .allMatch(b -> b.equals(Boolean.TRUE));
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    final List<Boolean> results = new ArrayList<>(executorServices.length);

    for (ExecutorService executorService : executorServices) {
      try {
        boolean isTerminated = executorService.awaitTermination(timeout, unit);
        results.add(isTerminated);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    //TODO find the best way to do this
 return false;

  }

  private static boolean waitForTermination(final long timeout, final TimeUnit unit,
      final ExecutorService e)
      throws InterruptedException {
    return e.awaitTermination(timeout, unit);
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    ExecutorService executorService = getExecutorService(task);
    return executorService.submit(task);
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    ExecutorService executorService = getExecutorService(task);
    return executorService.submit(task, result);
  }

  @Override
  public Future<?> submit(Runnable task) {
    ExecutorService executorService = getExecutorService(task);
    return executorService.submit(task);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return null;
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout,
      TimeUnit unit) throws InterruptedException {
    return null;
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    return null;
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return null;
  }

  private ExecutorService getExecutorService(Object o) {
    int hash = Math.abs(o.hashCode() % executorServices.length);
    return executorServices[hash];
  }
}
