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
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < trimmed.length(); i++) {
            // only alphanumeric characters '-' and '_' are allowed, whitespace replaced with hyphen, all others removed
            if (result.length() > config.getMaxLength()) {
                break;
            }
            String c = trimmed.substring(i, i+1);
            assert c.length() == 1 : "not pulling one character";
            
            if (StringUtils.isWhitespace(c)) {
                result.append('-');
            }
            if (StringUtils.isAlphanumeric(c)) {
                result.append(c);
            }
        }
        // 2. truncate
        // 3. verify available
        // 4. numerate
        PollId candidateId = new PollId(result.toString());
        while (pollRepository.hasPollId(candidateId)) {
            // increment poll id suffix
        }

        return candidateId;
    }
}