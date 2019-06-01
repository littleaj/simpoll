package littleaj.simpoll.api;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.repositories.inmem.InMemoryPollRepository;
import littleaj.simpoll.api.services.PollService;

@SpringBootApplication
public class SimpollApi {

    private final InMemoryPollRepository repository;

    public SimpollApi() {
        repository = new InMemoryPollRepository();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpollApi.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Inspecting Spring Boot provided beans: ");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }

    @Bean
    public PollRepository pollRepository(ApplicationContext ctx) {
        return repository;
    }

    @Bean
    public PollResultsRepository pollResultsRepository(ApplicationContext ctx) {
        return repository;
    }

    @Bean
    public PollStatusRepository pollStatusRepository(ApplicationContext ctx) {
        return repository;
    }

    @Bean
    public PollService pollService(ApplicationContext ctx) {
        return new PollService();
    }
}