package br.com.leonardoz.patterns.thread_safe.thread_confinement;

/**
 * Pattern: Thread Local Confinement
 * 
 * Motivation: Dealing with non thread-safe objects is a common situation, and
 * it's possible to still have a thread-safe code even using non thread-safe
 * classes.
 * 
 * Intent: Use ThreadLocal to confine instances in a per-thread model, keeping a
 * object copy to each thread.
 * 
 * Applicability: Use for non thread-safe objects that needs to be shared across threads.
 *
 */
public class ThreadLocalConfinement {

	private static final ThreadLocal<Object> threadLocalOject = new ThreadLocal<Object>() {
		@Override
		protected Object initialValue() {
			return new Object();
		}
	};	
	
	public Object getNowThreadSafeObjectInstance() {
		return threadLocalOject.get();
	}
	
}
