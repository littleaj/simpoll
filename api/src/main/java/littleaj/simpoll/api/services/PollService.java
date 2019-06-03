package littleaj.simpoll.api.services;

import java.util.List;

import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.PollStatus;
import littleaj.simpoll.model.Vote;

public interface PollService {
    /**
     * @return all polls
     */
    List<Poll> readAll();

    /**
     * Creates a new poll. poll.getId() should be null.
     * @param poll
     */
    void create(Poll poll);

    /**
     * Updates existing poll. poll.getId() should be set to the id of the poll to update.
     * @param poll
     */
    void update(Poll poll);

    /**
     * Retrieves the poll with the given id
     * @param id
     * @return
     */
    Poll read(PollId id);

    /**
     * Opens the poll for voting.
     * @param id
     */
    void open(PollId id);

    /**
     * Closes the poll for voting.
     * @param id
     */
    void close(PollId id);

    /**
     * Deletes the poll.
     * @param id
     */
    void delete(PollId id);

    /**
     * Returns the statsu of the poll.
     * @param id
     * @return
     */
    PollStatus status(PollId id);

    /**
     * Submits a poll vote.
     */
    void submitVote(Vote vote);

    /**
     * Gets the results of a poll.
     */
    PollResults pollResults(PollId id);
}