package littleaj.simpoll.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import littleaj.simpoll.api.model.PollsList;
import littleaj.simpoll.api.services.PollIdService;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;
import littleaj.simpoll.model.PollId;

@RestController
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private PollService service;

    @Autowired
    private PollIdService ids;

    @GetMapping
    public ResponseEntity<PollsList> index() {
        return ResponseEntity.ok(new PollsList(service.readAllPolls()));
    }

    // @PostMapping
    // public ResponseEntity<Poll> create(@RequestBody Poll poll) {
    //     // 1. generate id
    //     PollId id = ids.generateNextId(poll.getQuestion());
    //     // 2. store
    //     return null;
    // }

}