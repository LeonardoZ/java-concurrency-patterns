package br.com.leonardoz.patterns.thread_safe.publishing;

/**
 * Pattern: Safe Publishing
 * 
 * Motivations: If we want to share an object across threads, we must do this
 * safely. Publishing an object is making it visible to other parts of the code,
 * outside the current scope, showing the reference to it. If not made properly,
 * the published object can be in an inconsistent state due to the Java Memory
 * Model.
 * 
 * Intent: Publish an object safely, both the reference to the object and the
 * object state must be made visible to other threads at the same time. In this
 * example, we're using static fields, but AtomicReference, Volatile and Final Fields can also
 * an option.
 * 
 * Applicability: Always when you want to make an object visible to other in a
 * concurrent code execution.
 * 
 */
public class SafePublishing {

	public static Object object;

	{
		// use static field or a static block to initialize
		// static initialization is safe because it's done automatically locked.
		object = new Object();
	}

}
