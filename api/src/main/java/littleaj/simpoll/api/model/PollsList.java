package littleaj.simpoll.api.model;

import java.util.ArrayList;
import java.util.List;

import littleaj.simpoll.model.Poll;

public class PollsList {
    private List<Poll> polls = new ArrayList<>();

    public PollsList(List<Poll> polls) {
        this.polls = polls;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls);
    }

}