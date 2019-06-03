package littleaj.simpoll.api.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import littleaj.simpoll.api.config.SimpollConfigurationProperties.PollIdServiceConfiguration;
import littleaj.simpoll.api.repositories.PollRepository;
import littleaj.simpoll.model.PollId;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPollIdServiceTests {

    @Spy
    private PollIdServiceConfiguration pollIdServiceConfig = new PollIdServiceConfiguration();
    @Mock
    private PollRepository pollRepo = mock(PollRepository.class);
    @InjectMocks
    private DefaultPollIdService pollIdService;
    
    @Before
    public void setup() {
        when(pollRepo.hasPollId(any(PollId.class))).thenReturn(false);
    }

    private void runSimpleTest(String input, String expected) {
        System.out.printf("'%s' => '%s'%n", input, expected);
        PollId id = pollIdService.generateNextId(input);
        assertNotNull(id);
        assertEquals(expected, id.getId());
    }

    @Test
    public void whenNewId_thenReturnInput() {
        runSimpleTest("test123", "test123");
    }

    @Test
    public void whenNewIdTooLong_thenTruncate() {
        pollIdServiceConfig.setMaxLength(8);
        runSimpleTest("university", "universi");
    }

    @Test
    public void whenIdHasSpaces_thenReplaceWithHyphens() {
        runSimpleTest("what is your favorite color", "what-is-your-favorite-color");
    }

    @Test
    public void whenIdHasNonAlphanumericChars_thenRemoveThem() {
        runSimpleTest("what is (5*4)/3?", "what-is-543");
    }

    @Test
    public void whenIdHasRepeatedWhitespace_thenOnlyUseOneHyphen() {
        runSimpleTest("hello \t world  ***  \ta/g/a/in", "hello-world-again");
    }

    @Test
    public void whenIdHasHyphens_thenPreserveHyphens() {
        runSimpleTest("preserve-hyphens--ok", "preserve-hyphens--ok");
    }

    @Test
    public void whenIdExists_thenAddNumericSuffix() {
        when(pollRepo.hasPollId(new PollId("hello-world"))).thenReturn(true);
        runSimpleTest("hello world", "hello-world-2");
    }

    @Test
    public void whenIdExistsWithNumericSuffix_thenIncrementSuffix() {
        when(pollRepo.hasPollId(new PollId("hello-world-3"))).thenReturn(true);
        runSimpleTest("hello-world-3", "hello-world-4");
    }
}