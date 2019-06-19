package littleaj.simpoll.model;

import java.util.Objects;

public class Vote {
    private PollId pollId;
    private String answer;

    public PollId getPollId() {
        return pollId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
        return Objects.equals(pollId, vote.pollId) && Objects.equals(answer, vote.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pollId, answer);
    }

}