package io.spring2go.promdemo.instrument;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;

@Controller
@SpringBootApplication
@EnablePrometheusEndpoint
public class InstrumentApplication {
	
	private JobQueue queue = new JobQueue();
	
	private WorkerManager workerManager;

	public static void main(String[] args) {

		SpringApplication.run(InstrumentApplication.class, args);
	}

	@RequestMapping(value = "/hello-world")
	public @ResponseBody String sayHello() {
		return "hello, world";
	}
	
	@RequestMapping(value = "/jobs", method = RequestMethod.POST) 
	public @ResponseBody String jobs() {
		queue.push(new Job());
		return "ok";
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
		return new CommandLineRunner() {
			public void run(String... args) throws Exception {
				// 10 jobs per worker
				workerManager = new WorkerManager(queue, 1, 4, 10);
				executor.execute(workerManager);
				System.out.println("WorkerManager thread started...");
			}
		};
	}

}
