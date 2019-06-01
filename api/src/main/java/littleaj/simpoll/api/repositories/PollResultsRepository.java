package littleaj.simpoll.api.repositories;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Result;

public interface PollResultsRepository {
    PollResults getPollResults(PollId id) throws PollNotFoundException;
    
    PollResults submitResult(PollId id, Result result) throws PollNotFoundException;


}