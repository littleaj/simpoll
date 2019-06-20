package littleaj.simpoll.api;

import littleaj.simpoll.api.config.SimpollConfigurationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpollApi {

    private final SimpollConfigurationProperties config;

    public SimpollApi(SimpollConfigurationProperties config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpollApi.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> System.out.println("version = "+config.getVersion());
    }

}