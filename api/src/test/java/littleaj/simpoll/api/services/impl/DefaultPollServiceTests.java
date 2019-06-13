package littleaj.simpoll.api.services.impl;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.api.repositories.PollResultsRepository;
import littleaj.simpoll.api.repositories.PollStatusRepository;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPollServiceTests {
    @Mock
    private PollRepository pollRepo;

    @Mock
    private PollStatusRepository pollStatusRepo;

    @Mock
    private PollResultsRepository resutlsRepo;

    @Mock
    private PollIdService pollIdService;

    @InjectMocks
    private DefaultPollService service;

    private Poll createPoll(String id) {
        Poll poll = new Poll();
        poll.setName("my poll");
        poll.setQuestion("Is it true?");
        poll.addAnswer("yes");
        poll.addAnswer("no");
        if (id != null) {
            poll.setId(new PollId(id));
        }
        return poll;
    }

    private Poll createPoll() {
        return createPoll(null);
    }

    @Test
    public void whenCreate_ThenReturnsPollWithIdAndStoresItInRepo() {
        Poll poll = createPoll();
        service.create(poll);
        verify(pollIdService, times(1)).applyId(poll);
        verify(pollRepo, times(1)).storePoll(poll);
    }

    @Test
    public void whenUpdate_thenStoresInRepo() {
        Poll poll = createPoll("123");
        when(pollRepo.hasPollId(poll.getId())).thenReturn(true);
        service.update(poll);
        verify(pollRepo, times(1)).storePoll(poll);
    }

    @Test
    public void whenUpdateDnePollId_thenThrow() {
        Poll poll = createPoll("123");
        when(pollRepo.hasPollId(poll.getId())).thenReturn(false);
        Assert.assertThrows(PollNotFoundException.class, () -> service.update(poll));
    }

    @Test
    public void whenRead_thenReturnsPoll() {
        Poll poll = createPoll("123");
        when(pollRepo.hasPollId(poll.getId())).thenReturn(true);
        when(pollRepo.loadPoll(poll.getId())).thenReturn(poll);
        Poll actual = service.read(poll.getId());
        Assert.assertEquals(poll, actual);
    }

    @Test
    public void whenReadDnePollId_thenThrow() {
        PollId id = new PollId("123");
        when(pollRepo.hasPollId(id)).thenReturn(false);
        Assert.assertThrows(PollNotFoundException.class, () -> service.read(id));
    }
}