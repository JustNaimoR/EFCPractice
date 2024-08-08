package edu.mod6.linkabbreviationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mod6.linkabbreviationsservice.annotations.UniqueAllies;
import edu.mod6.linkabbreviationsservice.annotations.UniqueSourceLink;
import edu.mod6.linkabbreviationsservice.entities.LinkAllies;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record RegisterLinkDto(
        @JsonProperty(value = "src_link", required = true)
        @NotEmpty
        @UniqueSourceLink
        String srcLink,

        @JsonProperty(value = "expired_time")
        @Min(0)
        Long operatingTime,

        @JsonProperty(value = "allies")
        @UniqueAllies
        Set<String> allies
) {
    // Проверка что dto содержит время, а значит действие пары временно
    public boolean isTemporary() {
        return operatingTime != null;
    }

    public boolean hasAllies() {
        return allies != null && !allies.isEmpty();
    }

    public LinksPair fromDto() {
        Set<LinkAllies> linkAllies = getLinkAllies();

        if (isTemporary()) {
            Instant expiresIn = Instant.now().plusMillis(operatingTime);

            return new TemporaryLinksPair(
                    null, srcLink, expiresIn, linkAllies
            );
        } else {
            return new LinksPair(
                    null, srcLink, linkAllies
            );
        }
    }

    public Set<LinkAllies> getLinkAllies() {
        if (hasAllies())
            return allies.stream().map(LinkAllies::new).collect(Collectors.toSet());
        return Collections.emptySet();
    }
}