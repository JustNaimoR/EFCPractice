package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Table(name = "temporary_links_pair")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemporaryLinksPair extends LinksPair {

    @Column(name = "expired_in")
    private Instant expiredIn;

    public TemporaryLinksPair(String shortLink, String srcLink, Instant expiredIn, Set<LinkAllies> allies) {
        super(shortLink, srcLink, allies);
        this.expiredIn = expiredIn;
    }

}