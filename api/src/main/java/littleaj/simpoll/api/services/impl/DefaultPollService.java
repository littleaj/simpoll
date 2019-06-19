package littleaj.simpoll.api.services.impl;

import littleaj.simpoll.api.exceptions.AnswerNotFoundException;
import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Status;
import littleaj.simpoll.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultPollService implements PollService {

    private final PollRepository pollRepository;

    private final PollStatusRepository pollStatusRepository;

    private final PollResultsRepository resultsRepository;

    private final PollIdService pollIds;

    @Autowired
    public DefaultPollService(PollRepository pollRepository, PollStatusRepository pollStatusRepository,
                              PollResultsRepository resultsRepository, PollIdService pollIds) {
        this.pollRepository = pollRepository;
        this.pollStatusRepository = pollStatusRepository;
        this.resultsRepository = resultsRepository;
        this.pollIds = pollIds;
    }

    @Override
    public List<Poll> readAll() {
        return new ArrayList<>(pollRepository.getAllPolls());
    }

    @Override
    public Poll create(Poll poll) {
        pollIds.applyId(poll);
        pollRepository.storePoll(poll);
        return poll;
    }

    @Override
    public Poll update(Poll poll) {
        if (poll.getId() == null) {
            throw new IllegalArgumentException("must have pollId");
        }
        if (!pollRepository.hasPollId(poll.getId())) {
            throw new PollNotFoundException();
        }
        pollRepository.storePoll(poll);
        return poll;
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
        pollStatusRepository.updateStatus(id, Status.OPEN);
    }

    @Override
    public void close(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        pollStatusRepository.updateStatus(id, Status.CLOSED);
    }

    @Override
    public void delete(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        pollRepository.deletePoll(id);
    }

    @Override
    public void submitVote(Vote vote) {
        if (!hasAnswer(vote.getPollId(), vote.getAnswer())) {
            throw new AnswerNotFoundException();
        }
        resultsRepository.incrementResult(vote.getPollId(), vote.getAnswer());
    }

    private boolean hasAnswer(PollId pollId, String answer) {
        if (!pollRepository.hasPollId(pollId)) {
            throw new PollNotFoundException();
        }
        Poll poll = pollRepository.loadPoll(pollId);
        return poll.hasAnswer(answer);
    }

    @Override
    public PollResults pollResults(PollId id) {
        if (!pollRepository.hasPollId(id)) {
            throw new PollNotFoundException();
        }
        return resultsRepository.getPollResults(id);
    }
}