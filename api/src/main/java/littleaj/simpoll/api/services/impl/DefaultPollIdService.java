package littleaj.simpoll.api.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import littleaj.simpoll.api.config.SimpollConfigurationProperties.PollIdServiceConfiguration;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;

@Service
public class DefaultPollIdService implements PollIdService {
    
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollIdServiceConfiguration config;
    // private final PollIdServiceConfiguration config;

    // @Autowired
    // public DefaultPollIdService(PollIdServiceConfiguration config) {
    //     this.config = config;
    // }

    @Override
    public void applyId(Poll poll) {
        PollId id = generateNextId(poll.getName());
        poll.setId(id);
    }

    // TODO fix edge case: largest number in maxlen
    public PollId generateNextId(String name) {
        // 1. hyphenate & truncate
        String trimmed = StringUtils.strip(name);
        StringBuilder result = new StringBuilder();
        boolean skipHyphen = false;
        for (int i = 0; i < trimmed.length() && result.length() < config.getMaxLength(); i++) {
            // only alphanumeric characters '-' and '_' are allowed, whitespace replaced with hyphen, all others removed
            String c = trimmed.substring(i, i+1);
            assert c.length() == 1 : "not pulling one character";
            
            if (StringUtils.isWhitespace(c) && !skipHyphen) {
                result.append('-');
                skipHyphen = true;
            }
            if (StringUtils.isAlphanumeric(c) || "-".equals(c)) {
                result.append(c);
                skipHyphen = false;
            }
        }
        // 2. truncate
        // 3. verify available
        // 4. numerate
        PollId candidateId = new PollId(result.toString());
        while (pollRepository.hasPollId(candidateId)) {
            // increment poll id suffix; start with 2
            String suffix = StringUtils.substringAfterLast(result.toString(), "-");
            int number = 2; // increment below brings this to 2
            int appendIndex = StringUtils.lastIndexOf(result, "-");
            if (StringUtils.isNumeric(suffix)) {
                number = Integer.parseInt(suffix) + 1;
            } else {
                appendIndex = result.length();
            }
            String newSuffix = String.valueOf(number);
            // TODO verify this math; might be off by 1
            while(appendIndex + newSuffix.length() + 1 > config.getMaxLength()) {
                appendIndex--;
            }
            result.replace(appendIndex, appendIndex + newSuffix.length() + 1, "-" + newSuffix);
            candidateId = new PollId(result.toString());
        }

        return candidateId;
    }
}