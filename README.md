# Java Concurrency Patterns and Features

Concurrency Patterns and features found in Java, through multithreaded programming. 

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
  * [CopyOnWriteArrayList](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java#L89)
  *	[ConcurrentHashMap](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java#L40)
  *	[Blocking Queue](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/collections/UsingConcurrentCollections.java#L141)
* [Executors](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java)
  *	[Fixed Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L94)
  *	[Cached Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L65)
  *	[Single Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L51)
  *	[Scheduled Thread Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L122)
  *	[Single Thread Scheduled Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L139)
  *	[Work-Stealing Pool](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/executors/UsingExecutors.java#L156)
* [Atomics](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/atomics/UsingAtomics.java)
* [Futures](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/features/futures)
  * [FutureTask](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/futures/UsingFutureTasks.java)
  * [CompletableFuture](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/futures/UsingCompletableFuture.java)
* [Fork/Join Framework](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/forkjoin/UsingForkJoinFramework.java)
* [Parallel Streams](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/parallel_stream/UsingParallelStreams.java)
* [Java Memory Model](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/features/java_memory_model/WhatIsJavaMemoryModel.java)

## Patterns
* [Protect Shared State](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/shared_state)
* [Atomic Compound Actions](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/compound_actions)
* [Lock Split](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/lock_split)
* [Fixed Lock Ordering](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/fixed_lock_ordering)
* [Thread Local Confinement](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/thread_confinement)
* [Immutable Object](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/thread_safe/immutable_object)
* [Safe Lazy Initialization](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/thread_safe/initialization/SafeInitializationHolder.java) 
* [Safe Publishing](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/thread_safe/publishing/SafePublishing.java)
* [Resource Pool](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/resource_pool)
* [Condition Queues](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/condition_queues) 
  * [wait-notify](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/condition_queues/WaitNotifyQueue.java)
  * [await-signal](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/condition_queues/ExplicitConditionQueue.java)
* [Background Task Executor](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_execution/BackgroundTaskExecutor.java)
* [Task Cancel](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/task_cancel)
* [Producer-Consumer](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/producer_consumer/ProducerConsumer.java)
* [Task Convergence](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/task_convergence/TaskConvergence.java)
* [Non-Blocking with Atomics](https://github.com/LeonardoZ/java-concurrency-patterns/tree/master/src/main/java/br/com/leonardoz/patterns/non_blocking)
* [Controlled Concurrent Initialization](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/controlled_initialization/ControlledInitialization.java)
* [Parallel Divide and Conquer](https://github.com/LeonardoZ/java-concurrency-patterns/blob/master/src/main/java/br/com/leonardoz/patterns/divideconquer)

## About
Patterns and Algorithms inspired by the Java Concurrency in Practice book.

	
