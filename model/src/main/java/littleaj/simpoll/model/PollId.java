package littleaj.simpoll.model;

import java.util.Objects;

public class PollId {

    private final String id;

    private PollId(String id) {
        this.id = id;
    }

    public static PollId generateNew(String question) {
        return createIdFromQuestion(question);
    }

    public static PollId generateNext(PollId id) {
        return createNextIdFromExisting(id);
    }

    private static PollId createIdFromQuestion(String question) {
        throw new UnsupportedOperationException();
    }

    private static PollId createNextIdFromExisting(PollId id) {
        throw new UnsupportedOperationException();
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

}