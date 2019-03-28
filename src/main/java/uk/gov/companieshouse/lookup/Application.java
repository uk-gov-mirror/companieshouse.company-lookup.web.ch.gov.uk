package uk.gov.companieshouse.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application {

	public static final String APPLICATION_NAME_SPACE = "company-lookup.web.ch.gov.uk";


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
