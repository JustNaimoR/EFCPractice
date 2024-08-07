package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Table(name = "temporary_links_pair")
@NoArgsConstructor
public class TemporaryLinksPair extends LinksPair {

    @Column(name = "expired_in")
    private Instant expiredIn;

    public TemporaryLinksPair(String shortLink, String srcLink, Instant expiredIn) {
        super(shortLink, srcLink);
        this.expiredIn = expiredIn;
    }

}