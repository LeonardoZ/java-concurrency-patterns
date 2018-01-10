package br.com.leonardoz.patterns.thread_safe.lock_split;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import br.com.leonardoz.patterns.GuardedBy;
import br.com.leonardoz.patterns.ThreadSafe;

/**
 * Pattern: Lock Split
 * 
 * Example A shared OS tool and command configuration example. A class that will
 * be shared through several threads to be filled.
 * 
 */
@ThreadSafe
public class OSToolConfiguration {

	@GuardedBy("lockExecutorCommands")
	private List<String> executorCommands;

	@GuardedBy("lockToolName")
	private String toolName;

	@GuardedBy("lockUser")
	private String user;

	@GuardedBy("lockPassword")
	private String password;

	private Lock lockExecutorCommands = new ReentrantLock();
	private Lock lockToolName = new ReentrantLock();
	private Lock lockUser = new ReentrantLock();
	private Lock lockPassword = new ReentrantLock();

	public List<String> getExecutorCommands() {
		lockExecutorCommands.lock();
		try {
			return executorCommands;
		} finally {
			lockExecutorCommands.unlock();
		}
	}

	public void addExecutorCommands(String executorCommand) {
		lockExecutorCommands.lock();
		try {
			this.executorCommands.add(executorCommand);
		} finally {
			lockExecutorCommands.unlock();
		}
	}

	public void removeExecutorCommands(String executorCommand) {
		lockExecutorCommands.lock();
		try {
			this.executorCommands.remove(executorCommand);
		} finally {
			lockExecutorCommands.unlock();
		}
	}

	public String getToolName() {
		lockToolName.lock();
		try {
			return toolName;
		} finally {
			lockToolName.unlock();
		}
	}

	public void setToolName(String toolName) {
		lockToolName.lock();
		try {
			this.toolName = toolName;
		} finally {
			lockToolName.unlock();
		}
	}

	public String getUser() {
		lockUser.lock();
		try {
			return user;
		} finally {
			lockUser.unlock();
		}
	}

	public void setUser(String user) {
		lockUser.lock();
		try {
			this.user = user;
		} finally {
			lockUser.unlock();
		}
	}

	public String getPassword() {
		lockPassword.lock();
		try {
			return password;
		} finally {
			lockPassword.unlock();
		}
	}

	public void setPassword(String password) {
		lockPassword.lock();
		try {
			this.password = password;
		} finally {
			lockPassword.unlock();
		}
	}

}
