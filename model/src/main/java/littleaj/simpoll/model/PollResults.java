package littleaj.simpoll.model;

import java.util.HashSet;
import java.util.Set;

public class PollResults {
    private PollId pollId;
    private int resultsId;
    private Set<Result> results;
    
    public PollResults(PollId pollId) {
        this(pollId, 0);
    }

    public PollResults(PollId pollId, int resultsId) {
        setPollId(pollId);
        setResultsId(resultsId);
        setResults(new HashSet<>());
    }

    public int getResultsId() {
        return resultsId;
    }

    public void setResultsId(int resultsId) {
        this.resultsId = resultsId;
    }
    
    public PollId getPollId() {
        return pollId;
    }

    public void setPollId(PollId pollId) {
        this.pollId = pollId;
    }


    public Set<Result> getResults() {
        return new HashSet<>(results);
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }

    public void addResult(Result result) {
        this.results.add(result);
    }
}