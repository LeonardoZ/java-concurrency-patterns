# Java Concurrency Patterns and Features

Concurrency Patterns and features found in Java, through multithreaded programming.
This repository covers **Java 8 through Java 17** (with documentation stubs for Java 21 Project Loom APIs).

## Features:
* [Threads and Runnables](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/threads/UsingThreads.java)
* [Locks](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/features/locks)
  *	[Intrinsic](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/locks/UsingIntrinsicLocks.java)
  *	Explicit
	* [Reentrant](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/locks/UsingExplicitReentrantLocks.java)
	* [ReadWrite](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/locks/UsingExplicitReadWriteLocks.java) 
* [Synchronizers](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/features/synchronizers)
  *	[Latches](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/synchronizers/UsingLatches.java)
  *	[Semaphores](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/synchronizers/UsingSemaphores.java)
  *	[Barriers](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/synchronizers/UsingBarriers.java)
* [Synchronized Collections](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingSynchronizedCollections.java)
* [Concurrent Collections](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java)  
  * [CopyOnWriteArrayList](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java)
  *	[ConcurrentHashMap](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java) — now includes Java 8 bulk ops: `merge`, `compute`, `forEach`, `reduceValues`, `search`
  *	[Blocking Queue](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java)
* [Executors](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Fixed Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Cached Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Single Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Scheduled Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Single Thread Scheduled Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Work-Stealing Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  * Virtual Thread-Per-Task Executor *(Java 21 — see [`UsingVirtualThreads`](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/threads/UsingVirtualThreads.java))*
* [Atomics](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/atomics/UsingAtomics.java) — now includes **VarHandle** (Java 9)
* [Futures](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/features/futures)
  * [FutureTask](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/futures/UsingFutureTasks.java)
  * [CompletableFuture](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/futures/UsingCompletableFuture.java) — now includes `orTimeout` / `completeOnTimeout` (Java 9)
  * [Structured Concurrency](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/futures/UsingStructuredConcurrency.java) *(Java 21 — doc stub)*
* [Fork/Join Framework](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/forkjoin/UsingForkJoinFramework.java)
* [Parallel Streams](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/parallel_stream/UsingParallelStreams.java) — updated to use `Stream.toList()` (Java 16)
* [Java Memory Model](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/java_memory_model/WhatIsJavaMemoryModel.java)
* [Reactive Streams — `java.util.concurrent.Flow`](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/java_memory_model/UsingReactiveStreams.java) *(Java 9 — Publisher, Subscriber, Processor, back-pressure)*
* [Virtual Threads](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/threads/UsingVirtualThreads.java) *(Java 21 — doc stub)*
* [Scoped Values](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/threads/UsingScopedValues.java) *(Java 21 — doc stub)*

## Patterns
* [Protect Shared State](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/shared_state)
* [Atomic Compound Actions](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/compound_actions)
* [Lock Split](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/lock_split)
* [Fixed Lock Ordering](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/fixed_lock_ordering)
* [Thread Local Confinement](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/thread_confinement) — `ThreadLocal.withInitial()` (Java 8); `DateTimeFormatter` replaces `SimpleDateFormat` (Java 8)
* [Immutable Object](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/immutable_object) — inner value classes converted to **records** (Java 16); switch expression (Java 14)
* [Safe Lazy Initialization](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/thread_safe/initialization/SafeInitializationHolder.java) 
* [Safe Publishing](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/thread_safe/publishing/SafePublishing.java)
* [Resource Pool](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/resource_pool)
* [Condition Queues](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/condition_queues) 
  * [wait-notify](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/condition_queues/WaitNotifyQueue.java)
  * [await-signal](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/condition_queues/ExplicitConditionQueue.java)
* [Background Task Executor](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_execution/BackgroundTaskExecutor.java)
* [Structured Background Task Executor](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_execution/StructuredBackgroundTaskExecutor.java) *(Java 21 — doc stub)*
* [Task Cancel](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/task_cancel)
* [Producer-Consumer](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/producer_consumer/ProducerConsumer.java)
* [Virtual Thread Producer-Consumer](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/producer_consumer/VirtualThreadProducerConsumer.java) *(Java 21 — doc stub)*
* [Task Convergence](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_convergence/TaskConvergence.java)
* [Structured Task Convergence](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_convergence/StructuredTaskConvergence.java) *(Java 21 — doc stub)*
* [Non-Blocking with Atomics](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/non_blocking)
* [Controlled Concurrent Initialization](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/controlled_initialization/ControlledInitialization.java)
* [Parallel Divide and Conquer](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/divideconquer)

## Java Version Coverage

| Java Version | Feature Added / Updated |
|---|---|
| Java 8 | `ThreadLocal.withInitial()`, `ConcurrentHashMap` bulk ops (`merge`, `compute`, `forEach`, `reduceValues`, `search`) |
| Java 9 | `java.util.concurrent.Flow` (Reactive Streams), `CompletableFuture.orTimeout()` / `completeOnTimeout()`, `VarHandle` |
| Java 14 | Switch expressions (`EventKeeper`) |
| Java 16 | Records (`MyImmutableObject`, `Event`), `Stream.toList()` |
| Java 17 | Build target (compiler source/target) |
| Java 21 *(doc stubs — requires JDK upgrade)* | Virtual Threads, `Executors.newVirtualThreadPerTaskExecutor()`, Structured Concurrency (`StructuredTaskScope`), Scoped Values (`ScopedValue`) |

> **Java 21 stubs** are documentation-only files that describe the planned API
> with runnable code examples in Javadoc comments. They compile on Java 17
> (no executable code) and serve as a migration guide for when the project
> upgrades to JDK 21.

## About
Patterns and Algorithms inspired by the Java Concurrency in Practice book.

