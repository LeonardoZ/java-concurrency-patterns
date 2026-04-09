package br.com.leonardoz.features.atomics;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomics can be used from the java.util.concurrent.atomic.* package.
 *
 * An atomic operation is a compound action that totally completes out totally
 * fails, not supporting inconsistent values or results during it's execution.
 *
 * The classes in this package supports atomic operations on single variables,
 * having get and set (read and write) methods that behave like a volatile
 * variable.
 *
 * The compareAndSet are commonly used in non-blocking algorithms. They
 * basically tries to set a new value to a specified field, and it returns a
 * boolean indicating success or not. All atomic, only blocking briefly.
 *
 * Interesting classes in this package are: AtomicBoolean, AtomicLong,
 * AtomicReference&lt;T&gt;, AtomicMarkableReference&lt;T&gt; and
 * AtomicReferenceFieldUpdater&lt;T, V&gt;.
 *
 * == Java 9+: VarHandle ==
 *
 * {@link VarHandle} is a typed reference to a variable — an instance field, a
 * static field, or an array element.  It provides atomic access operations
 * without the object-wrapping overhead of AtomicXxx classes, and offers finer
 * control over memory ordering semantics.
 *
 * Key VarHandle access modes (ordered from weakest to strongest):
 *
 *  Plain         — ordinary read/write, no ordering guarantee.
 *  Opaque        — bit-atomicity; no cross-thread ordering.
 *  Acquire/Release — one-sided memory ordering (like C++ acquire/release).
 *  Volatile      — full sequential consistency (same as Java's volatile).
 *
 * Commonly used methods:
 *  get/set                  — plain or volatile access (depends on call site)
 *  getVolatile/setVolatile  — volatile semantics
 *  getAndAdd                — atomic fetch-and-add
 *  getAndSet                — atomic fetch-and-set
 *  compareAndSet            — CAS returning boolean
 *  compareAndExchange       — CAS returning the witness value
 *  fullFence/acquireFence/releaseFence — explicit memory barriers
 *
 * When to prefer VarHandle over AtomicXxx:
 *  - You want atomic access on a plain {@code volatile} field without boxing.
 *  - You need fine-grained memory-order control.
 *  - You are building high-performance, low-allocation data structures.
 *
 */
public class UsingAtomics {

	/*
	 * A Counter using AtomicInteger
	 */
	static class AtomicCounter {
		private AtomicInteger atomicInteger = new AtomicInteger(0);

		public void increment() {
			atomicInteger.incrementAndGet();
		}

		public void decrement() {
			atomicInteger.decrementAndGet();
		}

		public int get() {
			return atomicInteger.get();
		}
	}

	// -------------------------------------------------------------------------
	// Java 9+: same counter implemented with VarHandle
	// -------------------------------------------------------------------------

	/**
	 * A counter backed by a plain {@code volatile int} field, accessed atomically
	 * through a {@link VarHandle}.  This avoids the extra object allocation that
	 * {@link AtomicInteger} requires while providing identical thread-safety
	 * guarantees.
	 */
	static class VarHandleCounter {
		// The field must be accessible to the MethodHandles.lookup() at the
		// call site, so it is package-private rather than private.
		volatile int count = 0;

		private static final VarHandle COUNT;

		static {
			try {
				COUNT = MethodHandles.lookup()
						.findVarHandle(VarHandleCounter.class, "count", int.class);
			} catch (ReflectiveOperationException e) {
				throw new ExceptionInInitializerError(e);
			}
		}

		/** Atomically increment and return the new value. */
		public int increment() {
			// getAndAdd returns the *old* value, so we add 1 to get the new value.
			return (int) COUNT.getAndAdd(this, 1) + 1;
		}

		/** Atomically decrement and return the new value. */
		public int decrement() {
			return (int) COUNT.getAndAdd(this, -1) - 1;
		}

		/** Volatile read of the current counter value. */
		public int get() {
			return (int) COUNT.getVolatile(this);
		}

		/**
		 * Compare-and-exchange: atomically sets the field to {@code newValue}
		 * if it currently holds {@code expected}.
		 *
		 * Unlike {@code compareAndSet}, this returns the <em>witness</em> value
		 * (what was actually in the field), making it useful for retry loops.
		 */
		public int compareAndExchange(int expected, int newValue) {
			return (int) COUNT.compareAndExchange(this, expected, newValue);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// --- AtomicInteger counter ---
		var atomicCounter = new AtomicCounter();
		var cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10_000; i++) {
			cachedThreadPool.execute(() -> atomicCounter.increment());
		}
		cachedThreadPool.shutdown();
		cachedThreadPool.awaitTermination(4000, TimeUnit.SECONDS);
		System.out.println("AtomicInteger — expected 10000, got: " + atomicCounter.get());

		// --- VarHandle counter ---
		var varHandleCounter = new VarHandleCounter();
		var pool2 = Executors.newCachedThreadPool();
		for (int i = 0; i < 10_000; i++) {
			pool2.execute(() -> varHandleCounter.increment());
		}
		pool2.shutdown();
		pool2.awaitTermination(4000, TimeUnit.SECONDS);
		System.out.println("VarHandle       — expected 10000, got: " + varHandleCounter.get());

		// compare-and-exchange example
		var vh = new VarHandleCounter();
		int witness = vh.compareAndExchange(0, 42); // should succeed: witness == 0
		System.out.println("CAS witness (expected 0): " + witness + ", value is now: " + vh.get());
		witness = vh.compareAndExchange(0, 99);     // should fail: count is 42, not 0
		System.out.println("CAS witness (expected 42): " + witness + ", value is still: " + vh.get());
	}
}
