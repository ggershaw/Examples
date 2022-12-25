package org.gershaw.quickfixj.rest.service;

import java.util.Objects;
import java.util.concurrent.Semaphore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.gershaw.quickfixj.rest.dto.QuoteAckDto;
import org.gershaw.quickfixj.rest.dto.QuoteDto;
import org.gershaw.quickfixj.rest.transformer.QuoteFixTransformer;
import org.gershaw.quickfixj.ssl.client.FixInitiator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import quickfix.fix50sp2.MassQuote;

@Service
@RequiredArgsConstructor
public class QuoteService {
  private final FixInitiator initiator;
  private final Semaphore semaphore = new Semaphore(1);
  private final QuoteFixTransformer fixTransformer = new QuoteFixTransformer();
  private QuoteAckDto quoteAck;

  @SneakyThrows
  public QuoteAckDto sendQuote(QuoteDto quote) {
    Objects.requireNonNull(quote);
    final boolean isSent =  send(quote);

    semaphore.acquire();
    Objects.requireNonNull(this.quoteAck);
    return this.quoteAck;
  }

  @EventListener
  public void receiveAck(final QuoteAckDto quoteAck){
    this.quoteAck = quoteAck;
    semaphore.release();
  }

  private MassQuote transform(QuoteDto quote) {
    return fixTransformer.apply(quote);
  }


  @SneakyThrows
  private boolean send(QuoteDto quote) {
      semaphore.acquire();
      final MassQuote massQuote = transform(quote);
      return initiator.send(massQuote);
  }

}
