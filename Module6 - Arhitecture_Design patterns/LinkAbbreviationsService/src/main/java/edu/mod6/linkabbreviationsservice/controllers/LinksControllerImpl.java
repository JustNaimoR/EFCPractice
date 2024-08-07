package edu.mod6.linkabbreviationsservice.controllers;

import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import edu.mod6.linkabbreviationsservice.entities.TemporaryLinksPair;
import edu.mod6.linkabbreviationsservice.services.LinksPairService;
import edu.mod6.linkabbreviationsservice.services.TemporaryLinksPairService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/linksAbbreviation")
@RequiredArgsConstructor
public class LinksControllerImpl implements LinksController {
    private final TemporaryLinksPairService temporaryLinksPairService;
    private final LinksPairService linksPairService;

    @GetMapping("/{shortenedLink}")
    public RedirectView linksAbbreviation(@PathVariable("shortenedLink") String shortLink) {
        return linksPairService.linksAbbreviation(shortLink);
    }


    @Override
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterLinkDto dto) {     //todo написать обработку исключения при валидации
        String shortLink = "http://localhost:8080/linksAbbreviation/";

        if (dto.isTemporary()) {
            shortLink += temporaryLinksPairService.register(dto);
        } else {
            shortLink += linksPairService.register(dto);
        }

        return ResponseEntity.ok(
                shortLink
        );
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<LinksPair>> getAll() {
        List<LinksPair> list_1 = linksPairService.getAll();
        List<TemporaryLinksPair> list_2 = temporaryLinksPairService.getAll();

        return ResponseEntity.ok(
                Stream.concat(list_1.stream(), list_2.stream()).toList()
        );
    }

    @Override
    @DeleteMapping("/remove")
    public void remove(@RequestParam("src_link") String srcLink) {        //todo стоит передавать src_link в url? Или зажирно использовать @RequestBody
        linksPairService.remove(srcLink);
    }


}