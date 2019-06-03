package littleaj.simpoll.api.services;

import java.util.List;

import littleaj.simpoll.model.Poll;

public interface PollService {
    List<Poll> readAllPolls();

    void create(Poll poll);
}