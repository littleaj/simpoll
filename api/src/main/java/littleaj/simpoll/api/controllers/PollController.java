package littleaj.simpoll.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import littleaj.simpoll.api.model.PollsList;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;

@RestController
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private PollService polls;

    @GetMapping
    public ResponseEntity<PollsList> index() {
        return ResponseEntity.ok(new PollsList(polls.readAll()));
    }

    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody Poll poll) {
        polls.create(poll);
        return ResponseEntity.ok(poll);
    }

    @PutMapping
    public ResponseEntity<Poll> update(@RequestBody Poll poll) {
        polls.update(poll);
        return ResponseEntity.ok(poll);
    }

}