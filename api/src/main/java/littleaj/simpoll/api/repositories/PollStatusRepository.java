package littleaj.simpoll.api.repositories;

import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.Status;

public interface PollStatusRepository {

    void updateStatus(PollId id, Status status);
}