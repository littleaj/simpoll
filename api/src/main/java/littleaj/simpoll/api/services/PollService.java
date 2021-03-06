package littleaj.simpoll.api.services;

import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Vote;

import java.util.List;

public interface PollService {
    /**
     * @return all polls
     */
    List<Poll> readAll();

    /**
     * Creates a new poll. poll.getId() should be null.
     * @param poll the poll to create
     */
    Poll create(Poll poll);

    /**
     * Updates existing poll. poll.getId() should be set to the id of the poll to update.
     * @param poll the poll to update
     */
    Poll update(Poll poll);

    /**
     * Retrieves the poll with the given id
     * @param id poll id
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
     * Submits a poll vote.
     */
    void submitVote(Vote vote);

    /**
     * Gets the results of a poll.
     */
    PollResults pollResults(PollId id);
}