package com.pratheepa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SemaphoreSample {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Semaphore semaphore = new Semaphore(5);
		
		Runnable longRunningTask = () -> {
		    boolean permit = false;
		    try {
		        permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
		        if (permit) {
		            System.out.println("Semaphore acquired");
		            Thread.sleep(50);
		        } else {
		            System.out.println("Could not acquire semaphore");
		        }
		    } catch (InterruptedException e) {
		        throw new IllegalStateException(e);
		    } finally {
		        if (permit) {
		            semaphore.release();
		        }
		    }
		};
		
		IntStream.range(0, 10)
		    .forEach(i -> executor.submit(longRunningTask));
		
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		executor.shutdown();

	}

}
