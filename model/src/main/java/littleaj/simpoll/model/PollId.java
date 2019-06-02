package littleaj.simpoll.model;

import java.util.Objects;

public class PollId {

    private final String id;

    public PollId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PollId)) {
            return false;
        }
        PollId pollId = (PollId) o;
        return Objects.equals(id, pollId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getId();
    }

}