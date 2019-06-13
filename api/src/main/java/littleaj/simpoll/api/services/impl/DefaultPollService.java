package littleaj.simpoll.api.services.impl;

import littleaj.simpoll.api.exceptions.AnswerNotFoundException;
import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultPollService implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollStatusRepository pollStatusRepository;

    @Autowired
    private PollResultsRepository resultsRepository;

    @Autowired
    private PollIdService pollIds;

    @Override
    public List<Poll> readAll() {
        return new ArrayList<>(pollRepository.getAllPolls());
    }

    @Override
    public void create(Poll poll) {
        pollIds.applyId(poll);
        pollRepository.storePoll(poll);
    }

    @Override
    public void update(Poll poll) {
        if (poll.getId() == null) {
            throw new IllegalArgumentException("must have pollId");
        }
        if (!pollRepository.hasPollId(poll.getId())) {
            throw new PollNotFoundException();
        }
        pollRepository.storePoll(poll);
    }

    @Override
    public Poll read(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        return pollRepository.loadPoll(id);
    }

    @Override
    public void open(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        pollStatusRepository.updateStatus(id, PollStatus.OPEN);
    }

    @Override
    public void close(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        pollStatusRepository.updateStatus(id, PollStatus.CLOSED);
    }

    @Override
    public void delete(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        pollRepository.deletePoll(id);
    }

    @Override
    public PollStatus status(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        return pollStatusRepository.getStatus(id);
    }

    @Override
    public void submitVote(Vote vote) {
        if (!hasAnswerId(vote.getPollId(), vote.getAnswerId())) {
            throw new AnswerNotFoundException();
        }
        resultsRepository.incrementResult(vote.getPollId(), vote.getAnswerId());
    }

    private boolean hasAnswerId(PollId pollId, UUID answerId) {
        if (!pollRepository.hasPollId(pollId)) {
            throw new PollNotFoundException();
        }
        Poll poll = pollRepository.loadPoll(pollId);
        return poll.hasAnswerId(answerId);
    }

    @Override
    public PollResults pollResults(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        return resultsRepository.getPollResults(id);
    }
}