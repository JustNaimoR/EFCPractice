package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.repositories.TemporaryLinksPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class TemporaryLinksPairService {
    private final ShortenLinkIdSequenceService shortenLinkIdSequenceService;
    private final TemporaryLinksPairRepository temporaryLinksPairRepository;



    public String register(RegisterLinkDto dto) {
        TemporaryLinksPair temporaryLinksPair = (TemporaryLinksPair) dto.fromDto();
        String shortenLink = shortenLinkIdSequenceService.getNextShortenLink();

        temporaryLinksPair.setShortLink(shortenLink);
        temporaryLinksPair.getAllies().forEach(ally -> ally.setLinksPair(temporaryLinksPair));

        temporaryLinksPairRepository.save(temporaryLinksPair);

        return shortenLink;
    }

    public List<TemporaryLinksPair> getAll() {
        return temporaryLinksPairRepository.findAll();
    }

    public boolean linkIsTemporary(String shortLink) {
        return temporaryLinksPairRepository.existsByShortLink(shortLink);
    }

    public boolean linkIsExpired(String shortLink) {
        Optional<TemporaryLinksPair> opt = temporaryLinksPairRepository.findByShortLink(shortLink);

        TemporaryLinksPair linksPair = opt.orElseThrow(
                () -> new LinksPairNotFoundException("Pair of links with shortLink = '" + shortLink + "' wasn't found")
        );

        return Instant.now().isAfter(linksPair.getExpiredIn());
    }
}