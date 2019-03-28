package br.com.leonardoz.patterns.condition_queues;

import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pattern: Explicit Condition Queues
 * 
 * Motivations: State dependent classes can be difficult to implement, mainly
 * because some precondition states can become true through another thread.
 * Condition Queues help us to identify the condition predicates and do control
 * to programming flux associated with it. Also, wait/notify are based in
 * intrinsic lock mechanisms.
 * 
 * Intent: Create a Condition Queue mechanism based on the capabilities of the
 * Lock interface to generate Conditions.
 * 
 * Applicability: State dependent algorithms used in concurrent programming.
 *
 */
public class ExplicitConditionQueue {

	private static final int LIMIT = 5;
	private int messageCount = 0;
	private Lock lock = new ReentrantLock();
	private Condition limitReachedCondition = lock.newCondition();
	private Condition limitUnreachedCondition = lock.newCondition();

	public void stopMessages() throws InterruptedException {
		lock.lock();
		try {
			while (messageCount < LIMIT) {
				limitReachedCondition.await();
			}
			System.err.println("Limit reached. Wait 2s");
			Thread.sleep(2000);
			messageCount = 0;
			limitUnreachedCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void printMessages(String message) throws InterruptedException {
		lock.lock();
		try {
			while (messageCount == LIMIT) {
				limitUnreachedCondition.await();
			}
			System.out.println(message);
			messageCount++;
			limitReachedCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		var queue = new ExplicitConditionQueue();
		// Will run indefinitely
		new Thread(() -> {
			while (true) {
				var uuidMessage = UUID.randomUUID().toString();
				try {
					queue.printMessages(uuidMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(() -> {
			while (true) {
				try {
					queue.stopMessages();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
