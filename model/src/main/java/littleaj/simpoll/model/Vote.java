package littleaj.simpoll.model;

import java.util.Objects;

public class Vote {
    private PollId pollId;
    private int answerId;

    public PollId getPollId() {
        return pollId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
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