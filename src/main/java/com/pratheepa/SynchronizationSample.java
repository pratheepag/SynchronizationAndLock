package com.pratheepa;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SynchronizationSample{
	
	Integer count = 0;

	 void increment() {
		 synchronized (this) {
			 this.count = this.count + 1;
		 }
	}

	public static void main(String[] args) throws Exception {
		SynchronizationSample sync = new SynchronizationSample();
		sync.runExecutable();
		
		
	}
	
	public void runExecutable() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		IntStream.range(0, 10000)
		    .forEach(i -> executor.submit(this::increment));
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		executor.shutdown();//stop(executor);

		System.out.println(this.count);  // 9965
	}


}
