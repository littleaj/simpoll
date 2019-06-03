package littleaj.simpoll.api.repositories;

import java.util.UUID;

import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;

public interface PollResultsRepository {
    PollResults getPollResults(PollId id);
    
    void incrementResult(PollId pollId, UUID answerId);
}