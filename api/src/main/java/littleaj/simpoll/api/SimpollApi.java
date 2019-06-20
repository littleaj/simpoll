package littleaj.simpoll.api;

import littleaj.simpoll.api.config.SimpollConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
public class SimpollApi {

    private final SimpollConfigurationProperties config;

    public SimpollApi(SimpollConfigurationProperties config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpollApi.class, args);
    }

    @Autowired
    private Environment env;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> System.out.println("version = "+config.getVersion());
    }

    @Bean
    public DataSource mysqlDatasource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.user"));
        ds.setPassword(env.getProperty("dp.pw"));
        return ds;
    }

}