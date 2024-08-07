package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    public LinksPair(String shortLink) {
        this.shortLink = shortLink;
    }

    protected LinksPair() {}        // Сделано так, чтобы создание id было только через конструктор
}