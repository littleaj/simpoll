package littleaj.simpoll.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Poll {

    private PollId id;
    private String name;
    private String question;
    private List<Answer> answers;

    public Poll() {
        answers = new ArrayList<>();
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
        this.answers.add(new Answer(answer));
    }

    public Answer removeAnswer(int index) {
        return this.answers.remove(index);
    }

    public Answer getAnswer(int index) {
        return this.answers.get(index);
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