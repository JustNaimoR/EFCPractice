package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.repositories.ShortenLinkIdSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortenLinkIdSequenceService {
    private final ShortenLinkIdSequenceRepository repository;

    public String getNextShortenLink() {
        Long id = repository.getNextSequenceValue();

        return "shorten_link_" + id;
    }

}