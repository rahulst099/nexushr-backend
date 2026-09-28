package nexushr_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NexushrBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexushrBackendApplication.class, args);
	}
}
