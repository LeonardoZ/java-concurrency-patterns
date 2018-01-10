package br.com.leonardoz.patterns.thread_safe.lock_split;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import br.com.leonardoz.patterns.GuardedBy;
import br.com.leonardoz.patterns.ThreadSafe;

/**
 * Pattern: Lock Split
 * 
 * Motivations: If you have shared, mutable, independent and hot variables, you
 * can increase performance by giving each variable or variable group it's own
 * lock.
 * 
 * Intent: If the variables or variables groups are independent in terms of
 * logic and usage, we guard their state by assigning a lock to which one of
 * then. We protect all paths that interacts with each variable or variable
 * group, creating a thread safe class that is efficiently in term of lock
 * contention and other hazards like race conditions.
 *
 * Applicability: Classes where you have shared, mutable, independent and hot
 * variables or variables groups, where one single lock will be inefficiently.
 * 
 */
@ThreadSafe
public class LockSplit {

	@GuardedBy("lockState")
	private List<Object> hotState;

	@GuardedBy("lockAnotherState")
	private Object anotherState;

	@GuardedBy("lockOtherState")
	private Object otherState;

	private Lock lockState = new ReentrantLock();
	private Lock lockOtherStates = new ReentrantLock();

	public List<Object> stateReader() {
		lockState.lock();
		try {
			return hotState;
		} finally {
			lockState.unlock();
		}
	}

	public void stateWriterMethod(Object param) {
		lockState.lock();
		try {
			this.hotState.add(param);
		} finally {
			lockState.unlock();
		}
	}

	public Object anotherStateReader() {
		lockOtherStates.lock();
		try {
			return anotherState;
		} finally {
			lockOtherStates.unlock();
		}
	}

	public void anotherStateWriterMethod(Object param) {
		lockOtherStates.lock();
		try {
			this.anotherState = param;
		} finally {
			lockOtherStates.unlock();
		}
	}

	public Object otherStateReader() {
		lockOtherStates.lock();
		try {
			return otherState;
		} finally {
			lockOtherStates.unlock();
		}
	}

	public void otherStateWriterMethod(Object param) {
		lockOtherStates.lock();
		try {
			this.otherState = param;
		} finally {
			lockOtherStates.unlock();
		}
	}

}
