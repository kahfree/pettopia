package a2.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan("a2.model")
@ComponentScan({"a2.controller","a2.service","a2.Helper"})
@EnableJpaRepositories("a2.repository")
public class AssignmentTwoStarterApplication implements CommandLineRunner, WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentTwoStarterApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

    }

}
