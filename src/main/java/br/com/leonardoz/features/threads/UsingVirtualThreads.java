package br.com.leonardoz.features.threads;

/**
 * Virtual Threads — {@code java.lang.Thread.ofVirtual()} (Java 21)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>What are Virtual Threads?</h2>
 *
 * Virtual threads are lightweight threads managed entirely by the JVM (not
 * the OS).  They are cheap to create — millions can coexist in a single JVM
 * process — because they are not pinned to OS threads.  Instead they are
 * multiplexed onto a small pool of OS ("carrier") threads by the scheduler.
 *
 * <h2>When to Use Virtual Threads</h2>
 *
 * <ul>
 *   <li>I/O-bound tasks: HTTP calls, JDBC queries, file I/O — anything that
 *       blocks waiting for external resources.</li>
 *   <li>High-concurrency servers: handle tens of thousands of simultaneous
 *       requests without a matching thread-pool size.</li>
 * </ul>
 *
 * Virtual threads are <em>NOT</em> a replacement for parallel streams or the
 * Fork/Join framework for CPU-bound computation.
 *
 * <h2>Key API (Java 21)</h2>
 *
 * <pre>{@code
 * // 1. Create and start a single virtual thread
 * Thread vt = Thread.ofVirtual()
 *                   .name("my-vthread")
 *                   .start(() -> System.out.println("Hello from " + Thread.currentThread()));
 *
 * // 2. Virtual thread-per-task executor — the primary production use case.
 * //    Creates a new virtual thread for every submitted task.
 * //    try-with-resources shuts down and waits for all tasks automatically.
 * try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
 *     IntStream.range(0, 10_000).forEach(i ->
 *         executor.submit(() -> {
 *             // Blocking I/O — the virtual thread is unmounted while waiting,
 *             // freeing the carrier thread for other work.
 *             Thread.sleep(Duration.ofMillis(100));
 *             return i;
 *         }));
 * } // auto-close: awaits termination of all submitted tasks
 *
 * // 3. Detect whether the current thread is virtual
 * boolean isVirtual = Thread.currentThread().isVirtual();
 *
 * // 4. ThreadFactory for virtual threads
 * ThreadFactory factory = Thread.ofVirtual().name("worker-", 0).factory();
 * }</pre>
 *
 * <h2>Carrier Threads and Pinning</h2>
 *
 * A virtual thread runs on a carrier (platform) thread.  When the virtual
 * thread performs a blocking operation (sleep, I/O, {@code Lock.lock()}) it is
 * <em>unmounted</em> from the carrier — the carrier is then free to run
 * another virtual thread.  The virtual thread is re-mounted when it is ready
 * to continue.
 *
 * <p><strong>Pinning</strong> occurs when a virtual thread cannot be unmounted.
 * This happens when:
 * <ul>
 *   <li>The virtual thread holds a {@code synchronized} monitor while
 *       blocking.</li>
 *   <li>It calls a native method or foreign function.</li>
 * </ul>
 * Pinning does not cause correctness problems but degrades throughput.  Prefer
 * {@code ReentrantLock} over {@code synchronized} in hot paths used with
 * virtual threads.
 *
 * <h2>ThreadLocal Caution</h2>
 *
 * {@link ThreadLocal} works with virtual threads but can use significant memory
 * if each of millions of virtual threads holds a large value.  Prefer
 * {@code ScopedValue} (Java 21) for read-only per-request context propagation.
 *
 * @see UsingThreads
 */
public class UsingVirtualThreads {
	// Placeholder — add executable examples once the project is upgraded to Java 21.
}
