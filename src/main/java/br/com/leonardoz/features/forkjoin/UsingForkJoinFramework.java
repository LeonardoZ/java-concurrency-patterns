package br.com.leonardoz.features.forkjoin;

import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * The Fork/Join Framework
 * 
 * Parallelism is the execution of multiple tasks simultaneously. Fork Join
 * Framework helps with the execution of those tasks in parallel.
 * 
 * Why?
 * 
 * The Fork/Join approach speeds up the execution of a task that can be split
 * into subtasks/small tasks, be executing them in parallel and combining those
 * results.
 * 
 * Limitations
 * 
 * One requirement for using the Fork/Join Framework is that all of the Subtasks
 * must be "completable and independent" of each other to be truly parallel, so
 * not every problem can be solved using this method. In general, the ForkJoin Framework 
 * is to be used by CPU-intensive computations, not IO bound computations, due to the 
 * long wait periods that could happen.
 * 
 * How it works
 * 
 * It uses a divide and conquer approach, dividing a major task into minor
 * subtasks recursively (Fork), until the division limit is reached and the
 * tasks can be solved, to be later combined (Join).
 * 
 * For the execution of those tasks in parallel, the framework will use a thread
 * pool, which has, be default, the same size of the number of processors
 * available for the JVM.
 * 
 * A thread from the pool has it's own double ended queue, for the matter of
 * storing all the tasks that are being executed/to be executed. The double
 * ended queue nature enables inserts or deletes to both the head and last
 * position of the queue.
 * 
 * The work-stealing algorithm is the greatest functionality for the speed up
 * aspect of the ForkJoin Framework. The algorithm balances the workload between
 * threads, allowing the threads that doesn't have any task at the moment to
 * "steal" from last position of a thread's queue that can't process his own
 * last task at the moment. In theory, there will be more task being processed.
 * 
 * Framework architecture overview
 * 
 * - ForkJoinPool: Base class for the pools, used to balance tasks that can be
 * "work-stealed".
 * 
 * - ForkJoinTask: Represents a task to be executed in a ForkJoinPool.
 * 
 * - RecursiveTask: Specialization of ForkJoinTask, holds a result.
 * 
 * - RecursiveAction: Specialization of ForkJoinTask, just process something
 * without yielding a result.
 * 
 * 
 * Workflow
 * 
 * The idea is that you can split bigger tasks into smaller ones, until that the work
 * is small enough to be completed.
 * 
 * the following algorithm describes how to use the ForkJoinFramework correctly.
 * 
 * if (my task is small enough) 
 * 
 * 	  complete my task 
 * 
 * else 
 * 	  split my task into two small tasks 
 * 
 * 	  execute both tasks and wait for the results 
 * 
 *
 * Then do your work based on the result
 * 
 * 
 */
public class UsingForkJoinFramework {

	/**
	 * Common Pool
	 * 
	 * Default instance of a fork join pool in a Java app, used by
	 * CompletableFuture, and parallel streams. All threads used by the common pool
	 * can be reused, released and reinstated after some time. This approach reduces
	 * the resource consumption. It doesn't need to be closed/shutdown.
	 * 
	 */
	public ForkJoinPool getCommonPool() {
		return ForkJoinPool.commonPool();
	}

	/**
	 * Customize ForkJoinPool
	 * 
	 * Parallelism: Parallelism level, default is Runtime#availableProcessors
	 * 
	 * ForkJoinWorkerThreadFactory: Factory used for creating threads for the pool.
	 * 
	 * UncaughtExceptionHandler: handles worker threads that terminates due some
	 * "unrecoverable" problem.
	 * 
	 * True-value AsyncMode: FIFO scheduling mode, used by tasks that are never
	 * joined, like event-oriented asynchronous tasks.
	 * 
	 */
	public ForkJoinPool customForkJoinPool(int parallelism, 
			ForkJoinPool.ForkJoinWorkerThreadFactory factory,
			UncaughtExceptionHandler handler, 
			boolean asyncMode) {
		return new ForkJoinPool(parallelism, factory, handler, asyncMode);
	}

	/**
	 * 
	 * Tasks
	 * 
	 * ForkJoinTask is the base type of a task. It represents a "lightweight
	 * thread", with the ForkJoinPool being it's scheduler.
	 * 
	 * RecursiveTask: Task that returns a value, result of a computation.
	 * 
	 * RecursiveAction: Task that doesn't returns a value.
	 * 
	 * Both can be used to implement the workflow algorithm described in the
	 * Workflow section, with he aid of Fork and Join.
	 * 
	 */

	/**
	 * RecursiveTask
	 * 
	 * Represents a result of a computation.
	 * 
	 * In the example bellow, it follows the algorithm, partitioning the numbers
	 * list in half, using fork and join to control the task flow.
	 *
	 */
	static class RecSumTask extends RecursiveTask<BigInteger> {

		private static final long serialVersionUID = 1L;
		public static final int DIVIDE_AT = 500;

		private List<Integer> numbers;

		public RecSumTask(List<Integer> numbers) {
			this.numbers = numbers;
		}

		@Override
		protected BigInteger compute() {
			var subTasks = new LinkedList<RecSumTask>();
			if (numbers.size() < DIVIDE_AT) {
				// directly
				var subSum = BigInteger.ZERO;
				for (Integer number : numbers) {
					subSum = subSum.add(BigInteger.valueOf(number));
				}
				return subSum;
			} else {
				// Divide to conquer
				var size = numbers.size();
				var numbersLeft = numbers.subList(0, size / 2);
				var numbersRight = numbers.subList(size / 2, size);

				var recSumLeft = new RecSumTask(numbersLeft);
				var recSumRight = new RecSumTask(numbersRight);

				subTasks.add(recSumRight);
				subTasks.add(recSumLeft);

				// Fork Child Tasks
				recSumLeft.fork();
				recSumRight.fork();
			}

			var sum = BigInteger.ZERO;
			for (var recSum : subTasks) {
				// Join Child Tasks
				sum = sum.add(recSum.join());
			}
			return sum;
		}
	}

	public static void main(String[] args) {
		// prepares dataset for the example
		var numbers = new LinkedList<Integer>();
		for (int i = 0; i < 500_000; i++) {
			numbers.add(i);
		}

		// Usage
		var commonPool = ForkJoinPool.commonPool();
		var task = new RecSumTask(numbers);
		BigInteger result = commonPool.invoke(task);
		System.out.println("Result is: " + result);
		System.out.println("\n\n");
	}

	/**
	 * RecursiveTask
	 * 
	 * Represents a result of a computation, resembles RecursiveTask, but without
	 * the return value.
	 *
	 */
	static class ARecursiveAction extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		@Override
		protected void compute() {
			// same pattern goes here
		}

	}

	/**
	 * It's possible to extract informations about the pool's current state.
	 * 
	 * Active thread count: Number of threads that are stealing or executing tasks.
	 * 
	 * Pool size: Number of worker threads that are started but not terminated yet.
	 * 
	 * Parallelism level: Equivalent to the number of available processors.
	 * 
	 * Queue submitted tasks: Number of submitted tasks, but not executing. Steal
	 * count:
	 * 
	 * Number of stealed tasks from a thread to another, useful for monitoring.
	 * 
	 */
	public static void debugPool(ForkJoinPool commonPool) {
		System.out.println("Debuggin ForJoinPool");
		System.out.println("Active Thread Count: " + commonPool.getActiveThreadCount());
		System.out.println("Pool Size: " + commonPool.getPoolSize());
		System.out.println("Parallelism level: " + commonPool.getParallelism());
		System.out.println("Queue submitted tasks: " + commonPool.getQueuedSubmissionCount());
		System.out.println("Steal count: " + commonPool.getStealCount());
		System.out.println("\n");
	}

}
