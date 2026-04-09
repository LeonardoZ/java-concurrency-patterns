package br.com.leonardoz.patterns.task_convergence;

/**
 * Pattern: Structured Task Convergence (Java 21)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>Motivation</h2>
 *
 * {@link TaskConvergence} demonstrates convergence using a {@code CyclicBarrier}:
 * each worker calls {@code barrier.await()} and all threads block until every
 * participant arrives.  This approach has several drawbacks:
 * <ul>
 *   <li>Manual error handling — a single failure can leave other threads
 *       blocked at the barrier indefinitely (broken barrier).</li>
 *   <li>No automatic cancellation — if one worker throws, the others keep
 *       running.</li>
 *   <li>Shutdown boilerplate — separate {@code ExecutorService} shutdown
 *       and {@code awaitTermination} calls.</li>
 * </ul>
 *
 * Structured concurrency ({@code StructuredTaskScope}) solves all of these
 * problems.
 *
 * <h2>Implementation Sketch (Java 21)</h2>
 *
 * <pre>{@code
 * public class StructuredTaskConvergence {
 *
 *     record WorkerResult(int workerId, List<Long> data) {}
 *
 *     WorkerResult doWork(int workerId) {
 *         var rng = new Random();
 *         var results = new ArrayList<Long>();
 *         for (int i = 0; i < 400_000; i++) {
 *             results.add((long) rng.nextInt(150_000));
 *         }
 *         return new WorkerResult(workerId, results);
 *     }
 *
 *     void run() throws Exception {
 *         int cores = Runtime.getRuntime().availableProcessors();
 *
 *         // ShutdownOnFailure: if any worker fails, cancel all others.
 *         try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
 *
 *             var subtasks = IntStream.range(0, cores)
 *                 .mapToObj(id -> scope.fork(() -> doWork(id)))
 *                 .toList();
 *
 *             scope.join()           // wait for ALL workers to finish
 *                  .throwIfFailed(); // re-throw first failure (if any)
 *
 *             // All workers succeeded — aggregate results
 *             long total = subtasks.stream()
 *                 .map(Subtask::get)
 *                 .flatMap(r -> r.data().stream())
 *                 .mapToLong(Long::longValue)
 *                 .sum();
 *
 *             System.out.println("Converged. Total sum: " + total);
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Key Improvements over CyclicBarrier</h2>
 *
 * <ul>
 *   <li>Automatic cancellation — one failure stops all other workers.</li>
 *   <li>Exception propagates cleanly to the caller via
 *       {@code throwIfFailed()}.</li>
 *   <li>No barrier counter to configure — just fork and join.</li>
 *   <li>Integrates naturally with virtual threads (each fork spawns a virtual
 *       thread by default).</li>
 * </ul>
 *
 * @see TaskConvergence
 * @see br.com.leonardoz.features.futures.UsingStructuredConcurrency
 */
public class StructuredTaskConvergence {
	// Placeholder — add executable implementation once the project is upgraded to Java 21.
}
