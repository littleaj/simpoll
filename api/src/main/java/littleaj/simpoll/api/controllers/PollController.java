package littleaj.simpoll.api.controllers;

import littleaj.simpoll.api.exceptions.PollNotFoundException;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;
import littleaj.simpoll.model.PollResults;
import littleaj.simpoll.model.PollsList;
import littleaj.simpoll.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/polls")
public class PollController {

    private final PollService polls;

    @Autowired
    public PollController(PollService polls) {
        this.polls = polls;
    }

    @GetMapping
    public ResponseEntity<PollsList> index() {
        return ResponseEntity.ok(new PollsList(polls.readAll()));
    }

    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody Poll poll) {
        Poll createdPoll = polls.create(poll);
        final URI location;
        try {
            location = new URI("/polls/" + createdPoll.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.created(location).body(createdPoll);
    }

    @PutMapping
    public ResponseEntity<Poll> update(@RequestBody Poll poll) {
        try {
            Poll updated = polls.update(poll);
            return ResponseEntity.ok(updated);
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Poll> updateId(@PathVariable PollId id, @RequestBody Poll poll) {
        return update(poll);
    }

    @GetMapping("{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable PollId id) {
        try {
            Poll poll = polls.read(id);
            return ResponseEntity.ok(poll);
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletePoll(@PathVariable PollId id) {
        try {
            polls.delete(id);
            return ResponseEntity.ok().build();
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}/open")
    public ResponseEntity activatePoll(@PathVariable PollId id) {
        try {
            System.out.println("Opening poll: "+id);
            polls.open(id);
            Poll poll = polls.read(id);
            System.out.println("poll status = "+poll.getStatus());
            return ResponseEntity.ok().build();
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}/close")
    public ResponseEntity closePoll(@PathVariable PollId id) {
        try {
            polls.close(id);
            System.out.println("Closing poll: "+id);
            return ResponseEntity.ok().build();
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{id}/vote")
    public ResponseEntity<PollResults> voteOnPoll(@PathVariable PollId id, @RequestBody Vote vote) {
        try {
            polls.submitVote(vote);
            PollResults results = polls.pollResults(vote.getPollId());
            return ResponseEntity.ok(results);
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}/results")
    public ResponseEntity<PollResults> pollResults(@PathVariable PollId id) {
        try {
            System.out.println("Getting results for "+id);
            PollResults results = polls.pollResults(id);
            return ResponseEntity.ok(results);
        } catch (PollNotFoundException pnfe) {
            return ResponseEntity.notFound().build();
        }
    }

}