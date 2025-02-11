package io.github.joaoVitorLeal.libraryapi;

import io.github.joaoVitorLeal.libraryapi.models.Author;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing /* Annotation de auditoria. Para mais informações: @see Author */
public class Application {

	public static void main(String[] args) { SpringApplication.run(Application.class, args); }

}
