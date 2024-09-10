package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "links_pair")
@AllArgsConstructor
@NoArgsConstructor
public class LinksPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "short_link")
    private String shortLink;

    @Column(name = "src_link")
    private String srcLink;

    @OneToMany(mappedBy = "linksPair", cascade = CascadeType.ALL)
    private Set<LinkAllies> allies;

    @Column(name = "expired_in")
    private OffsetDateTime expiredIn;


    public boolean isTemporary() {
        return expiredIn != null;
    }

    public boolean isExpired() {
        return isTemporary() && expiredIn.isBefore(OffsetDateTime.now());
    }
}