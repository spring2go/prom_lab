package io.spring2go.promdemo.instrument;

import java.util.LinkedList;
import java.util.Queue;

public class WorkerManager extends Thread {
	
	private Queue<Worker> workers = new LinkedList<Worker>();
	
	private JobQueue queue;
	
	private int minWorkers;
	private int maxWorkers;
	
	private int jobsWorkerRatio;
	
	public WorkerManager(JobQueue queue, int minWorkers, int maxWorkers, int jobsWorkerRatio) {
		this.queue = queue;
		this.minWorkers = minWorkers;
		this.maxWorkers = maxWorkers;
		this.jobsWorkerRatio = jobsWorkerRatio;
		
		// Initialize workerpool
		for (int i = 0; i < minWorkers; i++) {
			this.addWorker();
		}
	}
	
	public void addWorker() {
		Worker worker = new Worker(queue);
		this.workers.offer(worker);
		worker.start();
	}
	
	public void shutdownWorker() {
		if (this.workers.size() > 0) {
			Worker worker = this.workers.poll();
			worker.shutdown();
		}
	}
	
	public void run() {
		this.scaleWorkers();
	}
	
	public void scaleWorkers() {
		while(true) {
			int queueSize = this.queue.size();
			int workerCount = this.workers.size();
			
			if ((workerCount + 1) * jobsWorkerRatio < queueSize && workerCount < this.maxWorkers) {
				System.out.println("[WorkerManager] Too much work, starting extra worker.");
				this.addWorker();
			}
			
			if ((workerCount - 1) * jobsWorkerRatio > queueSize && workerCount > this.minWorkers) {
				System.out.println("[WorkerManager] Too much workers, shutting down 1 worker");
				this.shutdownWorker();
			}
			
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
