package littleaj.simpoll.model;

public class Result {
    private Answer answer;
    private int voteCount;
    
    public Answer getAnswer() {
        return answer;
    }
    
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}