package littleaj.simpoll.api.repositories.inmem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.PollStatus;

public class InMemoryPollRepository implements PollRepository, PollResultsRepository, PollStatusRepository {
    private Map<PollId, Poll> pollsStore;
    private Map<PollId, PollResults> resultsStore;
    private Map<PollId, PollStatus> statusStore;

    public InMemoryPollRepository() {
        pollsStore = new HashMap<>();
        resultsStore = new HashMap<>();
        statusStore = new HashMap<>();
    }

    @Override
    public Collection<Poll> getAllPolls() {
        return new ArrayList<>(pollsStore.values());
    }

    @Override
    public boolean hasPollId(PollId id) {
        return pollsStore.containsKey(id);
    }

    @Override
    public void storePoll(Poll poll) {
        pollsStore.put(poll.getId(), poll);
    }

    @Override
    public Poll loadPoll(PollId id) {
        return pollsStore.get(id);
    }

    @Override
    public PollStatus getStatus(PollId id) {
        return statusStore.get(id);
    }

    /**
     * @throws PollNotFoundException
     */
    @Override
    public void updateStatus(PollId id, PollStatus status) {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        statusStore.put(id, status);
    }

    /**
     * @throws PollNotFoundException
     */
    @Override
    public PollResults getPollResults(PollId id) {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        return resultsStore.get(id);
    }

    @Override
    public void incrementResult(PollId pollId, UUID answerId) {
        if (!pollsStore.containsKey(pollId)) {
            throw new PollNotFoundException();
        }
        PollResults results = resultsStore.get(pollId);
        if (results == null) {
            results = new PollResults(pollId);
            resultsStore.put(pollId, results);
        }
        
    }

    /**
     * @throws PollNotFoundException
     */
    @Override
    public void deletePoll(PollId id) {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        resultsStore.remove(id);
        statusStore.remove(id);
        pollsStore.remove(id);
    }

}