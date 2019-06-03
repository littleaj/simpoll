package littleaj.simpoll.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PollResults {
    private PollId pollId;
    private int resultsId;
    private Map<UUID, Result> results;
    
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
        return new HashSet<>(results.values());
    }

    public void setResults(Set<Result> results) {
        Map<UUID, Result> newResults = new HashMap<>();
        results.stream().forEach((Result t) -> {
            newResults.put(t.getAnswer().getId(), t);
        });
        this.results = newResults;
    }

    public Result getResult(UUID answerId) {
        return results.get(answerId);
    }

    public void putResult(Result result) {
        results.put(result.getAnswer().getId(), result);
    }

}