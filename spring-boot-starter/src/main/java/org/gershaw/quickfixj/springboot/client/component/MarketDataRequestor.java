package org.gershaw.quickfixj.springboot.client.component;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MarketDepth;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.component.InstrmtMDReqGrp;
import quickfix.fix50sp2.component.Instrument;
import quickfix.fix50sp2.component.MDReqGrp;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Utility class responsible for creating MarketDataRequests
 */
@Slf4j
@NoArgsConstructor
public class MarketDataRequestor {

    /**
     * static method which create a MarketDataRequest
     * @param mdEntryTypeNeeds the MdEntryTypes being requested for. tag 269
     * @param instruments a map of the securityId(tag 42) to the securityIdSource(tag 22). Note: I'm incorrectly naively assuming
     * the securityIdSource is valid
     * @return fix5.02p2 MarketDataRequest
     */
    public static MarketDataRequest of(@NonNull final Character[] mdEntryTypeNeeds,
                                       @NonNull final Map<String, String> instruments) {
        final int fullBookDepth = 0;
        final MarketDataRequest request = new MarketDataRequest(
            new MDReqID(UUID.randomUUID().toString()),
            new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES),
            new MarketDepth(fullBookDepth));

        final MDReqGrp mdReqGrp = createMdReqGrp(Arrays.copyOf(mdEntryTypeNeeds,mdEntryTypeNeeds.length));
        request.set(mdReqGrp);
        final InstrmtMDReqGrp instrmtMDReqGrp = createInstrmtMdReqGrp(Map.copyOf(instruments));
        request.set(instrmtMDReqGrp);
        return request;
    }


    private static InstrmtMDReqGrp createInstrmtMdReqGrp(Map<String, String> instruments) {
        final InstrmtMDReqGrp instrmtMDReqGrp = new InstrmtMDReqGrp();

        instruments.entrySet()
            .stream()
            .map(
                entry -> {
                    final Instrument instrument = new Instrument();
                    instrument.set(new Symbol("N/A"));
                    instrument.set(new SecurityID(entry.getKey()));
                    instrument.set(new SecurityIDSource(entry.getValue()));
                    final InstrmtMDReqGrp.NoRelatedSym noRelatedSym = new InstrmtMDReqGrp.NoRelatedSym();
                    noRelatedSym.set(instrument);
                    return noRelatedSym;
                })
            .forEach(instrmtMDReqGrp::addGroup);

        return instrmtMDReqGrp;
    }

    private static MDReqGrp createMdReqGrp(final Character[] mdEntryTypes) {
        //TODO: Check that array contains only legal values or throw IllegalArgExc
        final Set<MDReqGrp.NoMDEntryTypes> mdEntryTypesSet = createMdEntryTypes(mdEntryTypes);
        final MDReqGrp mdReqGrp = new MDReqGrp();
        mdEntryTypesSet.forEach(mdReqGrp::addGroup);
        return mdReqGrp;
    }

    private static Set<MDReqGrp.NoMDEntryTypes> createMdEntryTypes(final Character[] mdEntryTypes) {
        return Arrays.stream(mdEntryTypes)
            .map(m -> {
                final MDReqGrp.NoMDEntryTypes noMDEntryTypes = new MDReqGrp.NoMDEntryTypes();
                noMDEntryTypes.set(new MDEntryType(m));
                return noMDEntryTypes;
            })
            .collect(Collectors.toUnmodifiableSet());
    }
}
