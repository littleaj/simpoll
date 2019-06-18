package littleaj.simpoll.model;

import java.util.Objects;

public class Result {
    private int answerId;
    private String answer;
    private int voteCount;
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return answerId == result.answerId &&
                voteCount == result.voteCount &&
                Objects.equals(answer, result.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId, answer, voteCount);
    }
}