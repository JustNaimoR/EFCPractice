package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.dto.mappers.RegisterLinkDtoMapper;
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
    private final RegisterLinkDtoMapper registerLinkDtoMapper;
    private final LinksPairRepository linksPairRepository;
    private final TemporaryLinksPairService temporaryLinksPairService;

    @Transactional()
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
        LinksPair linksPair = registerLinkDtoMapper.fromDto(dto);
        String shortenLink = shortenLinkIdSequenceService.getNextShortenLink();

        linksPair.setShortLink(shortenLink);

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