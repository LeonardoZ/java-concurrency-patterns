package br.com.leonardoz.features.futures;

/**
 * Structured Concurrency — {@code java.util.concurrent.StructuredTaskScope}
 * (Java 21 preview → graduated in later releases)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>What is Structured Concurrency?</h2>
 *
 * Structured concurrency treats a group of related concurrent tasks as a
 * single unit of work with a well-defined lifetime.  A parent scope cannot
 * exit until all its child tasks have completed (or been cancelled).  This
 * eliminates the "fire-and-forget" leaks and the complex error-handling
 * boilerplate that arise with unstructured use of {@code CompletableFuture} or
 * {@code ExecutorService}.
 *
 * <h2>Key principle</h2>
 *
 * <em>Tasks that are forked together, complete together.</em>
 *
 * <h2>API Overview (Java 21)</h2>
 *
 * {@code StructuredTaskScope} is an {@code AutoCloseable}; use it inside a
 * try-with-resources block.
 *
 * <h3>ShutdownOnFailure — all-or-nothing</h3>
 *
 * If any subtask fails, all remaining subtasks are cancelled immediately.
 * The parent re-throws the first failure after joining.
 *
 * <pre>{@code
 * record UserSummary(String userName, int orderCount) {}
 *
 * UserSummary fetchUserSummary(long userId) throws Exception {
 *     try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
 *
 *         StructuredTaskScope.Subtask<String>  user   = scope.fork(() -> fetchUserName(userId));
 *         StructuredTaskScope.Subtask<Integer> orders = scope.fork(() -> fetchOrderCount(userId));
 *
 *         scope.join()           // waits for both tasks
 *              .throwIfFailed(); // re-throws if either failed
 *
 *         return new UserSummary(user.get(), orders.get());
 *     }
 * }
 * }</pre>
 *
 * <h3>ShutdownOnSuccess — return the fastest result</h3>
 *
 * Returns as soon as the first subtask succeeds; cancels all others.
 * Useful for fan-out/fan-in or primary/fallback service calls.
 *
 * <pre>{@code
 * String callFastest() throws Exception {
 *     try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
 *
 *         scope.fork(() -> callService("primary"));
 *         scope.fork(() -> callService("fallback"));
 *
 *         scope.join();
 *         return scope.result(); // result of the first successful subtask
 *     }
 * }
 * }</pre>
 *
 * <h2>Advantages over CompletableFuture / ExecutorService</h2>
 *
 * <ul>
 *   <li>Automatic cancellation — no manual {@code Future.cancel()} chains.</li>
 *   <li>Guaranteed cleanup — the scope closes only when all children are done.</li>
 *   <li>Cleaner error propagation — exceptions bubble up naturally.</li>
 *   <li>Integrates with {@code ScopedValue} for transparent context
 *       propagation to child tasks.</li>
 * </ul>
 *
 * <h2>Relationship to Virtual Threads</h2>
 *
 * Structured concurrency is designed to be used with virtual threads.
 * Each {@code scope.fork()} call typically spawns a new virtual thread
 * (the default factory), keeping the concurrency overhead negligible.
 *
 * @see UsingVirtualThreads
 */
public class UsingStructuredConcurrency {
	// Placeholder — add executable examples once the project is upgraded to Java 21.
}
