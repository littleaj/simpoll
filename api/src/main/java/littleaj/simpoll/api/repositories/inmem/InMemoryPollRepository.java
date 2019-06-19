package littleaj.simpoll.api.repositories.inmem;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Result;
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

    public InMemoryPollRepository() {
        this(new HashMap<>(), new HashMap<>());
    }

    public InMemoryPollRepository(Map<PollId, Poll> pollsStore, Map<PollId, PollResults> resultsStore) {
        this.pollsStore = pollsStore;
        this.resultsStore = resultsStore;
    }

    InMemoryPollRepository(Collection<Poll> polls, Collection<PollResults> results, Collection<Status> statuses) {
        this(
                polls.stream().collect(Collectors.toMap(Poll::getId, Function.identity())),
                results.stream().collect(Collectors.toMap(PollResults::getPollId, Function.identity())));
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

    /**
     * @throws PollNotFoundException
     */
    @Override
    public void updateStatus(PollId id, Status status) {
        Poll poll = pollsStore.get(id);
        if (poll == null) {
            throw new PollNotFoundException();
        }
        poll.setStatus(status);
        pollsStore.put(poll.getId(), poll);
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
    public void incrementResult(PollId pollId, String answer) {
        if (!pollsStore.containsKey(pollId)) {
            throw new PollNotFoundException();
        }
        PollResults results = resultsStore.get(pollId);
        if (results == null) {
            final Poll poll = pollsStore.get(pollId);
            results = new PollResults(pollId);
            for (String a : poll.getAnswers()) {
                Result r = new Result();
                r.setAnswer(a);
                results.putResult(r);
            }
        }
        final Result result = results.getResult(answer);
        result.setVoteCount(result.getVoteCount() + 1);
        results.putResult(result);
        resultsStore.put(pollId, results);
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
        pollsStore.remove(id);
    }

}