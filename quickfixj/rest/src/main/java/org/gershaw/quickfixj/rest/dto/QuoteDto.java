package org.gershaw.quickfixj.rest.dto;

import java.util.Objects;
import org.gershaw.quickfixj.rest.types.SecurityIdType;
import org.gershaw.quickfixj.rest.types.SideType;

public record QuoteDto(String quoteId, String securityId, SecurityIdType secIdType, double price, int size, SideType sideType) {

  public QuoteDto {
    Objects.requireNonNull(quoteId);
    Objects.requireNonNull(securityId);
    Objects.requireNonNull(secIdType);
    Objects.requireNonNull(sideType);
  }
}
