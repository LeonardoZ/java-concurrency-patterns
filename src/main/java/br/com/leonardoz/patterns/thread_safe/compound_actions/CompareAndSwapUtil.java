package br.com.leonardoz.patterns.thread_safe.compound_actions;

/**
 * 
 * Pattern: Atomic Compound Actions
 * 
 * Example: Simple Atomic Compound Actions using Compare-and-swap idiom
 * 
 */
public class CompareAndSwapUtil {

	private int value;

	public synchronized int getValue() {
		return value;
	}

	public synchronized int compareAndSwap(int expected, int newValue) {
		int old = value;
		if (old == expected) {
			value = newValue;
		}
		return old;
	}

}
