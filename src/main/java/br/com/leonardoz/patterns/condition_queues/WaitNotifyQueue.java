package br.com.leonardoz.patterns.condition_queues;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Pattern: Wait/Notify Queue
 * 
 * Motivations: State dependent classes can be difficult to implement, mainly
 * because some precondition states can become true through another thread.
 * Condition Queues help us to identify the condition predicates and do
 * control to programming flux associated with it.
 * 
 * Intent: Create a Wait/Notify mechanism based on the capabilities of each java
 * object to be a condition queue itself.
 * 
 * Applicability: State dependent algorithms used in concurrent programming.
 *
 */
public class WaitNotifyQueue {

	private boolean continueToNotify;
	private BlockingQueue<String> messages;

	public WaitNotifyQueue(List<String> messages) {
		this.messages = new LinkedBlockingQueue<>(messages);
		this.continueToNotify = true;
	}

	public synchronized void stopsMessaging() {
		continueToNotify = false;
		notifyAll();
	}

	public synchronized void message() throws InterruptedException {
		while (!continueToNotify)
			wait();
		var message = messages.take();
		System.out.println(message);
	}

	public static void main(String[] args) {
		var messages = new LinkedList<String>();
		for (int i = 0; i < 130; i++) {
			messages.add(UUID.randomUUID().toString());
		}
		var waitNotifyQueue = new WaitNotifyQueue(messages);
		new Thread(() -> {
			try {
				while (true) {
					waitNotifyQueue.message();
					Thread.sleep(300);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}).start();
		var random = new Random();
		new Thread(() -> {
			while (true) {
				int val = random.nextInt(100);
				System.out.println(val);
				if (val == 99) {
					break;
				}
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
			waitNotifyQueue.stopsMessaging();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}).start();

	}

}
