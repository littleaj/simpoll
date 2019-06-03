package littleaj.simpoll.api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.PollStatus;
import littleaj.simpoll.model.Vote;

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
        return pollRepository.getAllPolls().stream().collect(Collectors.toList());
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
        // TODO exception if poll DNE
        pollRepository.storePoll(poll);
    }

    @Override
    public Poll read(PollId id) {
        return pollRepository.loadPoll(id);
    }

    @Override
    public void open(PollId id) {
        pollStatusRepository.updateStatus(id, PollStatus.OPEN);
    }

    @Override
    public void close(PollId id) {
        pollStatusRepository.updateStatus(id, PollStatus.CLOSED);
    }

    @Override
    public void delete(PollId id) {
        pollRepository.deletePoll(id);
    }

    @Override
    public PollStatus status(PollId id) {
        return pollStatusRepository.getStatus(id);
    }

    @Override
    public void submitVote(Vote vote) {
        // TODO validate poll is open, if not: 400, msg: poll is not open
        // TODO validate answer id exists, if not: 400, msg: answer not found
        resultsRepository.incrementResult(vote.getPollId(), vote.getAnswerId());
    }

    @Override
    public PollResults pollResults(PollId id) {
        return null;
    }
}