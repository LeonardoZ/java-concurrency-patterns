package br.com.leonardoz.patterns.resource_pool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Pattern: Resource Pool
 * 
 * Example: Resource Pool usage
 */
public class ResourcePoolUsage {
	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();
		var pool = new ResourcePool<Integer>(15,
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10, 11, 12, 13, 14));
		var random = new Random();
		for (int i = 0; i < 30; i++) {
			executor.execute(() -> {
				try {
					var value = pool.get(60);
					System.out.println("Value taken: " + value);
					Thread.sleep(random.nextInt(5000));
					pool.release(value);
					System.out.println("Value released " + value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		executor.shutdown();
	}
}
