package br.com.leonardoz.patterns.controlled_initialization;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Pattern: Controlled Concurrent Initialization
 *
 * Motivations: Expensive resources can be initialized by different threads to
 * speed up the process. And in order to proceed with the programming logic
 * flux, it's important to ensure that the initialization has been completed.
 * 
 * Intent: Use CountDownLatch to crate a controlled concurrent initialization
 * mechanism.
 * 
 * Applicability: When you have multiple expensive resources being initialized
 * concurrently.
 * 
 */
public class ControlledInitialization {

	static class Resource1 {

	}

	static class Resource2 {

	}

	static class Resource3 {

	}

	private Resource1 resource1;
	private Resource2 resource2;
	private Resource3 resource3;
	private CountDownLatch latch = new CountDownLatch(3);

	private Runnable initResource1 = () -> {
		try {
			// simulate wait
			Thread.sleep(4000);
			resource1 = new Resource1();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};

	private Runnable initResource2 = () -> {
		try {
			// simulate wait
			Thread.sleep(4000);
			resource2 = new Resource2();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};

	private Runnable initResource3 = () -> {
		try {
			// simulate wait
			Thread.sleep(4000);
			resource3 = new Resource3();
			latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};

	public ControlledInitialization() {
		initialize();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		doTask();
	}

	private void doTask() {
		System.out.println("=== Resources Initialized ===");
		System.out.println("Resource 1 instance " + resource1);
		System.out.println("Resource 2 instance " + resource2);
		System.out.println("Resource 3 instance " + resource3);

	}

	private void initialize() {
		System.out.println("=== Initializing Resources ===");
		var executor = Executors.newFixedThreadPool(3);
		executor.execute(initResource1);
		executor.execute(initResource2);
		executor.execute(initResource3);
		executor.shutdown();

	}

	public static void main(String[] args) {
		new ControlledInitialization();
	}

}
