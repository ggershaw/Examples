package org.gershaw.quickfixj.rest.dto;

public record QuoteAckDto(String fixQuoteId, String internalQuoteId, boolean isSuccess) {

}
