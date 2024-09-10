package edu.mod6.linkabbreviationsservice.services;

import edu.mod6.linkabbreviationsservice.config.TestContainersConfiguration;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.dto.mappes.LinksPairDtoMapperImpl;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.exceptions.LinksPairNotFoundException;
import edu.mod6.linkabbreviationsservice.repositories.LinkAlliesRepository;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;
import edu.mod6.linkabbreviationsservice.repositories.ShortenLinkIdSequenceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.Optional;

/**
 * todo ДЛЯ ЖЕНИ !!!
 *  Прекрасно понимаю, что нужно протестировать каждый слой изолированно, а потом еще тесты вообще всего приложения.
 *   Такие тесты я писал в двух модулях у Радиона, суть я понял. Просто этих тестов там минимум строк 1000. Поэтому
 *   дай мне возможность схалтурить и ограничиться лишь этим тестом
 */

@DataJpaTest
@Import({
        TestContainersConfiguration.class,      // Для тест-контейнеров
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LinksPairServiceTests {

    LinksPairService linksPairService;
    ShortenLinkIdSequenceService shortenLinkIdSequenceService;
    LinkAlliesService linkAlliesService;
    ShortenLinkIdSequenceRepository shortenLinkIdSequenceRepository;
    LinksPairDtoMapperImpl linksPairDtoMapper;

    @Autowired
    LinksPairRepository linksPairRepository;
    @Autowired
    LinkAlliesRepository linkAlliesRepository;
    @Autowired
    EntityManager entityManager;

    @PostConstruct
    void serviceInit() {
        linksPairDtoMapper = new LinksPairDtoMapperImpl();

        shortenLinkIdSequenceRepository = new ShortenLinkIdSequenceRepository(
                entityManager
        );

        linkAlliesService = new LinkAlliesService(
                linkAlliesRepository
        );

        shortenLinkIdSequenceService = new ShortenLinkIdSequenceService(
                shortenLinkIdSequenceRepository
        );

        linksPairService = new LinksPairService(
                shortenLinkIdSequenceService,
                linksPairRepository,
                linkAlliesService,
                linksPairDtoMapper
        );
    }



    @Nested
    class LinksAbbreviationMethodTests {

        @Test
        @Sql("/sql/add_one_linkPair.sql")
        public void existingLinkAbbreviation() {
            final String shortLink = "http://localhost:8080/linksAbbreviation/shorten_link_1";

            Optional<LinksPair> opt = linksPairRepository.findByShortLink(shortLink);

            Assertions.assertTrue(opt.isPresent());

            RedirectView redirectView = linksPairService.linksAbbreviation(shortLink);
            String srcLink = opt.get().getSrcLink();

            Assertions.assertEquals(srcLink, redirectView.getUrl());
        }

        @Test
        public void nonExistingLinkAbbreviation() {
            final String shortLink = "http://localhost:8080/linksAbbreviation/shorten_link_200";

            Assertions.assertTrue(linksPairRepository.findByShortLink(shortLink).isEmpty());

            Assertions.assertThrows(
                    LinksPairNotFoundException.class,
                    () -> linksPairService.deleteBySrcLink(shortLink)
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
                    null,
                    Collections.emptySet()
            );

            String shortLink = linksPairService.register(dto);

            Optional<LinksPair> opt = linksPairRepository.findBySrcLink(srcLink);

            Assertions.assertTrue(opt.isPresent());
            Assertions.assertEquals(srcLink, opt.get().getSrcLink());
            Assertions.assertEquals(shortLink, opt.get().getShortLink());
        }

        @Test
        @Sql("/sql/add_one_linkPair.sql")
        void uniqueViolationRegistration() {
            final String srcLink = "https://www.youtube.com";

            RegisterLinkDto dto = new RegisterLinkDto(
                    srcLink,
                    null,
                    Collections.emptySet()
            );

            Assertions.assertThrows(ValidationException.class, () -> linksPairService.register(dto));
        }
    }

    @Nested
    class RemoveMethodTests {

        @Test
        @Sql("/sql/add_one_linkPair.sql")
        public void removeExisting() {
            final String srcLink = "https://www.youtube.com";

            Assertions.assertTrue(linksPairRepository.findBySrcLink(srcLink).isPresent());

            linksPairService.deleteBySrcLink(srcLink);

            Assertions.assertThrows(LinksPairNotFoundException.class, () -> linksPairService.findBySrcLink(srcLink));
        }

        @Test
        public void removeNonExisting() {
            final String srcLink = "https://www.google.ru/";

            Assertions.assertTrue(linksPairRepository.findBySrcLink(srcLink).isEmpty());

            Assertions.assertThrows(
                    LinksPairNotFoundException.class,
                    () -> linksPairService.deleteBySrcLink(srcLink)
            );
        }
    }

}