package br.com.leonardoz.patterns.task_execution;

/**
 * Pattern: Structured Background Task Executor (Java 21)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>Motivation</h2>
 *
 * {@link BackgroundTaskExecutor} wraps an {@code ExecutorService} and returns
 * {@code Future} handles for submitted tasks.  Callers must remember to shut
 * down the executor, handle {@code InterruptedException} at every
 * {@code Future.get()} call site, and manually cancel remaining futures if one
 * fails.  This unstructured concurrency is error-prone.
 *
 * Structured concurrency ({@code StructuredTaskScope}) provides the same
 * capability with automatic cancellation, clean exception propagation, and
 * guaranteed lifetime.
 *
 * <h2>Implementation Sketch (Java 21)</h2>
 *
 * <pre>{@code
 * public class StructuredBackgroundTaskExecutor {
 *
 *     // Execute multiple tasks; fail fast if any task fails.
 *     public <T> List<T> executeAll(List<Callable<T>> tasks) throws Exception {
 *         try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
 *
 *             var subtasks = tasks.stream()
 *                     .map(scope::fork)
 *                     .toList();
 *
 *             scope.join().throwIfFailed();
 *
 *             return subtasks.stream()
 *                     .map(StructuredTaskScope.Subtask::get)
 *                     .toList();
 *         }
 *     }
 *
 *     // Execute several tasks; return the result of whichever finishes first.
 *     public <T> T executeAny(List<Callable<T>> tasks) throws Exception {
 *         try (var scope = new StructuredTaskScope.ShutdownOnSuccess<T>()) {
 *
 *             tasks.forEach(scope::fork);
 *
 *             scope.join();
 *             return scope.result();
 *         }
 *     }
 * }
 * }</pre>
 *
 * <h2>Comparison with BackgroundTaskExecutor</h2>
 *
 * <table>
 *   <tr><th></th><th>BackgroundTaskExecutor</th>
 *       <th>StructuredBackgroundTaskExecutor</th></tr>
 *   <tr><td>Cancellation on failure</td><td>Manual Future.cancel()</td>
 *       <td>Automatic (ShutdownOnFailure)</td></tr>
 *   <tr><td>Executor shutdown</td><td>Manual shutdown()/awaitTermination()</td>
 *       <td>Automatic (try-with-resources)</td></tr>
 *   <tr><td>Exception handling</td>
 *       <td>Checked at every Future.get() call site</td>
 *       <td>Single throwIfFailed() propagates the first exception</td></tr>
 *   <tr><td>Thread type</td><td>Platform threads</td>
 *       <td>Virtual threads (default factory)</td></tr>
 * </table>
 *
 * @see BackgroundTaskExecutor
 * @see br.com.leonardoz.features.futures.UsingStructuredConcurrency
 */
public class StructuredBackgroundTaskExecutor {
	// Placeholder — add executable implementation once the project is upgraded to Java 21.
}
