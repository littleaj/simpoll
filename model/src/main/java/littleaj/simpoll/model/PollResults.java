package littleaj.simpoll.model;

import java.util.HashMap;
import java.util.Map;

public class PollResults {
    private PollId pollId;
    private int resultsId;
    private Map<String, Result> results;
    
    public PollResults(PollId pollId) {
        this.pollId = pollId;
        this.resultsId = 0;
        this.results = new HashMap<>();
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

    public Map<String, Result> getResults() {
        return new HashMap<>(results);
    }

    public void setResults(Map<String, Result> results) {
        this.results.clear();
        this.results.putAll(results);
    }

    public void putResult(Result result) {
        this.results.put(result.getAnswer(), result);
    }

    public Result getResult(String answer) {
        return this.results.get(answer);
    }
}