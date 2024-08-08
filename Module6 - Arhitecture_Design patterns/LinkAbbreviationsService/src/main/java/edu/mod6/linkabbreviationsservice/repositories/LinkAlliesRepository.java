package edu.mod6.linkabbreviationsservice.repositories;

import edu.mod6.linkabbreviationsservice.entities.LinkAllies;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkAlliesRepository extends JpaRepository<LinkAllies, String> {

    boolean existsByAllyName(String allyName);

    @Query(
            value = "select lp " +
                    "from LinksPair lp left join LinkAllies la on lp.shortLink = la.linksPair.shortLink " +
                    "where la.allyName = :ally"
    )
    Optional<LinksPair> getLinksPairByAlly(@Param("ally") String ally);
}