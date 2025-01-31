package edu.mod6.linkabbreviationsservice.repositories;

import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinksPairRepository extends JpaRepository<LinksPair, Integer> {

    @Query(nativeQuery = true, value = "select nextval('shorten_link_id_seq')")
    Long getNextShortenLinkSequenceValue();

    Optional<LinksPair> findBySrcLink(String srcLink);

    Optional<LinksPair> findByShortLink(String shortLink);

    void deleteBySrcLink(String srcLink);

    boolean existsBySrcLink(String srcLink);
}