package littleaj.simpoll.api.repositories.mysql;

import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.Result;
import littleaj.simpoll.model.Status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class MySqlPollRepository implements PollRepository {
    private final RowMapper<Poll> pollRowMapper = (rs, rowNum) -> {
        Poll p = new Poll();
        p.setId(new PollId(rs.getString("id")));
        p.setName(rs.getString("name"));
        p.setQuestion(rs.getString("question"));
        p.setStatus(Status.valueOf(rs.getString("status")));
        String[] answers = rs.getString("poll_answers").split("\n");
        p.setAnswers(Arrays.asList(answers));
        return p;
    };
    private final JdbcTemplate db;

    public MySqlPollRepository(JdbcTemplate db) {
        this.db = db;
    }

    @Override
    public Collection<Poll> getAllPolls() {
        return db.query("SELECT `id`, `name`, `question`, `status`, GROUP_CONCAT(`answers`.`answer` SEPARATOR '\n') AS `poll_answers` FROM `polls` JOIN `answers` ON `polls`.`id`=`answers`.`poll_id` GROUP BY `polls`.`id`;",
                pollRowMapper);
    }

    @Override
    public boolean hasPollId(PollId id) {
        List<PollId> result = db.query("SELECT `id` FROM `polls` WHERE `id`=?", new Object[]{id.toString()},
                (rs, rowNum) -> new PollId(rs.getString("id")));
        return !result.isEmpty();
    }

    @Override
    public void storePoll(Poll poll) {
        db.update("INSERT INTO `polls` ( `id`, `name`, `question`, `status` ) VALUES ( ?, ?, ?, ? ) " +
                "ON DUPLICATE KEY UPDATE `name`=VALUES(`name`), `question`=VALUES(`question`), `status`=VALUES(`status`)",
                poll.getId().toString(), poll.getName(), poll.getQuestion(), poll.getStatus().toString());

        for (String answer : poll.getAnswers()) {
            db.update("INSERT IGNORE INTO `answers` ( `poll_id`, `answer` ) VALUES ( ?, ? )",
                    poll.getId().toString(), answer);
        }
        // FIXME doesn't allow removing of answers
    }

    @Override
    public Poll loadPoll(PollId id) {
        final List<Poll> polls = db.query("SELECT `id`, `name`, `question`, `status`, GROUP_CONCAT(`answers`.`answer` SEPARATOR '\n" +
                        "') AS `poll_answers` \n" +
                        "FROM `polls` JOIN `answers` ON `polls`.`id`=`answers`.`poll_id`\n" +
                        "WHERE `polls`.`id`=?\n" +
                        "GROUP BY `polls`.`id`",
                new Object[]{id.toString()}, pollRowMapper);

        if (polls.isEmpty()) {
            return null;
        }
        // assume the db won't return multiple because of schema constraints
        return polls.get(0);
    }

    @Override
    public void deletePoll(PollId id) {
        int rows = db.update("DELETE FROM `polls` WHERE `id`=?", id.toString());
    }

    @Override
    public PollResults getPollResults(PollId id) {
        final List<Result> resultList = db.query("SELECT `answer`,`vote_count` FROM `results` WHERE `poll_id`=?",
                new Object[]{id.toString()}, (rs, rowNum) -> {
                    Result r = new Result();
                    r.setVoteCount(rs.getInt("vote_count"));
                    r.setAnswer(rs.getString("answer"));
                    return r;
                });
        final PollResults pollResults = new PollResults(id);
        resultList.forEach(pollResults::putResult);
        return pollResults;
    }

    @Override
    public void incrementResult(PollId pollId, String answer) {
        db.update("INSERT INTO `results` ( `poll_id`, `answer`, `vote_count` ) VALUES ( ?, ?, 1 ) ON DUPLICATE KEY UPDATE `vote_count` = `vote_count` + 1",
                pollId.toString(), answer);
    }

    @Override
    public void updateStatus(PollId id, Status status) {
        System.out.println("updating status for "+id+": "+status);
        int rows = db.update("UPDATE `polls` SET `status`=? WHERE `id`=?", status.toString(), id.toString());
        System.out.println(rows);
    }
}
