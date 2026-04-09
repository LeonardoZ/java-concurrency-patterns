package br.com.leonardoz.patterns.producer_consumer;

/**
 * Pattern: Virtual Thread Producer-Consumer (Java 21)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>Motivation</h2>
 *
 * The classic {@link ProducerConsumer} pattern uses a bounded
 * {@code BlockingQueue} shared between platform threads.  The number of
 * concurrent producers and consumers is constrained by the thread-pool size,
 * because each blocking {@code put()}/{@code take()} call ties up an OS thread.
 *
 * Virtual threads remove this constraint: a blocked virtual thread is
 * <em>unmounted</em> from its carrier thread while waiting, freeing the
 * carrier for other work.  You can therefore spawn one virtual thread per
 * producer and one per consumer without exhausting OS resources.
 *
 * <h2>Implementation Sketch (Java 21)</h2>
 *
 * <pre>{@code
 * public class VirtualThreadProducerConsumer {
 *
 *     private static final int CAPACITY = 1_000;
 *     private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(CAPACITY);
 *
 *     // Each producer runs in its own virtual thread; blocking put() is cheap.
 *     private final Callable<Void> producer = () -> {
 *         for (int i = 0; i < 90_000; i++) {
 *             queue.put(UUID.randomUUID().toString());
 *         }
 *         return null;
 *     };
 *
 *     // Each consumer runs in its own virtual thread; blocking take() is cheap.
 *     private final Callable<Void> consumer = () -> {
 *         while (true) {
 *             String item = queue.poll(5, TimeUnit.SECONDS);
 *             if (item == null) break;
 *             System.out.println("Consumed " + item + " on " + Thread.currentThread());
 *         }
 *         return null;
 *     };
 *
 *     public void run(int numProducers, int numConsumers) throws InterruptedException {
 *         // newVirtualThreadPerTaskExecutor() — one virtual thread per task
 *         try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
 *             IntStream.range(0, numProducers).forEach(i -> executor.submit(producer));
 *             IntStream.range(0, numConsumers).forEach(i -> executor.submit(consumer));
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Advantages over the Platform-Thread Version</h2>
 *
 * <ul>
 *   <li>No artificial ceiling on producer/consumer count — scale to thousands
 *       of concurrent producers and consumers.</li>
 *   <li>Simpler code — no need to tune thread-pool sizes; just submit one
 *       virtual thread per logical task.</li>
 *   <li>Lower memory footprint per thread compared to platform threads.</li>
 * </ul>
 *
 * @see ProducerConsumer
 * @see br.com.leonardoz.features.threads.UsingVirtualThreads
 */
public class VirtualThreadProducerConsumer {
	// Placeholder — add executable implementation once the project is upgraded to Java 21.
}
