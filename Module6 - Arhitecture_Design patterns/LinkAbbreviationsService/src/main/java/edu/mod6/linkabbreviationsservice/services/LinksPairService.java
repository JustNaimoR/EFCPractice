package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.dto.LinksPairDto;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.dto.mappes.LinksPairDtoMapper;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.exceptions.TempLinkExpiredException;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;



@Service
@RequiredArgsConstructor
public class LinksPairService {
    private final ShortenLinkIdSequenceService shortenLinkIdSequenceService;
    private final LinksPairRepository linksPairRepository;
    private final LinkAlliesService linkAlliesService;
    private final LinksPairDtoMapper linksPairDtoMapper;


    @Transactional
    public RedirectView linkAllyAbbreviation(String ally) throws TempLinkExpiredException {
        LinksPair linksPair = linkAlliesService.getLinksPairByAlly(ally);

        if (linksPair.isExpired())
            throw new TempLinkExpiredException("Short link = '" + linksPair.getShortLink() + "' is expired");

        String srcLink = linksPair.getSrcLink();

        return new RedirectView(srcLink);
    }

    @Transactional
    public RedirectView linksAbbreviation(String shortLink) throws TempLinkExpiredException {
        LinksPair linksPair = findByShortLink(shortLink);

        if (linksPair.isExpired())
            throw new TempLinkExpiredException("Short link = '" + linksPair.getShortLink() + "' is expired");

        String srcLink = linksPair.getSrcLink();

        return new RedirectView(srcLink);
    }


    @Transactional
    public String register(RegisterLinkDto dto) {
        LinksPair linksPair = dto.fromDto();
        String shortenLink = shortenLinkIdSequenceService.getNextShortenLink();

        linksPair.setShortLink(shortenLink);
        linksPair.getAllies().forEach(ally -> ally.setLinksPair(linksPair));

        save(linksPair);

        return shortenLink;
    }

    public List<LinksPair> getAll() {
        return linksPairRepository.findAll();
    }

    @Transactional
    public void remove(String srcLink) {
        LinksPair deleted = findBySrcLink(srcLink);

        linksPairRepository.removeById(deleted.getId());
    }

    @Transactional
    public void save(LinksPair linksPair) throws ValidationException {
        try {
            linksPairRepository.save(linksPair);
        } catch (DataIntegrityViolationException exc) {
            throw new ValidationException("Unique constraint violation");
        }
    }

    public LinksPair findByShortLink(String shortLink) throws LinksPairNotFoundException {
        return linksPairRepository.findByShortLink(shortLink).orElseThrow(
                () -> new LinksPairNotFoundException("Pair of links with shortLink = '" + shortLink + "' wasn't found")
        );
    }

    public LinksPair findBySrcLink(String srcLink) throws LinksPairNotFoundException {
        return linksPairRepository.findBySrcLink(srcLink).orElseThrow(
                () -> new LinksPairNotFoundException("Pair of links with srcLink = '" + srcLink + "' wasn't found")
        );
    }

    public List<LinksPairDto> getAllToDto() {
        return getAll().stream()
                .map(linksPairDtoMapper::toDto)
                .toList();
    }
}