package org.gershaw.quickfixj.springboot.client.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import quickfix.FieldNotFound;
import quickfix.field.MDEntryType;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SubscriptionRequestType;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.component.InstrmtMDReqGrp;
import quickfix.fix50sp2.component.MDReqGrp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * I have purposely not imported static methods in the implementation of this test suite. I'm trying to avoid people
 * wondering where did this method come from. This code could be shortened with static imports
 */
class MarketDataRequestorTest {

    @Test
    void testTo() throws FieldNotFound {
        final Character[] mdEntries = new Character[]{MDEntryType.BID,MDEntryType.OFFER};
        final String secIdSourceIsin = "4";
        final String secIdSourceCusip = "1";
        final Map<String,String> expectedInstruments = Map.of("ISIN1", secIdSourceIsin,"CUSIP1", secIdSourceCusip);
        final MarketDataRequest actualMarketDataRequest = MarketDataRequestor.of(mdEntries, expectedInstruments);

        //Begin assertions
        Assertions.assertNotNull(actualMarketDataRequest.getMDReqID().getValue());
        Assertions.assertEquals(0, actualMarketDataRequest.getMarketDepth().getValue());
        Assertions.assertEquals(SubscriptionRequestType.SNAPSHOT_UPDATES,actualMarketDataRequest.getSubscriptionRequestType().getValue());
        final List<Character> actualMdEntryTypes = getMdEntryTypeChars(actualMarketDataRequest);
        final Map<String,String> actualInstruments = getInstruments(actualMarketDataRequest);
        Assertions.assertEquals(mdEntries.length, actualMarketDataRequest.getMDReqGrp().getNoMDEntryTypes().getValue());
        Assertions.assertTrue(actualMdEntryTypes.containsAll(Arrays.asList(mdEntries)));
        Assertions.assertEquals(expectedInstruments.size(), actualMarketDataRequest.getInstrmtMDReqGrp().getNoRelatedSym().getValue());
        Assertions.assertEquals(actualInstruments, expectedInstruments);
    }

    private List<Character> getMdEntryTypeChars(final MarketDataRequest marketDataRequest) throws FieldNotFound {
        final List<Character> mdEntryTypesRequested = new ArrayList<>();

        for (int i = 1; i <= marketDataRequest.getMDReqGrp().getNoMDEntryTypes().getValue(); i++){
            final MDReqGrp.NoMDEntryTypes mdEntryTypeGroup = new MDReqGrp.NoMDEntryTypes();
            marketDataRequest.getMDReqGrp().getGroup(i, mdEntryTypeGroup);
            mdEntryTypesRequested.add(mdEntryTypeGroup.getMDEntryType().getValue());
        }
        return mdEntryTypesRequested;
    }

    private Map<String,String> getInstruments(final MarketDataRequest marketDataRequest) throws FieldNotFound {
        final Map<String,String> instruments = new HashMap<>();

        for (int i = 1; i <= marketDataRequest.getInstrmtMDReqGrp().getNoRelatedSym().getValue(); i++){
            final InstrmtMDReqGrp.NoRelatedSym noRelatedSymGroup = new InstrmtMDReqGrp.NoRelatedSym();
            marketDataRequest.getInstrmtMDReqGrp().getGroup(i, noRelatedSymGroup);
            final SecurityID securityID = new SecurityID();
            final SecurityIDSource securityIDSource = new SecurityIDSource();
            noRelatedSymGroup.get(securityID);
            noRelatedSymGroup.get(securityIDSource);
            instruments.put(securityID.getValue(), securityIDSource.getValue());
        }
        return instruments;
    }
}