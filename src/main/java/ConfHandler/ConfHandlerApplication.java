package ConfHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConfHandlerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ConfHandlerApplication.class, args);
	}


}
