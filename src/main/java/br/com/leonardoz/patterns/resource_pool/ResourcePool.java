package br.com.leonardoz.patterns.resource_pool;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Pattern: Resource Pool
 * 
 * Motivations: Some resources can be limited, and it's import to ensures those
 * limits in concurrent programming.
 * 
 * Intent: Establish a mechanism for limiting the resource use. It'll block the
 * user when there's none available. It Implements the concurrent thread-safe
 * pool using Semaphores.
 * 
 * Applicability: Use when you want to create a pool of some limited resource.
 *
 */
public class ResourcePool<T> {

	private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	private Semaphore semaphore;
	private BlockingQueue<T> resources;

	public ResourcePool(int poolSize, List<T> initializedResources) {
		this.semaphore = new Semaphore(poolSize, true);
		this.resources = new LinkedBlockingQueue<>(poolSize);
		this.resources.addAll(initializedResources);
	}

	public T get() throws InterruptedException {
		return get(Integer.MAX_VALUE);
	}

	public T get(long secondsToTimeout) throws InterruptedException {
		semaphore.acquire();
		try {
			T resource = resources.poll(secondsToTimeout, TIME_UNIT);
			return resource;
		} finally {
			semaphore.release();
		}
	}

	public void release(T resource) throws InterruptedException {
		if (resource != null) {
			resources.put(resource);
			semaphore.release();
		}
	}

}
