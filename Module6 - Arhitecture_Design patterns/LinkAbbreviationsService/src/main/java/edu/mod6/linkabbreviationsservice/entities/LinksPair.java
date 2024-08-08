package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    protected String shortLink;

    @Column(name = "src_link", unique = true, nullable = false)
    protected String srcLink;

    @OneToMany(mappedBy = "linksPair", cascade = CascadeType.ALL)
    protected Set<LinkAllies> allies;

    public LinksPair(String shortLink) {
        this.shortLink = shortLink;
    }

    protected LinksPair() {}        // Сделано так, чтобы создание id было только через конструктор
}