package br.com.leonardoz.features.futures;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 
 * CompletableFuture is a Future that may be manually completed. It combines a
 * Future interface with the CompletionStage interface, supporting dependent
 * actions that trigger upon its completion, similarly to a callback.
 *
 * Important: Specify an Executor for async methods when available. All async
 * methods without an explicit Executor argument are performed using the
 * ForkJoinPool.commonPool();
 *
 * Important 2: Mostly of the CompletableFuture methods returns a new
 * CompletableFuture.
 *
 * == Quick terminology guide ==
 * 
 * = Async =
 * 
 * xxxAsync(...); // Async method executed in the ForkJoinPool.commonPool();
 * 
 * xxxAsync(..., Executor executor); // Executed in the specified Executor, good
 * for Java EE.
 * 
 * 
 * = supply x run =
 * 
 * supplyAsync(Supplier<U> supplier); // will complete asynchronously by calling
 * supplier.
 * 
 * runAsync(Runnable runnable); // will complete after the runnable executions;
 * 
 * 
 * = thenApply x thenAccept x thenRun
 * 
 * thenApply: transforms a value to another type;
 * 
 * thenAccept: accepts a consumer to the result value;
 * 
 * thenRun: accepts a Runnable to be executed after the result is ready;
 * 
 * 
 * == Quick API guide ==
 * 
 * = Creating =
 *
 * new CompletableFuture<>();
 * 
 * CompletableFuture.supplyAsync(Supplier<U>supplier);
 * 
 * CompletableFuture.supplyAsync(Supplier<U> supplier, Executor executor);
 * 
 * CompletableFuture.runAsync(Runnable runnable);
 * 
 * CompletableFuture.runAsync(Runnable runnable, Executor executor);
 * 
 * 
 * = Mapping values =
 * 
 * completableFuture.thenApply(Function<? super T,? extends U> fn);
 * 
 * completableFuture.thenApplyAsync(Function<? super T,? extends U> fn);
 * 
 * completableFuture.thenApplyAsync(Function<? super T,? extends U> fn, Executor
 * executor);
 * 
 * 
 * = Callback on completion =
 * 
 * completableFuture.thenAccept(Consumer<? super T> block);
 * 
 * completableFuture.thenRun(Runnable action);
 * 
 * 
 * = Error handling =
 * 
 * completableFuture.exceptionally(ex -> ex.getMessage());
 * 
 * completableFuture.handle((value, ex) -> {if value != null... else {}})
 * 
 * 
 * = Pipeline =
 * 
 * Chain one future dependent on the other
 * 
 * completableFuture.thenCompose(Function<? super T,CompletableFuture<U>> fn);
 * // flatMap
 * 
 * 
 * = Mapping values from Two Futures =
 * 
 * completableFuture.thenCombine(CompletableFuture<? extends U> other,
 * BiFunction<? super T,? super U,? extends V> fn) ex.:
 * 
 * 
 * = Waiting for first CompletableFuture to complete =
 * 
 * Two services, one fast and the other slow. Fastest always wins.
 * 
 * completableFuture.acceptEither(CompletableFuture<? extends T> other,
 * Consumer<? super T> block);
 * 
 * 
 * = Transforming first completed =
 * 
 * completableFuture.applyToEither(CompletableFuture<? extends T> other,
 * Function<? super T,U> fn)
 * 
 * 
 * = Combining multiple CompletableFuture together =
 * 
 * CompletableFuture.allOf(CompletableFuture<?>... cfs)
 * 
 * CompletableFuture.anyOf(CompletableFuture<?>... cfs)
 * 
 * 
 * = Get-Complete value =
 * 
 * CompletableFuture.get() // block
 * 
 * CompletableFuture.complete() // complete future's lifecycle
 * 
 * CompletableFuture.obtrudeValue() // ignores complete
 * 
 * CompletableFuture.join() // same as get
 * 
 * CompletableFuture.getNow(valueIfAbsent) // immediately return
 * 
 * CompletableFuture.completeExceptionally() // completes throwing a exception
 * 
 * CompletableFuture.completeExceptionally(ex) // completes with a exception
 *
 */
public class UsingCompletableFuture {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		var random = new Random();
		var executor = Executors.newCachedThreadPool();
		// Creating
		CompletableFuture<Integer> randomNum = CompletableFuture.supplyAsync(() -> random.nextInt(140), executor);

		// Mapping
		String strNum = randomNum.thenApplyAsync(n -> Integer.toString(n), executor).get();
		System.out.println("Executed " + strNum);

		// Combining
		CompletableFuture<Integer> anotherNum = CompletableFuture.supplyAsync(() -> random.nextInt(140), executor);

		// accept both and do something
		randomNum.thenAcceptBoth(anotherNum, (num1, num2) -> {
			System.out.println("Num1 is: " + num1);
			System.out.println("Num2 is: " + num2);
		});

		// combine both into a new type/value
		CompletableFuture<Integer> mappedAndCombined = randomNum.thenCombine(anotherNum, (num1, num2) -> num1 + num2);

		// retrieving value
		Integer value = mappedAndCombined.get();
		System.out.println("Sum " + value);

		// Undefined time task
		Supplier<Double> randomDouble = () -> {
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return random.nextDouble();
		};

		// Run after executed
		CompletableFuture<Double> f1 = CompletableFuture.supplyAsync(randomDouble);
		CompletableFuture<Double> f2 = CompletableFuture.supplyAsync(randomDouble);
		CompletableFuture<Double> f3 = CompletableFuture.supplyAsync(randomDouble);
		CompletableFuture<Double> f4 = CompletableFuture.supplyAsync(randomDouble);
		CompletableFuture.anyOf(f1, f2, f3, f4).thenRun(() -> System.out.println("Completed"));

		// Fastest result will be delivered
		// Undefined time task - static value
		Supplier<String> getVal = () -> {
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "First";
		};
		Supplier<String> getVal2 = () -> {
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "Second";
		};
		CompletableFuture
			.supplyAsync(getVal)
			.acceptEitherAsync(CompletableFuture.supplyAsync(getVal2, executor), (firstToBeReady) -> System.out.println(firstToBeReady), executor);
		executor.shutdown();
		executor.awaitTermination(3000, TimeUnit.SECONDS);
	}

}
