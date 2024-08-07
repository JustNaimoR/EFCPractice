package edu.mod6.linkabbreviationsservice.repositories;

import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryLinksPairRepository extends JpaRepository<TemporaryLinksPair, String> {

    boolean existsByShortLink(String shortLink);

    void deleteByShortLink(String shortLink);

    Optional<TemporaryLinksPair> findByShortLink(String shortLink);

    Optional<TemporaryLinksPair> findBySrcLink(String srcLink);
}