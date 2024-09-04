package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "links_pair")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
public class LinksPair {

    @Id
    @Column(name = "short_link")
    private String shortLink;

    @Column(name = "src_link", unique = true, nullable = false)
    private String srcLink;

    @OneToMany(mappedBy = "linksPair", cascade = CascadeType.ALL)
    private Set<LinkAllies> allies;

    @Column(name = "expired_in")
    private ZonedDateTime expiredIn;

    public LinksPair(String shortLink) {
        this.shortLink = shortLink;
    }

    protected LinksPair() {}        // Сделано так, чтобы создание id было только через конструктор

    public boolean isTemporary() {
        return expiredIn != null;
    }

    public boolean isExpired() {
        return isTemporary() && expiredIn.isBefore(ZonedDateTime.now());
    }
}