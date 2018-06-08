package pe.com.foxsoft.ballartelyweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BallartelyWebManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallartelyWebManagerApplication.class, args);
	}
}
