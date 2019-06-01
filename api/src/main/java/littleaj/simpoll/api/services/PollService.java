package littleaj.simpoll.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.model.Poll;

@Service
public class PollService {

    @Autowired
    private PollRepository repository;

    public List<Poll> readAll() {
        return repository.listAllPolls().stream().collect(Collectors.toList());
    }
}