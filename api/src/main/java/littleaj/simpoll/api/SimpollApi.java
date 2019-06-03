package littleaj.simpoll.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import littleaj.simpoll.api.config.SimpollConfigurationProperties;
import littleaj.simpoll.api.repositories.inmem.InMemoryPollRepository;

@SpringBootApplication
public class SimpollApi {

    private final InMemoryPollRepository repository;

    public SimpollApi() {
        repository = new InMemoryPollRepository();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpollApi.class, args);
    }

    @Autowired
    SimpollConfigurationProperties config;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("version = "+config.getVersion());
        };
    }

    @Bean
    public InMemoryPollRepository allInOnePollRepository(ApplicationContext ctx) {
        return repository;
    }

}