package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.entities.LinkAllies;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.AllyNotFoundException;
import edu.mod6.linkabbreviationsservice.repositories.LinkAlliesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;



@Service
@RequiredArgsConstructor
public class LinkAlliesService {
    private final LinkAlliesRepository linkAlliesRepository;

    public void save(LinkAllies linkAllies) {
        linkAlliesRepository.save(linkAllies);
    }

    @Transactional
    public void save(Set<LinkAllies> alliesSet) {
        alliesSet.forEach(this::save);
    }

    public LinksPair getLinksPairByAlly(String ally) {
        return linkAlliesRepository.getLinksPairByAlly(ally).orElseThrow(
                () -> new AllyNotFoundException("Ally '" + ally + "' was not found")
        );
    }
}