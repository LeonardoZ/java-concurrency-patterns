package br.com.leonardoz.features.executors;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Thread creation is expensive and difficult to manage.
 * 
 * Executors help us to decouple task submission from execution.
 * 
 * We have 6 types of executors:
 * 
 * - Single Thread Executor: Uses a single worker to process tasks.
 * 
 * - Cached Thread Pool: Unbounded thread limit, good performance for long
 * running tasks.
 * 
 * - Fixed Thread Pool: Bounded thread limit, maintains the same thread pool
 * size.
 * 
 * - Scheduled Thread Pool: Bounded thread limit, used for delayed tasks.
 * 
 * - Single-Thread Scheduled Pool: Similar to the scheduled thread pool, but 
 * single-threaded, with only one active task at the time. 
 * 
 * - Work-Stealing Thread Pool: Based on Fork/Join Framework, applies the
 * work-stealing algorithm for balancing tasks, with available processors as a
 * paralellism level.
 * 
 * And 2 types of tasks:
 * 
 * - execute: Executes without giving feedback. Fire-and-forget.
 * 
 * - submit: Returns a FutureTask.
 * 
 * ThreadPools: Used by the executors described above. ThreadPoolExecutor can be
 * used to create custom Executors.
 * 
 * shutdown() -> Waits for tasks to terminate and release resources.
 * shutdownNow() -> Try to stops all executing tasks and returns a list of not
 * executed tasks.
 *
 */
public class UsingExecutors {

	public static void usingSingleThreadExecutor() {
		System.out.println("=== SingleThreadExecutor ===");
		var singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(() -> System.out.println("Print this."));
		singleThreadExecutor.execute(() -> System.out.println("and this one to."));
		singleThreadExecutor.shutdown();
		try {
			singleThreadExecutor.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}

	public static void usingCachedThreadPool() {
		System.out.println("=== CachedThreadPool ===");
		var cachedThreadPool = Executors.newCachedThreadPool();
		var uuids = new LinkedList<Future<UUID>>();
		for (int i = 0; i < 10; i++) {
			var submittedUUID = cachedThreadPool.submit(() -> {
				var randomUUID = UUID.randomUUID();
				System.out.println("UUID " + randomUUID + " from " + Thread.currentThread().getName());
				return randomUUID;
			});
			uuids.add(submittedUUID);
		}
		cachedThreadPool.execute(() -> uuids.forEach((f) -> {
			try {
				System.out.println("Result " + f.get() + " from thread " + Thread.currentThread().getName());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}));
		cachedThreadPool.shutdown();
		try {
			cachedThreadPool.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");

	}

	public static void usingFixedThreadPool() {
		System.out.println("=== FixedThreadPool ===");
		var fixedPool = Executors.newFixedThreadPool(4);
		var uuids = new LinkedList<Future<UUID>>();
		for (int i = 0; i < 20; i++) {
			var submitted = fixedPool.submit(() -> {
				var randomUUID = UUID.randomUUID();
				System.out.println("UUID " + randomUUID + " from " + Thread.currentThread().getName());
				return randomUUID;
			});
			uuids.add(submitted);
		}
		fixedPool.execute(() -> uuids.forEach((f) -> {
			try {
				System.out.println("Result " + f.get() + " from " + Thread.currentThread().getName());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}));
		fixedPool.shutdown();
		try {
			fixedPool.awaitTermination(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}

	public static void usingScheduledThreadPool() {
		System.out.println("=== ScheduledThreadPool ===");
		var scheduledThreadPool = Executors.newScheduledThreadPool(4);
		scheduledThreadPool.scheduleAtFixedRate(() -> System.out.println("1) Print every 2s"), 0, 2, TimeUnit.SECONDS);
		scheduledThreadPool.scheduleAtFixedRate(() -> System.out.println("2) Print every 2s"), 0, 2, TimeUnit.SECONDS);
		scheduledThreadPool.scheduleWithFixedDelay(() -> System.out.println("3) Print every 2s delay"), 0, 2,
				TimeUnit.SECONDS);

		try {
			scheduledThreadPool.awaitTermination(6, TimeUnit.SECONDS);
			scheduledThreadPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}
	
	public static void usingSingleTreadScheduledExecutor() {
		System.out.println("=== SingleThreadScheduledThreadPool ===");
		var singleThreadScheduler = Executors.newSingleThreadScheduledExecutor();
		singleThreadScheduler.scheduleAtFixedRate(() -> System.out.println("1) Print every 2s"), 0, 2, TimeUnit.SECONDS);
		singleThreadScheduler.scheduleWithFixedDelay(() -> System.out.println("2) Print every 2s delay"), 0, 2,
				TimeUnit.SECONDS);

		try {
			singleThreadScheduler.awaitTermination(6, TimeUnit.SECONDS);
			singleThreadScheduler.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
		
	}

	public static void usingWorkStealingThreadPool() {
		System.out.println("=== WorkStealingThreadPool ===");
		var workStealingPool = Executors.newWorkStealingPool();

		workStealingPool.execute(() -> System.out.println("Prints normally"));

		Callable<UUID> generatesUUID = UUID::randomUUID;
		var severalUUIDsTasks = new LinkedList<Callable<UUID>>();
		for (int i = 0; i < 20; i++) {
			severalUUIDsTasks.add(generatesUUID);
		}

		try {
			var futureUUIDs = workStealingPool.invokeAll(severalUUIDsTasks);
			for (var future : futureUUIDs) {
				if (future.isDone()) {
					var uuid = future.get();
					System.out.println("New UUID :" + uuid);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		try {
			workStealingPool.awaitTermination(6, TimeUnit.SECONDS);
			workStealingPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\n\n");
	}

	

	public static void main(String[] args) {
		usingSingleThreadExecutor();
		usingCachedThreadPool();
		usingFixedThreadPool();
		usingScheduledThreadPool();
		usingSingleTreadScheduledExecutor();
		usingWorkStealingThreadPool();
	}
}
