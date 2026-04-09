package br.com.leonardoz.features.java_memory_model;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Reactive Streams — {@code java.util.concurrent.Flow} (Java 9)
 *
 * Java 9 introduced the {@link java.util.concurrent.Flow} API, which is an
 * adaptation of the <a href="https://www.reactive-streams.org/">Reactive
 * Streams</a> specification.  It defines four interfaces that together describe
 * a push-based, back-pressure-capable asynchronous data pipeline:
 *
 * <pre>
 *  Publisher  ──publishes items──▶  Subscriber
 *                                       │
 *                         back-pressure (request(n)) ◀─┘
 * </pre>
 *
 * == Key interfaces ==
 *
 * {@link Flow.Publisher}&lt;T&gt;
 *   Produces items and delivers them to registered subscribers.
 *   subscribe(Subscriber) is the only method.
 *
 * {@link Flow.Subscriber}&lt;T&gt;
 *   Consumes items.  Four callbacks:
 *   - onSubscribe(Subscription) — called once after subscribing; use it to
 *     call request(n) and signal how many items you are ready to receive.
 *   - onNext(T item)            — called for each delivered item.
 *   - onError(Throwable)        — terminal; called when an error occurs.
 *   - onComplete()              — terminal; called when the publisher is done.
 *
 * {@link Flow.Subscription}
 *   Connects one publisher to one subscriber.
 *   - request(n)  — signals demand for n more items (back-pressure).
 *   - cancel()    — cancels the subscription.
 *
 * {@link Flow.Processor}&lt;T,R&gt;
 *   Extends both Publisher and Subscriber; acts as a pipeline stage that
 *   transforms items from type T to type R.
 *
 * == Back-pressure ==
 *
 * The subscriber controls flow by calling {@code request(n)}.  The publisher
 * must not deliver more items than requested.  This prevents fast producers
 * from overwhelming slow consumers.
 *
 * == SubmissionPublisher ==
 *
 * {@link SubmissionPublisher} is the only concrete {@code Flow.Publisher}
 * provided by the JDK.  It is buffered, auto-closes on exception, and uses
 * the common ForkJoinPool by default.
 */
public class UsingReactiveStreams {

	/**
	 * A simple subscriber that requests one item at a time (maximum
	 * back-pressure) and prints each value it receives.
	 */
	static class PrintSubscriber implements Flow.Subscriber<Integer> {

		private final String name;
		private Flow.Subscription subscription;

		PrintSubscriber(String name) {
			this.name = name;
		}

		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			this.subscription = subscription;
			// Request the first item — without this call no items are ever delivered.
			subscription.request(1);
		}

		@Override
		public void onNext(Integer item) {
			System.out.println("[" + name + "] received: " + item);
			// Request the next item only after processing the current one.
			// This is back-pressure: the publisher cannot outrun the subscriber.
			subscription.request(1);
		}

		@Override
		public void onError(Throwable throwable) {
			System.err.println("[" + name + "] error: " + throwable.getMessage());
		}

		@Override
		public void onComplete() {
			System.out.println("[" + name + "] completed.");
		}
	}

	/**
	 * A {@link Flow.Processor} that doubles every integer it receives before
	 * forwarding it downstream.  It extends {@link SubmissionPublisher} (which
	 * implements {@link Flow.Publisher}) and implements {@link Flow.Subscriber}.
	 */
	static class DoublingProcessor
			extends SubmissionPublisher<Integer>
			implements Flow.Processor<Integer, Integer> {

		private Flow.Subscription subscription;

		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			this.subscription = subscription;
			subscription.request(1);
		}

		@Override
		public void onNext(Integer item) {
			// Transform and forward to downstream subscribers.
			submit(item * 2);
			subscription.request(1);
		}

		@Override
		public void onError(Throwable throwable) {
			closeExceptionally(throwable);
		}

		@Override
		public void onComplete() {
			close();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== Flow API — Basic Publisher/Subscriber ===");

		// SubmissionPublisher is AutoCloseable; close() signals onComplete().
		try (var publisher = new SubmissionPublisher<Integer>()) {

			var subscriber = new PrintSubscriber("sub-1");
			publisher.subscribe(subscriber);

			// Publish integers 1..5
			IntStream.rangeClosed(1, 5).forEach(publisher::submit);

		} // publisher.close() is called here, triggering onComplete()

		// Give the async pipeline time to drain before the next demo.
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.println();

		// -----------------------------------------------------------------------
		System.out.println("=== Flow API — Multiple Subscribers ===");

		try (var publisher = new SubmissionPublisher<Integer>()) {
			publisher.subscribe(new PrintSubscriber("sub-A"));
			publisher.subscribe(new PrintSubscriber("sub-B"));

			IntStream.rangeClosed(1, 3).forEach(publisher::submit);
		}

		TimeUnit.MILLISECONDS.sleep(200);
		System.out.println();

		// -----------------------------------------------------------------------
		System.out.println("=== Flow API — Processor (doubling pipeline) ===");

		// Source publisher  ──▶  DoublingProcessor  ──▶  PrintSubscriber
		try (var source    = new SubmissionPublisher<Integer>();
			 var processor = new DoublingProcessor()) {

			// Wire the pipeline: source → processor → subscriber
			source.subscribe(processor);
			processor.subscribe(new PrintSubscriber("doubled"));

			IntStream.rangeClosed(1, 5).forEach(source::submit);

		} // both publishers close, propagating onComplete() through the pipeline

		TimeUnit.MILLISECONDS.sleep(200);
	}
}
