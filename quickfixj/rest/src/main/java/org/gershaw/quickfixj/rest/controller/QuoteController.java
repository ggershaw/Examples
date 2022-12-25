package org.gershaw.quickfixj.rest.controller;

import lombok.RequiredArgsConstructor;
import org.gershaw.quickfixj.rest.dto.QuoteAckDto;
import org.gershaw.quickfixj.rest.dto.QuoteDto;
import org.gershaw.quickfixj.rest.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quotes")
@RequiredArgsConstructor
class QuoteController {
  private final QuoteService quoteService;

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public QuoteAckDto createQuote(@RequestBody QuoteDto quote){
    return quoteService.sendQuote(quote);
  }
}
