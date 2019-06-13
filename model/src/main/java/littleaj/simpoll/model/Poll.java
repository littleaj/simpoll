package littleaj.simpoll.model;

import java.util.*;

public class Poll {

    private PollId id;
    private String name;
    private String question;
    private Map<UUID, Answer> answers;

    public Poll() {
        answers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public PollId getId() {
        return this.id;
    }

    public void setId(PollId id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void addAnswer(String answer) {
        Answer a = new Answer(answer);
        a.setId(UUID.randomUUID());
        this.answers.put(a.getId(), a);
    }

    public Answer removeAnswer(UUID id) {
        return this.answers.remove(id);
    }

    public Answer getAnswer(UUID id) {
        return this.answers.get(id);
    }

    public boolean hasAnswerId(UUID id) {
        return this.answers.containsKey(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Poll)) {
            return false;
        }
        Poll poll = (Poll) o;
        return Objects.equals(id, poll.id) && Objects.equals(question, poll.question) && Objects.equals(answers, poll.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answers);
    }

}