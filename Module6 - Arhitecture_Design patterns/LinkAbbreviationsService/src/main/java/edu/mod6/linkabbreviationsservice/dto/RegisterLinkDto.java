package edu.mod6.linkabbreviationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.mod6.linkabbreviationsservice.annotations.UniqueSourceLink;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record RegisterLinkDto(
        @JsonProperty(value = "src_link", required = true)
        @NotEmpty
        @UniqueSourceLink
        String srcLink,

        @JsonProperty(value = "expired_time")
        @Min(0)
        Long operatingTime
) {
    // Проверка что dto содержит время, а значит действие пары временно
    public boolean isTemporary() {
        return operatingTime != null;
    }
}