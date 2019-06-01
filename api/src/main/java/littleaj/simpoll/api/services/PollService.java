package littleaj.simpoll.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.model.Poll;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollStatusRepository pollStatusRepository;

    @Autowired
    private PollResultsRepository resultsRepository;

    public List<Poll> readAllPolls() {
        return pollRepository.getAllPolls().stream().collect(Collectors.toList());
    }
}