package br.com.leonardoz.patterns.non_blocking;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pattern: Non-blocking Design
 * 
 * Example: Double Counter
 * 
 */
public class DoubleCounter {

	private AtomicInteger value = new AtomicInteger(0);

	public void increase() {
		int updatedValue;
		int old;
		do {
			old = value.get(); // get value
			updatedValue = old + 2; // new updated value
		} while (!value.compareAndSet(old, updatedValue));
		/*
		 * if the expected value (old) differs from what is actually present in the
		 * structure, this means that in the mean time some thread changed it. The loop
		 * will run again until it reaches an acceptable state.
		 */
	}
	
	public int getValue() {
		return value.get();
	}
	
}
