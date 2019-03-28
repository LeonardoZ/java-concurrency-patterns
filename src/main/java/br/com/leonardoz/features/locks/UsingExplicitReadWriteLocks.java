package br.com.leonardoz.features.locks;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 
 * Special lock with a different strategy: allow multiple readers simultaneously
 * and only one single writer.
 *
 * ReadLock readLock = rwLock.readLock();
 * 
 * WriteLock writeLock = rwLock.writeLock();
 * 
 * Read access is granted if there's no Writer or a Writer requesting access.
 * 
 * Write access is granted if there's no Reader.
 *
 * ReentrantReadWriterLock gives reentrant capabilities do ReadWriteLock
 * 
 * Fair in constructor:
 * 
 * true: Fair Lock, newly requesting threads are queued if the lock is held.
 * 
 * false: Unfair lock: if the lock is held, requesting threads can 'jump' the
 * waiting queue (default, specially for write lock).
 * 
 */
public class UsingExplicitReadWriteLocks {

	// Equivalent to Intrinsic Locks
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private String myContent = "A long default content......";

	/**
	 * Simplest way to use the read mode
	 * 
	 * @return
	 */
	public String showContent() {
		ReadLock readLock = readWriteLock.readLock();
		readLock.lock();
		try {
			System.out.println("Reading state while holding a lock.");
			return myContent;
		} finally {
			readLock.unlock();
		}
	}

	public void writeContent(String newContentToAppend) {
		WriteLock writeLock = readWriteLock.writeLock();
		writeLock.lock();
		try {
			System.err.println("Writing " + newContentToAppend);
			myContent = new StringBuilder().append(myContent).append(newContentToAppend).toString();
		} finally {
			writeLock.unlock();
		}
	}

	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();
		var self = new UsingExplicitReadWriteLocks();
		// Readers
		for (int i = 0; i < 100; i++) {
			executor.submit(() -> {
				try {
					// Delay readers to start
					Thread.sleep(new Random().nextInt(10) * 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(self.showContent());
			});
		}

		// Writers - only if no writer is available
		for (int i = 0; i < 5; i++) {
			executor.execute(() -> self.writeContent(UUID.randomUUID().toString()));
		}
		executor.shutdown();
	}

}
