package edu.mod6.linkabbreviationsservice.config;

import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;
import edu.mod6.linkabbreviationsservice.repositories.ShortenLinkIdSequenceRepository;
import edu.mod6.linkabbreviationsservice.repositories.TemporaryLinksPairRepository;
import edu.mod6.linkabbreviationsservice.services.LinksPairService;
import edu.mod6.linkabbreviationsservice.services.ShortenLinkIdSequenceService;
import edu.mod6.linkabbreviationsservice.services.TemporaryLinksPairService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Отдельный класс-конфигурации для сервисного слоя     todo стоит ли так делать, если требуются классы сервиса, которых нат в @DataJpaTest?

@Configuration
public class ServiceLayerConfiguration {

    @Bean
    public LinksPairService linksPairService(
            ShortenLinkIdSequenceService shortenLinkIdSequenceService,
            RegisterLinkDtoMapper registerLinkDtoMapper,
            LinksPairRepository linksPairRepository,
            TemporaryLinksPairService temporaryLinksPairService
    ) {
        return new LinksPairService(
                shortenLinkIdSequenceService,
                registerLinkDtoMapper,
                linksPairRepository,
                temporaryLinksPairService
        );
    }

    @Bean
    public ShortenLinkIdSequenceService shortenLinkIdSequenceService(
            ShortenLinkIdSequenceRepository shortenLinkIdSequenceRepository
    ) {
        return new ShortenLinkIdSequenceService(
                shortenLinkIdSequenceRepository
        );
    }

    @Bean
    public TemporaryLinksPairService temporaryLinksPairService(
            ShortenLinkIdSequenceService shortenLinkIdSequenceService,
            TemporaryLinksPairRepository temporaryLinksPairRepository,
            RegisterLinkDtoMapper registerLinkDtoMapper
    ) {
        return new TemporaryLinksPairService(
                shortenLinkIdSequenceService,
                temporaryLinksPairRepository,
                registerLinkDtoMapper
        );
    }

    @Bean
    public RegisterLinkDtoMapper registerLinkDtoMapper() {
        return new RegisterLinkDtoMapper();
    }

    @Bean
    public ShortenLinkIdSequenceRepository shortenLinkIdSequenceRepository() {
        return new ShortenLinkIdSequenceRepository();
    }
}