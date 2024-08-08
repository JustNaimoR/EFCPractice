package edu.mod6.linkabbreviationsservice.controllers;

import edu.mod6.linkabbreviationsservice.dto.LinksPairDto;
import edu.mod6.linkabbreviationsservice.dto.RegisterLinkDto;
import edu.mod6.linkabbreviationsservice.entities.LinksPair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Set;

public interface LinksController {

    ResponseEntity<String> register(RegisterLinkDto dto);

    ResponseEntity<List<LinksPairDto>> getAll();

    void remove(String srcLink);

    RedirectView linksAbbreviation(String shortLink);
}