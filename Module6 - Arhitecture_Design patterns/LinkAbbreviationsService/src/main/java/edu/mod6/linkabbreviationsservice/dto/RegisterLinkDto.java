package edu.mod6.linkabbreviationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mod6.linkabbreviationsservice.entities.LinkAllies;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record RegisterLinkDto(
        @JsonProperty(value = "src_link", required = true)
        @NotEmpty
        String srcLink,

        @JsonProperty(value = "expired_time")
        @Min(0)
        Long operatingTime,

        @JsonProperty(value = "allies")
        Set<String> allies
) {
    // Проверка, что dto содержит время, а значит действие пары временно
    public boolean isTemporary() {
        return operatingTime != null;
    }

    public boolean hasAllies() {
        return allies != null && !allies.isEmpty();
    }

    public LinksPair fromDto() {
        Set<LinkAllies> linkAllies = getLinkAllies();
        OffsetDateTime dateTime = getDateTime();

        return new LinksPair(
                0, null, srcLink, linkAllies, dateTime
        );
    }

    public Set<LinkAllies> getLinkAllies() {
        if (hasAllies()) {
            return allies.stream().map(LinkAllies::new).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public OffsetDateTime getDateTime() {
        if (isTemporary()) {
            return OffsetDateTime.now().plusSeconds(operatingTime / 1000);
        }
        return null;
    }
}