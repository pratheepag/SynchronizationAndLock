package com.pratheepa;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class ReadWriteLock {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
		    long stamp = lock.writeLock();
		    try {
		        Thread.sleep(1);
		        map.put("foo", "bar");
		    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
		        lock.unlockWrite(stamp);
		    }
		});

		Runnable readTask = () -> {
		    long stamp = lock.readLock();
		    try {
		        System.out.println(map.get("foo"));
		        Thread.sleep(1);
		    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
		        lock.unlockRead(stamp);
		    }
		};

		executor.submit(readTask);
		executor.submit(readTask);

		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		executor.shutdown();
	}
}
