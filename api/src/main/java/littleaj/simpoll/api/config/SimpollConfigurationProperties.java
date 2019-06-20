package littleaj.simpoll.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "simpoll")
@Validated
public class SimpollConfigurationProperties {

    @NotBlank
    private String version;

    private final PollIdServiceConfiguration pollId = new PollIdServiceConfiguration();

    public String getVersion() {
        return version;
    }

    @Bean
    public PollIdServiceConfiguration getPollId() {
        return pollId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class PollIdServiceConfiguration {
    
        @Min(16)
        private int maxLength = 64;
    
        public int getMaxLength() {
            return maxLength;
        }
    
        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }
    }

}