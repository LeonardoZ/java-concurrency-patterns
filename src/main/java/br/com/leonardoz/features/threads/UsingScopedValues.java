package br.com.leonardoz.features.threads;

/**
 * Scoped Values — {@code java.lang.ScopedValue} (Java 21 preview → graduated
 * in later releases)
 *
 * <p><strong>NOTE: This file requires Java 21+ to compile and run.</strong>
 * It is provided as a reference for the planned upgrade. The current build
 * targets Java 17.
 *
 * <h2>What are Scoped Values?</h2>
 *
 * A {@code ScopedValue} is an immutable, per-thread variable that is bound for
 * a bounded <em>scope</em> (a {@code run()} or {@code call()} block).  It is
 * the recommended replacement for {@link ThreadLocal} when working with virtual
 * threads or structured concurrency.
 *
 * <h2>Comparison with ThreadLocal</h2>
 *
 * <table>
 *   <tr><th>Feature</th><th>ThreadLocal</th><th>ScopedValue</th></tr>
 *   <tr><td>Mutability</td><td>Mutable (set/remove)</td>
 *       <td>Immutable (bound once per scope)</td></tr>
 *   <tr><td>Cleanup</td><td>Must call remove(); leaks if forgotten</td>
 *       <td>Automatic — cleaned up when scope exits</td></tr>
 *   <tr><td>Virtual thread memory</td><td>Each thread holds its own copy</td>
 *       <td>Shared read-only; no per-thread copy</td></tr>
 *   <tr><td>Child task visibility</td><td>Manual InheritableThreadLocal</td>
 *       <td>Automatically inherited by StructuredTaskScope children</td></tr>
 * </table>
 *
 * <h2>API Overview (Java 21)</h2>
 *
 * <pre>{@code
 * // 1. Declare — typically a static final field on a class
 * static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();
 *
 * // 2. Bind and run — value visible only within the lambda/block
 * ScopedValue.where(CURRENT_USER, "alice").run(() -> {
 *     System.out.println("User: " + CURRENT_USER.get()); // "alice"
 *     processRequest();   // value is visible anywhere in this call tree
 * });
 * // CURRENT_USER.get() here would throw NoSuchElementException
 *
 * // 3. Nested re-binding — child sees "bob"; parent still sees "alice"
 * ScopedValue.where(CURRENT_USER, "alice").run(() ->
 *     ScopedValue.where(CURRENT_USER, "bob").run(() ->
 *         System.out.println(CURRENT_USER.get())  // "bob"
 *     )
 * );
 *
 * // 4. Returning a value from a scoped computation
 * String result = ScopedValue.where(CURRENT_USER, "charlie")
 *                            .call(() -> "hello, " + CURRENT_USER.get());
 *
 * // 5. Integration with StructuredTaskScope
 * //    Child tasks forked inside a scope automatically inherit the binding.
 * ScopedValue.where(CURRENT_USER, "dave").run(() -> {
 *     try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
 *         scope.fork(() -> auditLog(CURRENT_USER.get()));  // sees "dave"
 *         scope.fork(() -> sendEmail(CURRENT_USER.get())); // sees "dave"
 *         scope.join().throwIfFailed();
 *     }
 * });
 * }</pre>
 *
 * <h2>When to Use</h2>
 *
 * <ul>
 *   <li>Propagating per-request context (user identity, trace ID, locale) to
 *       all callees without passing it as a parameter.</li>
 *   <li>Any use of {@link ThreadLocal} that involves virtual threads or
 *       structured concurrency.</li>
 * </ul>
 *
 * @see UsingVirtualThreads
 * @see UsingStructuredConcurrency
 */
public class UsingScopedValues {
	// Placeholder — add executable examples once the project is upgraded to Java 21.
}
