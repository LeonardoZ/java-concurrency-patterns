package br.com.leonardoz.features.parallel_stream;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * Warning
 *
 * Parallel Streams uses the ForkJoin Pool, so be aware of using it in Java
 * EE/Jakarta EE environments!
 *
 * The Stream API allows, with methods like parallel() or parallelStream(), the
 * execution of streams's operations in parallel.
 *
 * It enables each element to be processed in parallel, having a thread for each
 * one of them, depending on the number of cores available. Like the Fork/Join
 * Framework, it has an overhead, so the speed of execution can get much better
 * in some cases, getting worse in some others.
 *
 * Streams are composed of a Source, several intermediate operations and a
 * terminal operation. Streams are executed only when a terminal operation is
 * executed, so they're lazy too. The intermediate operations respect the order
 * that you used, they're sequential. The work of each intermediate operation is
 * parallel.
 *
 * CPU intensive tasks benefits from this feature.
 *
 * Java 16+ note: {@code Stream.toList()} is the idiomatic replacement for
 * {@code .collect(Collectors.toList())}. It returns an <em>unmodifiable</em>
 * list. Use {@code Collectors.toList()} when you need a mutable result list.
 *
 */
public class UsingParallelStreams {

	public static void main(String[] args) {
		// Creating Parallel Streams from existing collection
		new ArrayList<>().parallelStream();

		// Making Stream Parallel — Java 16+: Stream.toList() instead of Collectors.toList()
		List<Boolean> primes = IntStream.rangeClosed(0, 30_000) // source
				.parallel().mapToObj(BigInteger::valueOf).map(UsingParallelStreams::isPrime) // Intermediate operations
				.toList(); // Terminal operation (Java 16+, returns unmodifiable list)
		System.out.println("Primes checked: " + primes.size());

		// Each operation run in parallel, out of order
		IntStream.rangeClosed(0, 20) // source
				.parallel().mapToObj(Integer::toString) // Intermediate operation
				.forEach(System.out::print); // Terminal operation

		System.out.println("\n");

		// Runs sequentially, in order.
		IntStream.rangeClosed(0, 20)
			.mapToObj(Integer::toString)
			.forEach(System.out::print);

		System.out.println("\n");

		dummyPerformanceCheck();
	}

	private static void dummyPerformanceCheck() {

		// Sequential Stream — Java 16+: Stream.toList()
		var start1 = System.currentTimeMillis();
		IntStream.rangeClosed(0, 50_000)
				.mapToObj(BigInteger::valueOf)
				.map(UsingParallelStreams::isPrime)
				.toList();
		var end1 = System.currentTimeMillis();
		var time1 = (end1 - start1) / 1000;
		System.out.println("Sequential: " + time1);

		// Parallel Stream — Java 16+: Stream.toList()
		var start2 = System.currentTimeMillis();
		IntStream.rangeClosed(0, 50_000)
				.parallel()
				.mapToObj(BigInteger::valueOf)
				.map(UsingParallelStreams::isPrime)
				.toList();
		var end2 = System.currentTimeMillis();
		var time2 = (end2 - start2) / 1000;
		System.out.println("Parallel: " + time2);
	}

	// thanks to linski on
	// https://stackoverflow.com/questions/15862271/java-compute-intensive-task
	public static boolean isPrime(BigInteger n) {
		var counter = BigInteger.ONE.add(BigInteger.ONE);
		var isPrime = true;
		while (counter.compareTo(n) == -1) {
			if (n.remainder(counter).compareTo(BigInteger.ZERO) == 0) {
				isPrime = false;
				break;
			}
			counter = counter.add(BigInteger.ONE);
		}
		return isPrime;
	}
}
