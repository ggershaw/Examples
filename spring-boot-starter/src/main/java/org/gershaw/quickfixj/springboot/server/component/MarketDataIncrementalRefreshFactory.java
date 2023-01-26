package org.gershaw.quickfixj.springboot.server.component;

import lombok.NonNull;
import quickfix.field.MDBookType;
import quickfix.field.MDEntryID;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateAction;
import quickfix.field.PriceType;
import quickfix.field.SecurityIDSource;
import quickfix.field.Symbol;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.component.MDIncGrp;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MarketDataIncrementalRefreshFactory {

    public static MarketDataIncrementalRefresh of(@NonNull final String requestId, @NonNull final Security security) {
        final MarketDataIncrementalRefresh incrementalRefresh = new MarketDataIncrementalRefresh();
        incrementalRefresh.set(new MDReqID(requestId));
        incrementalRefresh.set(new MDBookType(MDBookType.TOP_OF_BOOK));

        final MDIncGrp mdIncGrp = createIncGroup(security);
        incrementalRefresh.set(mdIncGrp);
        return incrementalRefresh;
    }

    private static MDIncGrp createIncGroup(@NonNull final Security security) {
        final MDIncGrp mdIncGrp = new MDIncGrp();
        createMdEntryTypes(security).forEach(mdIncGrp::addGroup);
        return mdIncGrp;
    }

    private static Set<MDIncGrp.NoMDEntries> createMdEntryTypes(@NonNull final Security security) {
        final MDIncGrp.NoMDEntries offerMdEntry = createMdEntry(security, SideType.OFFER);
        final MDIncGrp.NoMDEntries bidMdEntry = createMdEntry(security, SideType.BID);
        return Set.of(offerMdEntry, bidMdEntry);

    }

    private static MDIncGrp.NoMDEntries createMdEntry(@NonNull final Security security,@NonNull final SideType side) {
        final MDIncGrp.NoMDEntries mdEntry = new MDIncGrp.NoMDEntries();
        final Random random = new Random();
        mdEntry.set(new MDUpdateAction(MDUpdateAction.CHANGE));
        switch (side){
            case BID -> mdEntry.set(new MDEntryType(MDEntryType.BID));
            case OFFER -> mdEntry.set(new MDEntryType(MDEntryType.OFFER));
        }
        mdEntry.set(new Symbol(security.securityId()));
        mdEntry.set(new SecurityIDSource(security.securityIdType));
        mdEntry.set(new MDEntryID(UUID.randomUUID().toString()));
        mdEntry.set(new MDEntryPx(random.nextDouble(0,200d)));
        mdEntry.set(new MDEntrySize(random.nextDouble(1_000, 1_000_000)));
        mdEntry.set((new PriceType(PriceType.PERCENTAGE)));
        return mdEntry;

    }

    //TODO: create a SecurityIdType enum
    public record Security(String securityId, String securityIdType) {
    }

    enum SideType{
        OFFER,
        BID
    }
}
