package edu.mod6.linkabbreviationsservice.dto.mappes;

import edu.mod6.linkabbreviationsservice.dto.LinksPairDto;
import edu.mod6.linkabbreviationsservice.entities.LinkAllies;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class LinksPairDtoMapper {

    public LinksPairDto toDto(LinksPair linksPair) {
        return LinksPairDto.builder()
                .srcLink(linksPair.getSrcLink())
                .shortLink(linksPair.getShortLink())
                .allies(
                        linksPair.getAllies().stream()
                                .map(LinkAllies::getAllyName)
                                .collect(Collectors.toSet())
                )
                .expiredIn(     //todo Ну выглядит так себе, но как тогда сделать это красиво...
                        linksPair instanceof TemporaryLinksPair? ((TemporaryLinksPair) linksPair).getExpiredIn(): null
                )
                .build();
    }
}