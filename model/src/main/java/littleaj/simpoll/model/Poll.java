package littleaj.simpoll.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Poll {

    private PollId id;
    private String name;
    private String question;
    private List<String> answers;
    private Status status;

    public Poll() {
        answers = new ArrayList<>();
        status = Status.CREATED;
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

    public List<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public void setAnswers(Collection<String> answers) {
        this.answers.clear();
        this.answers.addAll(answers);
    }

    public String removeAnswer(int index) {
        return this.answers.remove(index);
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }

    public String getAnswer(int index) {
        return this.answers.get(index);
    }

    public boolean hasAnswerId(int index) {
        return index >= 0 && index < this.answers.size();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}