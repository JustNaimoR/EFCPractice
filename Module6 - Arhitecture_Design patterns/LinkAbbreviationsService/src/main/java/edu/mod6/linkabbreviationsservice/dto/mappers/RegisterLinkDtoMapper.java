package edu.mod6.linkabbreviationsservice.dto.mappers;

import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.time.Instant;

//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Component
public class RegisterLinkDtoMapper {

    public LinksPair fromDto(RegisterLinkDto dto) {
        if (dto.isTemporary()) {
            Instant expiresIn = Instant.now().plusMillis(dto.operatingTime());
            String srcLink = dto.srcLink();

            return new TemporaryLinksPair(
                    null,
                    srcLink,
                    expiresIn
            );
        } else {
            String srcLink = dto.srcLink();

            return new LinksPair(
                    null,
                    srcLink
            );
        }
    }

}