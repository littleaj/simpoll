package littleaj.simpoll.model;

import java.util.ArrayList;
import java.util.List;

public class PollsList {
    private List<Poll> polls;

    public PollsList() {
        this(new ArrayList<>());
    }

    public PollsList(List<Poll> polls) {
        this.polls = polls;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls);
    }

}