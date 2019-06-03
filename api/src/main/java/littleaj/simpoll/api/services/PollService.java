package littleaj.simpoll.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import littleaj.simpoll.model.Poll;

@Service
public interface PollService {
    List<Poll> readAllPolls();
}