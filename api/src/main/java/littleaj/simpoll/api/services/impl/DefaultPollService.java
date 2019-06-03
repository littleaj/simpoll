package littleaj.simpoll.api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;

@Primary
public class DefaultPollService implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollStatusRepository pollStatusRepository;

    @Autowired
    private PollResultsRepository resultsRepository;

    @Override
    public List<Poll> readAllPolls() {
        return pollRepository.getAllPolls().stream().collect(Collectors.toList());
    }
}