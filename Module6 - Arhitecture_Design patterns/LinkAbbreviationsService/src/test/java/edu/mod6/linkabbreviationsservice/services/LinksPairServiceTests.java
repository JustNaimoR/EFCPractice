package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.config.ServiceLayerConfiguration;
import edu.mod6.linkabbreviationsservice.config.TestContainersConfiguration;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@DataJpaTest
@Import({
        TestContainersConfiguration.class,      // Для тест-контейнеров
        ServiceLayerConfiguration.class         // Для сервисного слоя
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LinksPairServiceTests {
    @Autowired
    LinksPairService service;
    @Autowired
    LinksPairRepository repository;

    @Nested
    class LinksAbbreviationMethodTests {

        @Test
        @Sql("/sql/add_one_linkPair.sql")
        public void existingLinkAbbreviation() {
            final String shortLink = "http://localhost:8080/linksAbbreviation/shorten_link_1";

            Optional<LinksPair> opt = repository.findByShortLink(shortLink);

            Assertions.assertTrue(opt.isPresent());

            RedirectView redirectView = service.linksAbbreviation(shortLink);
            String srcLink = opt.get().getSrcLink();

            Assertions.assertEquals(srcLink, redirectView.getUrl());
        }

        @Test
        public void nonExistingLinkAbbreviation() {
            final String shortLink = "http://localhost:8080/linksAbbreviation/shorten_link_200";

            Assertions.assertTrue(repository.findByShortLink(shortLink).isEmpty());

            Assertions.assertThrows(
                    LinksPairNotFoundException.class,
                    () -> service.remove(shortLink)
            );
        }

    }

    @Nested
    class RegisterMethodTests {

        @Test
        public void simpleRegistration() {
            final String srcLink = "https://www.yandex.com/";

            RegisterLinkDto dto = new RegisterLinkDto(
                    srcLink,
                    null
            );

            String shortLink = service.register(dto);

            Optional<LinksPair> opt = repository.findBySrcLink(srcLink);

            Assertions.assertTrue(opt.isPresent());
            Assertions.assertEquals(srcLink, opt.get().getSrcLink());
            Assertions.assertEquals(shortLink, opt.get().getShortLink());
        }

    }

    @Nested
    class RemoveMethodTests {

        @Test
        @Sql("/sql/add_one_linkPair.sql")
        public void removeExisting() {
            final String srcLink = "https://www.youtube.com";

            Assertions.assertTrue(repository.findBySrcLink(srcLink).isPresent());

            service.remove(srcLink);

            Assertions.assertTrue(repository.findBySrcLink(srcLink).isEmpty());
        }

        @Test
        public void removeNonExisting() {
            final String srcLink = "https://www.google.ru/";

            Assertions.assertTrue(repository.findBySrcLink(srcLink).isEmpty());

            Assertions.assertThrows(
                    LinksPairNotFoundException.class,
                    () -> service.remove(srcLink)
            );
        }

    }

}