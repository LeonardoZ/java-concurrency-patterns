package br.com.leonardoz.features.synchronizers;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphores controls the number of activities that can access a resource or
 * perform a certain action;
 *
 * - First you give a number of 'permits';
 * 
 * - Activities will acquire it and release when they're done;
 * 
 * - If none is available, activity will block until one become available.
 *
 * Good for resource pools.
 *
 */
public class UsingSemaphores {
	public static void main(String[] args) {
		
		var executor = Executors.newCachedThreadPool();
		var semaphore = new Semaphore(3);

		Runnable r = () -> {
			try {
				System.out.println("Trying to acquire - " + Thread.currentThread().getName());
				if (semaphore.tryAcquire(2, TimeUnit.SECONDS)) {
					// use-get resource
					// simulate work in progress
					System.out.println("Acquired - " + Thread.currentThread().getName());
					Thread.sleep(2000);
					System.out.println("Done - " + Thread.currentThread().getName());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaphore.release();
			}
		};
		for (int i = 0; i < 4; i++) {
			executor.execute(r);
		}
		
		executor.shutdown();

	}
}
