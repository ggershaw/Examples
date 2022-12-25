package org.gershaw.quickfixj.rest.transformer;

import java.util.function.Function;
import org.gershaw.quickfixj.rest.dto.QuoteDto;
import quickfix.fix50sp2.MassQuote;

public class QuoteFixTransformer implements Function<QuoteDto, MassQuote> {

  @Override
  public MassQuote apply(QuoteDto quoteDto) {
    return null;
  }
}
