package br.com.leonardoz.features.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Concurrent collections are an alternative to the Synchronized Collections.
 *
 * Supports concurrent access from n threads and performs better than
 * Synchronized collections.
 *
 */
public class UsingConcurrentCollections {

	/**
	 * Map for Concurrent access. Sacrifices some aspects of synchronization to
	 * achieve better performance.
	 *
	 * Locks it's values with Lock Striping.
	 *
	 * Lock Striping divides the protected region through several locks.
	 *
	 * - Don't throw ConcurrentModificationException
	 *
	 * - size() and isEmpty() can be incorrect. Don't rely on them.
	 *
	 * - Supports atomic operations, don't need client side locking.
	 *
	 * - Readers can access concurrently, and iterator have weak consistency.
	 *
	 * - Just a few Writers can modify it.
	 *
	 * Java 8+ additions demonstrated below:
	 *
	 * - {@code merge(key, value, remappingFn)} — atomically combines a new value
	 *   with an existing one using a merge function; ideal for frequency counting.
	 *
	 * - {@code compute(key, remappingFn)} — atomically computes a new value for a
	 *   key based on its current value (or null if absent).
	 *
	 * - {@code forEach(parallelismThreshold, action)} — applies an action to each
	 *   entry, processing in parallel when the map size exceeds the threshold.
	 *
	 * - {@code reduceValues(parallelismThreshold, reducer)} — parallel reduction
	 *   over values; returns null if the map is empty.
	 *
	 * - {@code search(parallelismThreshold, searchFn)} — returns the first
	 *   non-null result of applying the function, stopping as soon as found.
	 */
	public static void usingConcurrentHashMap() {
		System.out.println("=== ConcurrentHashMap ===");
		var executor = Executors.newCachedThreadPool();
		var random = new Random();
		var valuesPerUuid = new ConcurrentHashMap<UUID, Integer>();
		// atomic operations
		valuesPerUuid.putIfAbsent(UUID.randomUUID(), random.nextInt(100));

		// simulating concurrent access - no duplicates should be inserted
		for (int i = 0; i < 100; i++) {
			if (i % 6 == 0) {
				// write
				executor.execute(() -> {
					UUID uuid = UUID.randomUUID();
					Integer value = random.nextInt(10);
					System.out.println("Added " + uuid + " - " + value);
					valuesPerUuid.putIfAbsent(uuid, value);
				});
			} else {
				// read
				executor.execute(() -> System.out.println("Printed " + valuesPerUuid.values().toString()));
			}
		}

		// Finishing
		executor.shutdown();
		try {
			executor.awaitTermination(2000, TimeUnit.SECONDS);
			// space for other examples
			Thread.sleep(2000);
			System.out.println("\n\n\n\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// -----------------------------------------------------------------------
		// Java 8+ bulk operations on ConcurrentHashMap
		// -----------------------------------------------------------------------
		System.out.println("=== ConcurrentHashMap — Java 8+ bulk operations ===");

		var words = Arrays.asList("apple", "banana", "apple", "cherry", "banana", "apple");

		// merge: atomically increment a word counter; no external locking needed.
		var wordCounts = new ConcurrentHashMap<String, Integer>();
		for (var word : words) {
			wordCounts.merge(word, 1, Integer::sum);
		}
		System.out.println("Word counts (merge): " + wordCounts);

		// compute: square the count of "apple" in-place atomically.
		wordCounts.compute("apple", (k, v) -> v == null ? 1 : v * v);
		System.out.println("After compute square of 'apple': " + wordCounts.get("apple"));

		// forEach with parallelism threshold (process in parallel for large maps)
		System.out.println("forEach (parallel threshold = 2):");
		wordCounts.forEach(2, (k, v) ->
				System.out.println("  " + k + " -> " + v));

		// reduceValues: sum all counts in parallel
		Integer total = wordCounts.reduceValues(2, Integer::sum);
		System.out.println("Total word occurrences (reduceValues): " + total);

		// search: find the first word whose count is > 1
		String popular = wordCounts.search(2, (k, v) -> v > 1 ? k : null);
		System.out.println("First word with count > 1 (search): " + popular);

		System.out.println("\n");
	}

	/**
	 * Replacement for synchronized list. Based on the immutable object concept.
	 * 
	 * Use when reading is far more common than writing.
	 * 
	 * Creates a new copy every time that the list is modified, only synchronizing
	 * briefly to ensure array content visibility.
	 * 
	 * iterator returns a snapshot of the current state of the collection.
	 * 
	 * Supports atomic operations.
	 * 
	 */
	public static void usingCopyOnWriteArrayList() {
		System.out.println("=== CopyOnWriteArrayList ===");
		var executor = Executors.newCachedThreadPool();
		var random = new Random();
		// No ConcurrentModificationException
		var copyOnWriteArrayList = new CopyOnWriteArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			if (i % 8 == 0) {
				// write
				executor.execute(() -> {
					var value = random.nextInt(10);
					System.err.println("Added " + value);
					copyOnWriteArrayList.add(value);
				});
			} else {
				// read
				executor.execute(() -> {
					var builder = new StringBuilder();
					for (var value : copyOnWriteArrayList) {
						builder.append(value + " ");
					}
					System.out.println("Reading " + builder.toString());
				});
			}
		}

		// Finishing
		executor.shutdown();
		try {
			executor.awaitTermination(2000, TimeUnit.SECONDS);
			// space for other examples
			Thread.sleep(2000);
			System.out.println("\n\n\n\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Concurrent Queue interface.
	 * 
	 * Implementations: LinkedBlockingQueue, ArrayBlockingQueue,
	 * PriorityBlockingQueue, SynchronizedQueue.
	 * 
	 * Used for the Producer-Consumer pattern.
	 * 
	 * Blocking methods: put/take; Timed blocking methods: offer, poll;
	 * 
	 * Can be bounded or unbounded.
	 * 
	 */
	public static void usingBlockingQueue() {
		System.out.println("=== BlockingQueue ===");

		// Bounded UUID queue
		var uuidQueue = new LinkedBlockingQueue<UUID>(10);

		System.out.println("Queue will execute for 10s");

		// Multiple consumers
		Runnable runConsumer = () -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					var uuid = uuidQueue.take();
					System.out.println("Consumed: " + uuid + " by " + Thread.currentThread().getName());

				} catch (InterruptedException e) {
					// interrupted pattern
					// InterruptedException makes isInterrupted returns false
					Thread.currentThread().interrupt(); 
					System.err.println("Consumer Finished");
				}
			}
		};
		var consumer1 = new Thread(runConsumer);
		consumer1.start();
		var consumer2 = new Thread(runConsumer);
		consumer2.start();

		// Producer Thread
		Runnable runProducer = () -> {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					Random r = new Random();
					// Delay producer
					Thread.sleep(r.nextInt(1000));
					UUID randomUUID = UUID.randomUUID();
					System.out.println("Produced: " + randomUUID + " by " + Thread.currentThread().getName());
					uuidQueue.put(randomUUID);
				}
			} catch (InterruptedException e) {
				// interrupted pattern
				System.err.println("Producer Finished");
			}
		};

		// Multiple producers - Examples using simple threads this time.
		var producer1 = new Thread(runProducer);
		producer1.start();
		var producer2 = new Thread(runProducer);
		producer2.start();
		var producer3 = new Thread(runProducer);
		producer3.start();

		try {
			// Queue will run for 10secs
			Thread.sleep(10000);
			producer1.interrupt();
			producer2.interrupt();
			producer3.interrupt();
			consumer1.interrupt();
			consumer2.interrupt();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		usingConcurrentHashMap();
		usingCopyOnWriteArrayList();
		usingBlockingQueue();
	}

}
