package edu.mod6.linkabbreviationsservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "link_allies")
public class LinkAllies {

    @Id
    @Column(name = "ally_name")
    private String allyName;

    @ManyToOne()
    @JoinColumn(name = "short_link")
    private LinksPair linksPair;

    public LinkAllies(String allyName) {
        this.allyName = allyName;
    }

    protected LinkAllies() {}
}