package br.com.leonardoz.patterns.task_execution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Pattern: Background Task Executor
 * 
 * Motivation: Executing tasks outside the main thread is useful when some task
 * may take a long time to run and you don't want to wait for it to end.
 * 
 * Intent: Create a Background task executor with the ability to execute and
 * cancel tasks.
 * 
 * Applicability: Sending an email after a user registration in your web-app; or
 * executing a long running task in background; use the Background Task Executor
 * to not compromise the main thread or the current thread you're using.
 * 
 */
public class BackgroundTaskExecutor {

	public static interface OnInterruption<T> {
		void accept(Future<T> t, Exception exception);
	}

	public static interface OnShutdownError {
		void accept(ExecutorService executor, Exception exception);
	}

	private final ExecutorService executor;

	public BackgroundTaskExecutor(int threadsForTasks) {
		this.executor = Executors.newFixedThreadPool(threadsForTasks);
	}

	public <T> Future<T> execute(Callable<T> task) {
		var submited = executor.submit(task);
		return submited;
	}

	public <T> List<Future<T>> execute(List<Callable<T>> tasks) {
		var futureTasks = tasks.stream()
				.map(executor::submit)
				.collect(Collectors.toList());
		return futureTasks;
	}

	public <T> boolean cancel(Future<T> task) {
		var canceled = task.cancel(true);
		return canceled;
	}

	public <T> boolean cancel(List<FutureTask<T>> task) {
		var hasAFalse = task.stream()
				.map(f -> f.cancel(true))
				.anyMatch(b -> b.equals(false));
		return !hasAFalse;
	}

	public <T> List<Optional<T>> completeTask(List<Future<T>> tasks, OnInterruption<T> onInterruption) {
		Function<Future<T>, Optional<T>> fn = (task) -> {
			try {
				return Optional.ofNullable(task.get());
			} catch (InterruptedException | ExecutionException e) {
				onInterruption.accept(task, e);
				return Optional.empty();
			}
		};
		var results = tasks.stream()
				.map(fn)
				.collect(Collectors.toList());
		return results;
	}

	public <T> Optional<T> completeTask(Future<T> task, OnInterruption<T> onInterruption) {
		try {
			return Optional.ofNullable(task.get());
		} catch (InterruptedException | ExecutionException e) {
			onInterruption.accept(task, e);
			return Optional.empty();
		}
	}

	public void shutdownTasks(long timeout, TimeUnit timeUnit, OnShutdownError onShutdownError) {
		executor.shutdown();
		try {
			executor.awaitTermination(timeout, timeUnit);
		} catch (InterruptedException e) {
			onShutdownError.accept(executor, e);
		}
	}

	public List<Runnable> shutdownNowTasks(long timeout, TimeUnit timeUnit, OnShutdownError onShutdownError) {
		var remainingTasks = executor.shutdownNow();
		try {
			executor.awaitTermination(timeout, timeUnit);
		} catch (InterruptedException e) {
			onShutdownError.accept(executor, e);
		}
		return remainingTasks;
	}

}
