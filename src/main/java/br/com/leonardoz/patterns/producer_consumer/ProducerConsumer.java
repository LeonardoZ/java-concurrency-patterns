package br.com.leonardoz.patterns.producer_consumer;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Pattern: Producer-Consumer
 * 
 * Motivations: A common design pattern is the Producer-Consumer, where the
 * logic that produces data is decoupled from the consumer logic through
 * threads.
 * 
 * Intent: Create a simple Producer-Consumer relationship using Threads,
 * using BlockinQueue with multiple Producers/Consumers threads.
 * 
 * Applicability: Good for when "getting the data" and "consuming the data"
 * happens in a non-serialized order.
 *
 */
public class ProducerConsumer {

	private BlockingQueue<String> data = new LinkedBlockingQueue<>();

	private Callable<Void> consumer = () -> {
		while (true) {
			var dataUnit = data.poll(5, TimeUnit.SECONDS);
			if (dataUnit == null)
				break;
			System.out.println("Consumed " + dataUnit + " from " + Thread.currentThread().getName());
		}
		return null;
	};

	private Callable<Void> producer = () -> {
		for (int i = 0; i < 90_000; i++) {
			var dataUnit = UUID.randomUUID().toString();
			data.put(dataUnit);
		}
		return null;
	};

	public void run(long forHowLong, TimeUnit unit) throws InterruptedException {
		var pool = Executors.newCachedThreadPool();
		pool.submit(producer);
		pool.submit(consumer);
		pool.submit(consumer);
		pool.shutdown();
		pool.awaitTermination(forHowLong, unit);
	}

	public static void main(String[] args) {
		var producerConsumer = new ProducerConsumer();
		try {
			producerConsumer.run(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
