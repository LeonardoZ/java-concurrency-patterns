package br.com.leonardoz.features.collections;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Synchronized collections synchronizes every public method to encapsulate
 * their state.
 * 
 * They're thread-safe, but if you use compound actions like size+add, they'rent
 * anymore, because these operations needs to be atomic.
 * 
 * It's required to use client side locking for compound actions.
 * 
 * Synchronized collections doesn't support concurrent iteration+modification.
 * They'll throw ConcurrentModificationException
 * 
 */
public class UsingSynchronizedCollections {

	/**
	 * Use client-side locking to guard compound actions; contains and add are
	 * synchronized, but this doesn't help when you need to use both in a compounded
	 * manner.
	 * 
	 * It's just an easy example to explain the problem, sets are better for this
	 * than a vector/list.
	 */
	public static void insertIfAbsent(Vector<Long> list, Long value) {
		synchronized (list) {
			var contains = list.contains(value);
			if (!contains) {
				list.add(value);
				System.out.println("Value added: " + value);
			}
		}
	}

	/**
	 * You can have duplicates. Try to run multiple times and see the diff in
	 * results
	 */
	public static void insertIfAbsentUnsafe(Vector<Long> list, Long value) {
		var contains = list.contains(value);
		if (!contains) {
			list.add(value);
			System.out.println("Value added: " + value);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var executor = Executors.newCachedThreadPool();
		// Synchronized - Vector
		var vector = new Vector<Long>();

		Runnable insertIfAbsent = () -> {
			long millis = System.currentTimeMillis() / 1000;
			insertIfAbsent(vector, millis);
		};
		for (int i = 0; i < 10001; i++) {
			executor.execute(insertIfAbsent);
		}
		executor.shutdown();
		executor.awaitTermination(4000, TimeUnit.SECONDS);

		// Using the wrappers for not sync collections
		// List<String> synchronizedList = Collections.synchronizedList(abcList);
		// Collections.synchronizedMap(m)
		// Collections.synchronizedXXX
	}
}
