package littleaj.simpoll.api.repositories;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollStatus;

public interface PollStatusRepository {
    PollStatus getStatus(PollId id);

    void updateStatus(PollId id, PollStatus status) throws PollNotFoundException;
}