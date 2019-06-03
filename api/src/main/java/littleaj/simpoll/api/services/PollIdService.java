package littleaj.simpoll.api.services;

import org.springframework.stereotype.Service;

import littleaj.simpoll.model.PollId;

@Service
public interface PollIdService {
    PollId generateNextId(String question);
}