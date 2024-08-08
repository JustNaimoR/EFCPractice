package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.exceptions.TempLinkExpiredException;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinksPairService {
    private final ShortenLinkIdSequenceService shortenLinkIdSequenceService;
    private final LinksPairRepository linksPairRepository;
    private final TemporaryLinksPairService temporaryLinksPairService;
    private final LinkAlliesService linkAlliesService;

    @Transactional
    public RedirectView linkAllyAbbreviation(String ally) {
        LinksPair linksPair = linkAlliesService.getLinksPairByAlly(ally);
        String shortLink = linksPair.getShortLink();

        if (temporaryLinksPairService.linkIsTemporary(shortLink)) {
            if (temporaryLinksPairService.linkIsExpired(shortLink))
                throw new TempLinkExpiredException("Short link = '" + shortLink + "' is expired");
        }

        String srcLink = linksPair.getSrcLink();

        return new RedirectView(srcLink);
    }

    @Transactional
    public RedirectView linksAbbreviation(String shortLink) {
        if (temporaryLinksPairService.linkIsTemporary(shortLink)) {
            if (temporaryLinksPairService.linkIsExpired(shortLink))
                throw new TempLinkExpiredException("Short link = '" + shortLink + "' is expired");
        }

        LinksPair linksPair = linksPairRepository.findByShortLink(shortLink).orElseThrow(
                () -> new LinksPairNotFoundException("Pair of links with shortLink = '" + shortLink + "' wasn't found")
        );

        String srcLink = linksPair.getSrcLink();

        return new RedirectView(srcLink);
    }


    @Transactional
    public String register(RegisterLinkDto dto) {
        LinksPair linksPair = dto.fromDto();
        String shortenLink = shortenLinkIdSequenceService.getNextShortenLink();

        linksPair.setShortLink(shortenLink);
        linksPair.getAllies().forEach(ally -> ally.setLinksPair(linksPair));

        linksPairRepository.save(linksPair);

        return shortenLink;
    }

    public List<LinksPair> getAll() {
        return linksPairRepository.findAll();
    }

    @Transactional
    public void remove(String srcLink) {
        Optional<LinksPair> opt = linksPairRepository.findBySrcLink(srcLink);

        if (opt.isEmpty())
            throw new LinksPairNotFoundException("Pair of links with srcLink = '" + srcLink + "' wasn't found");

        linksPairRepository.delete(opt.get());
    }
}