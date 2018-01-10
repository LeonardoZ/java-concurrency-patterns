package br.com.leonardoz.patterns.non_blocking;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Pattern: Non-blocking Design
 * 
 * Motivations: Blocking algorithms are inefficiently under certain loads and are
 * susceptible to failures like liveness. Algorithms are called non-blocking if
 * failure or suspension of any thread cannot cause the suspension or failure of
 * another thread; or if in each step it can make progress in some thread.
 * 
 * Intent: Non-blocking algorithms actually can block briefly, by limiting the
 * scope of atomic changes to a single variable while maintaining data
 * consistency.
 *
 * Applicability: Performance dependent operations where blocking designs can be
 * a problem.
 * 
 */
public class NonBlockingDesign {

	private AtomicReference<Object> value = new AtomicReference<>(new Object());

	public void modifyValue() {
		Object updatedValue;
		Object old;
		do {
			old = value.get(); // get value
			updatedValue = new Object(); // new updated value
		} while (!value.compareAndSet(old, updatedValue));
		/*
		 * if the expected value (old) differs from what is actually present in the
		 * structure, this means that in the mean time some thread changed it. The loop
		 * will run until it reaches an acceptable state.
		 */
	}
	
	public Object getValue() {
		return value.get();
	}

	/*
	 * Modified value...
	 */
	public Object newObject() {
		return new Object();
	}
}
