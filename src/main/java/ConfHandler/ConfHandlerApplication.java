package ConfHandler;

import ConfHandler.Admin.EmailService;
import ConfHandler.model.entity.Participant;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ConfHandlerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ConfHandlerApplication.class, args);
	}


}
