package br.com.leonardoz.patterns.divideconquer;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/*
 * Pattern: Parallel Divide and Conquer
 * 
 * Motivations: Some kind of tasks, algorithms or flows can be divided 
 * into smaller pieces, which are independently	resolvable, making them fall 
 * into the category of divide and conquer computations. Those properties 
 * allows the execution to be parallel, possible increasing the speed if those 
 * problems were difficult enough to compensate the cost involved in parallelization.
 * 
 * Intent: Make the execution of some computation that uses the divide and conquer 
 * approach parallel, in order to increase performance if the problem difficulty is 
 * enough to overcome the overhead of using threads and coordination. It uses the ForkJoin Framework 
 * to model those problems.
 *
 * Applicability: Divide and conquer computations.
 * 
 */
public class ParallelDivideAndConquer {

	private final static int THRESHOLD = 10; // Choosing a number to split the computation
	
	
	public static class Task extends RecursiveTask<Integer> {

		private static final long serialVersionUID = 1L;
		private List<Integer> somethingToDivideAndConquer;

		public Task(List<Integer> somethingToDivideAndConquer) {
			this.somethingToDivideAndConquer = somethingToDivideAndConquer;
		
		}
		
		@Override
		protected Integer compute() {
			var size = somethingToDivideAndConquer.size();
			if (size < THRESHOLD) {
				// solves directly
				return 1;
			} else {
				// creates tasks, fork and join
				return 2;
			}
		}
		
	}
	
	
}
