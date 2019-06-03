package littleaj.simpoll.model;

import java.util.Objects;
import java.util.UUID;

public class Answer {
    private UUID id;
    private String answer;

    public Answer() {
        this(null);
    }

    public Answer(String answer) {
        this.answer = answer;
    }

    public UUID getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Answer)) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) && Objects.equals(answer, answer.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }


}