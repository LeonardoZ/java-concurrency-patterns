package br.com.leonardoz.patterns.task_convergence;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Pattern: Task Convergence
 * 
 * Motivations: Executing tasks may require a bit of synchronization after they
 * end.
 * 
 * Intent: Establish a mechanism to converge all tasks at a single point using
 * CyclicBarriers.
 * 
 * Applicability: When you need to identify if a set of running tasks are done.
 * 
 */
public class TaskConvergence {

	private static final int BOUND = 150_000;
	private static final int ITERS = 400_000;
	private static final int CORES = Runtime.getRuntime().availableProcessors();

	private CyclicBarrier barrier;
	private List<Long> synchronizedLinkedList;
	private ExecutorService executor;

	private Runnable run = () -> {
		var random = new Random();
		var results = new LinkedList<Long>();
		for (int i = 0; i < ITERS; i++) {
			var next = (long) random.nextInt(BOUND);
			results.add(next);
		}
		try {
			persist(results);
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	};

	private Runnable onComplete = () -> {
		System.out.println("=== Random Number Results ===");
		System.out.println("CPU Cores: " + CORES);
		System.out.println("Random Bound: " + BOUND);
		System.out.println("Iterations per Core: " + ITERS);
		System.out.println("Total Iterations: " + ITERS * CORES);
		System.out.println("Size: " + synchronizedLinkedList.size());
		System.out.println("Sum " + synchronizedLinkedList.stream().mapToLong(Long::longValue).sum());

	};

	public TaskConvergence() {
		barrier = new CyclicBarrier(CORES, onComplete);
		synchronizedLinkedList = Collections.synchronizedList(new LinkedList<>());
		executor = Executors.newFixedThreadPool(CORES);
	}

	public void run() {
		for (int i = 0; i < CORES; i++) {
			executor.execute(run);
		}
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
			executor.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void persist(List<Long> randomNumbers) {
		synchronizedLinkedList.addAll(randomNumbers);
	}

	public static void main(String[] args) {
		new TaskConvergence().run();
	}
}
