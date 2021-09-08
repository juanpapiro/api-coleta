package br.com.coleta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableSpringDataWebSupport
public class app  {
	
	
	public static void main(String[] args) {
		SpringApplication.run(app.class, args);
	}
	

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
