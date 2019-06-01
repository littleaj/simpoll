package littleaj.simpoll.api.repositories.inmem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.PollStatus;
import littleaj.simpoll.model.Result;

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

    @Override
    public void updateStatus(PollId id, PollStatus status) throws PollNotFoundException {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        statusStore.put(id, status);
    }

    @Override
    public PollResults getPollResults(PollId id) throws PollNotFoundException {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        return resultsStore.get(id);
    }

    @Override
    public PollResults submitResult(PollId id, Result result) throws PollNotFoundException {
        if (!pollsStore.containsKey(id)) {
            throw new PollNotFoundException();
        }
        PollResults results = resultsStore.get(id);
        if (results == null) {
            results = new PollResults(id);
            resultsStore.put(id, results);
        }
        results.addResult(result);
        return results;
    }

}