package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.config.ServiceLayerConfiguration;
import edu.mod6.linkabbreviationsservice.config.TestContainersConfiguration;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;
import edu.mod6.linkabbreviationsservice.repositories.TemporaryLinksPairRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Optional;

@DataJpaTest
@Import({
        ServiceLayerConfiguration.class,
        TestContainersConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TemporaryPairServiceTests {
    @Autowired
    TemporaryLinksPairService service;
    @Autowired
    TemporaryLinksPairRepository repository;

    @Nested
    class RegisterMethodTests {

        @Test
        void simpleRegistration() {
            final String srcLink = "https://javarush.com";
            final Long timestamp = 1000L;

            RegisterLinkDto dto = new RegisterLinkDto(
                    srcLink,
                    timestamp
            );

            String shortLink = service.register(dto);

            Optional<TemporaryLinksPair> opt = repository.findBySrcLink(srcLink);

            Assertions.assertTrue(opt.isPresent());
            Assertions.assertEquals(srcLink, opt.get().getSrcLink());
            Assertions.assertEquals(shortLink, opt.get().getShortLink());
            Assertions.assertNotNull(opt.get().getExpiredIn());
        }

    }

    @Nested
    class LinkIsExpiredMethodTests {

        @Test
        void linkIsNotExpired() {
            final String shortLink = "short_link";

            TemporaryLinksPair linksPair = new TemporaryLinksPair(
                    shortLink, "src_link", Instant.now().plusSeconds(3600)   // + 1 час
            );

            repository.save(linksPair);

            Assertions.assertTrue(repository.existsByShortLink(shortLink));
            Assertions.assertFalse(service.linkIsExpired(shortLink));
        }

        @Test
        void linkIsExpired() {
            final String shortLink = "short_link";

            TemporaryLinksPair linksPair = new TemporaryLinksPair(
                    shortLink, "src_link", Instant.now().minusSeconds(3600)   // - 1 час
            );

            repository.save(linksPair);

            Assertions.assertTrue(repository.existsByShortLink(shortLink));
            Assertions.assertTrue(service.linkIsExpired(shortLink));
        }
    }

}