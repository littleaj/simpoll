package littleaj.simpoll.api.repositories;

import java.util.Collection;

import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;

public interface PollRepository {
    Collection<Poll> getAllPolls();

    boolean hasPollId(PollId id);

    void storePoll(Poll poll);

    Poll loadPoll(PollId id);
}