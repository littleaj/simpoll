package littleaj.simpoll.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Poll {

    private PollId id;
    private String question;
    private List<String> answers;


    private Poll() {
        answers = new ArrayList<>();
    }

    public PollId getId() {
        return this.id;
    }

    private void setId(PollId id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    private void setQuestion(String question) {
        this.question = question;
    }

    private void addAnswer(String answer) {
        this.answers.add(answer);
    }

    private String removeAnswer(int index) {
        return this.answers.remove(index);
    }

    public String getAnswer(int index) {
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

    public static class Builder {
        private Poll product;
        private Set<ValidationRule> validationRules;

        private Builder() {
            product = new Poll();
            validationRules = new HashSet<>();
        }

        public Builder question(String question) {
            // TODO validate input
            product.setQuestion(question);
            return this;
        }

        public Builder id(PollId id) {
            product.setId(id);
            return this;
        }

        public Builder answer(String answer) {
            product.addAnswer(answer);
            return this;
        }
    }

    private static interface ValidationRule {
        boolean validate(Poll poll);
        String getFailureReason();
    }

    private static class QuestionIsSet implements ValidationRule {
        
        @Override
        public String getFailureReason() {
            return "question cannot be null/empty/blank";
        }

        @Override
        public boolean validate(Poll poll) {
            return StringUtils.isNotBlank(poll.getQuestion());
        }
    }

}