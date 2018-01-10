package br.com.leonardoz.patterns.thread_safe.shared_state;

import br.com.leonardoz.patterns.GuardedBy;
import br.com.leonardoz.patterns.ThreadSafe;

/**
 * Pattern: Protected Shared State
 * 
 * Motivations: When you have a shared object passed through threads, you need
 * to protect it's internal state.
 *
 * Intent: Guard the shared mutable state of the object with a lock using the
 * synchronized mechanism. Protect all paths that interacts with the object
 * state, creating a thread safe class. We do this to avoid hazards like race
 * conditions.
 *
 * Applicability: Simples classes where you have a single independent mutable
 * variable(s).
 * 
 * Example: A simple counter class
 *
 */
@ThreadSafe
public class ProtectedSharedState {

	@GuardedBy("this")
	private Object state;
	
	@GuardedBy("this")
	private Object state2;

	public synchronized Object getState() {
		return state;
	}

	public synchronized void setState(Object state) {
		this.state = state;
	}

	public synchronized Object getState2() {
		return state2;
	}

	public synchronized void setState2(Object state2) {
		this.state2 = state2;
	}

}
