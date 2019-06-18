package littleaj.simpoll.api.repositories.inmem;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryPollRepository implements PollRepository, PollResultsRepository, PollStatusRepository {
    private Map<PollId, Poll> pollsStore;
    private Map<PollId, PollResults> resultsStore;
    private Map<PollId, Status> statusStore;

    public InMemoryPollRepository() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public InMemoryPollRepository(Map<PollId, Poll> pollsStore, Map<PollId, PollResults> resultsStore, Map<PollId, Status> statusStore) {
        this.pollsStore = pollsStore;
        this.resultsStore = resultsStore;
        this.statusStore = statusStore;
    }

    InMemoryPollRepository(Collection<Poll> polls, Collection<PollResults> results, Collection<Status> statuses) {
        this(
                polls.stream().collect(Collectors.toMap(Poll::getId, Function.identity())),
                results.stream().collect(Collectors.toMap(PollResults::getPollId, Function.identity())),
                null);
//        statuses.stream().collect(Collectors.toMap())
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
    public Status getStatus(PollId id) {
        return statusStore.get(id);
    }

    /**
     * @throws PollNotFoundException
     */
    @Override
    public void updateStatus(PollId id, Status status) {
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
    public void incrementResult(PollId pollId, int answerId) {
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