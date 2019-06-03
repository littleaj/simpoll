package littleaj.simpoll.model;

import java.util.Objects;
import java.util.UUID;

public class Vote {
    private PollId pollId;
    private UUID answerId;

    public PollId getPollId() {
        return pollId;
    }

    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }

    public void setPollId(PollId pollId) {
        this.pollId = pollId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Vote)) {
            return false;
        }
        Vote vote = (Vote) o;
        return Objects.equals(pollId, vote.pollId) && Objects.equals(answerId, vote.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pollId, answerId);
    }

}