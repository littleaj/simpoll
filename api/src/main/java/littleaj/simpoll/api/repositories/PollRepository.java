package littleaj.simpoll.api.repositories;

import java.util.Collection;

import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;

public interface PollRepository {
    /**
     *
     * @return All polls in repository. Empty collection if none.
     */
    Collection<Poll> getAllPolls();


    /**
     *
     * @param id poll id
     * @return true if the id is in the repository, false otherwise.
     */
    boolean hasPollId(PollId id);

    /**
     * Adds the poll to the repository. If PollId already exists, it updates existing.
     * @param poll the poll to store
     */
    void storePoll(Poll poll);

    /**
     *
     * @param id poll id
     * @return the Poll with the given PollId. null if the poll does not exist.
     */
    Poll loadPoll(PollId id);

    /**
     * Removes the poll from the repository. Nop if poll does not exist.
     * @param id poll id
     */
    void deletePoll(PollId id);
}