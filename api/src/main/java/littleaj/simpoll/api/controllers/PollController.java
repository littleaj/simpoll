package littleaj.simpoll.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import littleaj.simpoll.api.model.PollsList;
import littleaj.simpoll.api.services.PollService;
import littleaj.simpoll.model.Poll;

@RestController
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private PollService service;

    @GetMapping
    public ResponseEntity<PollsList> index() {
        return ResponseEntity.ok(new PollsList(service.readAllPolls()));
    }

}