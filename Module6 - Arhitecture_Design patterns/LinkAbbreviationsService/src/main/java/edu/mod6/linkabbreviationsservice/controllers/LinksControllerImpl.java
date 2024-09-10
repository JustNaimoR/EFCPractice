package edu.mod6.linkabbreviationsservice.controllers;

import edu.mod6.linkabbreviationsservice.dto.LinksPairDto;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.services.LinksPairService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/linksAbbreviation")
@RequiredArgsConstructor
public class LinksControllerImpl implements LinksController {
    private final LinksPairService linksPairService;



    @GetMapping("/ally")
    public RedirectView linkAllies(@PathParam("ally") String ally) {
        return linksPairService.linkAllyAbbreviation(ally);
    }

    @GetMapping("/{shortenedLink}")
    public RedirectView linksAbbreviation(@PathVariable("shortenedLink") String shortLink) {
        return linksPairService.linksAbbreviation(shortLink);
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterLinkDto dto) {
        String shortLink = "http://localhost:8080/linksAbbreviation/";

        shortLink += linksPairService.register(dto);

        return ResponseEntity.ok(
                shortLink
        );
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<LinksPairDto>> getAll() {
        List<LinksPairDto> set = linksPairService.getAllToDto();

        return ResponseEntity.ok(set);
    }

    @Override
    @DeleteMapping("/remove")
    public void remove(@RequestParam("src_link") String srcLink) {        //todo стоит передавать src_link в url? Или зажирно использовать @RequestBody
        linksPairService.deleteBySrcLink(srcLink);
    }
}