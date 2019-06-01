package littleaj.simpoll.api.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import littleaj.simpoll.api.config.SimpollConfigurationProperties.PollIdServiceConfiguration;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.model.PollId;

@Service
public class PollIdService {
    
    @Autowired
    private PollRepository pollRepository;

    private final PollIdServiceConfiguration config;

    @Autowired
    public PollIdService(PollIdServiceConfiguration config) {
        this.config = config;
    }

    public PollId generateNextId(String question) {
        // 1. hyphenate
        StringBuilder sb = new StringBuilder();
        String trimmed = StringUtils.strip(question);
        for (int i = 0; i < question.length(); i++) {
            // only alphanumeric characters '-' and '_' are allowed, whitespace replaced with hyphen, all others removed

        }
        // 2. truncate
        // 3. verify available
        // 4. numerate

        return null;
    }
}