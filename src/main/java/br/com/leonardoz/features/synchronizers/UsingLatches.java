package br.com.leonardoz.features.synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Latches are used to delay the progress of threads until it reach a terminal
 * state
 *
 * Most common implementation is CountDownLatch.
 * 
 * In CountDownLatch, each event adds 1. When ready, countDown() is called,
 * decrementing by counter by 1. await() method releases when counter is 0.
 * 
 * Single use synchronizer.
 */
public class UsingLatches {

	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();
		var latch = new CountDownLatch(3);
		Runnable r = () -> {
			try {
				Thread.sleep(1000);
				System.out.println("Service in " + Thread.currentThread().getName() + " initialized.");
				latch.countDown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		executor.execute(r);
		executor.execute(r);
		executor.execute(r);
		try {
			latch.await(2, TimeUnit.SECONDS);
			System.out.println("All services up and running!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

}
