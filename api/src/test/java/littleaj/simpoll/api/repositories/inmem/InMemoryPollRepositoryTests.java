package littleaj.simpoll.api.repositories.inmem;

import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertTrue;

public class InMemoryPollRepositoryTests {
    private InMemoryPollRepository repo;

    @Before
    public void setup() {
        repo = new InMemoryPollRepository();
    }

    @After
    public void tearDown() {
        repo = null;
    }

    @Test
    public void whenStorePoll_thenPollIsInRepo() {
        Poll poll = createTestPoll("123");
        repo.storePoll(poll);
        assertTrue(repo.hasPollId(poll.getId()));
        assertThat(repo.getAllPolls(), hasItem(poll));
        Poll poll2 = createTestPoll("456");
        assertTrue(repo.hasPollId(poll.getId()));
        assertTrue(repo.hasPollId(poll2.getId()));
        assertThat(repo.getAllPolls(), hasItem(poll));
        assertThat(repo.getAllPolls(), hasItem(poll2));
    }


    @Ignore
    @Test
    public void whenRemovePoll_thenPollIsNotInRepo() {

    }



    private Poll createTestPoll(String id) {
        PollId pid = new PollId(id);
        Poll poll = new Poll();
        poll.setId(pid);
        poll.setName("PollName-"+id);
        poll.setQuestion("Question "+id);
        poll.addAnswer("Answer 1-"+id);
        poll.addAnswer("Answer 2-"+id);
        return poll;
    }
}
