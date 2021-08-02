package in.bitanxen.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class SpringBootReactiveWorkflowApplication {

	/*
	static {
		System.loadLibrary("attach");
		BlockHound.install();
	}
	 */

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactiveWorkflowApplication.class, args);
	}

}
