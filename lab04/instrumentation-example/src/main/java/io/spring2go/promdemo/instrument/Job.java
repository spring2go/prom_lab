package io.spring2go.promdemo.instrument;

import java.util.Random;
import java.util.UUID;

public class Job {
	
	private String id;
	
	private Random rand = new Random();
	
	public Job() {
		this.id = UUID.randomUUID().toString();
	}
	
	public void run() {
		try {
			// Run the job (5 - 15 seconds)
			Thread.sleep((5 + rand.nextInt(10)) * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
