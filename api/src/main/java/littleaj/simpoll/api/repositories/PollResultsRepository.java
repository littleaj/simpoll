package littleaj.simpoll.api.repositories;

import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;

public interface PollResultsRepository {
    PollResults getPollResults(PollId id);
    
    void incrementResult(PollId pollId, int answerId);
}