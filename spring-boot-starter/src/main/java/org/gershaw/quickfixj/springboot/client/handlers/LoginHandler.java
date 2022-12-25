package org.gershaw.quickfixj.springboot.client.handlers;

import io.allune.quickfixj.spring.boot.starter.model.Logon;
import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import quickfix.Initiator;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MarketDepth;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SubscriptionRequestType;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MarketDataRequest.NoRelatedSym;
import quickfix.fix50sp2.component.InstrmtMDReqGrp;
import quickfix.fix50sp2.component.MDReqGrp;
import quickfix.fix50sp2.component.MDReqGrp.NoMDEntryTypes;

@Slf4j
public class LoginHandler {

  private final Character[] mdEntryTypeNeeds;
  private final Map<String, SecurityIDSource> instruments;
  private final QuickFixJTemplate fixJTemplate;

  private final Initiator initiator;

  public LoginHandler(final QuickFixJTemplate fixJTemplate, final Map<String, SecurityIDSource> instruments,
      final Initiator initiator){
    this.initiator = initiator;
    this.mdEntryTypeNeeds = new Character[]{MDEntryType.BID, MDEntryType.OFFER};
    this.instruments = Map.copyOf(instruments);
    this.fixJTemplate = fixJTemplate;
  }
  public LoginHandler(final QuickFixJTemplate fixJTemplate, final Character[] mdEntryTypeNeeds, final Map<String, SecurityIDSource> instruments,
      final Initiator initiator) {
    this.mdEntryTypeNeeds = mdEntryTypeNeeds;
    this.instruments = Map.copyOf(instruments);
    this.fixJTemplate = fixJTemplate;
    this.initiator = initiator;
  }


  @EventListener
  public void handleOnLogon(Logon logon) {
    log.info("Received logon: {}. ", logon);
    MarketDataRequest marketDataRequest = createMdr(this.instruments);
    log.info("Requesting MarketData: {}",marketDataRequest);

  }

  private MarketDataRequest createMdr(final Map<String, SecurityIDSource> instruments) {
    final int fullBookDepth = 0;
    final MarketDataRequest request = new MarketDataRequest(
        new MDReqID(UUID.randomUUID().toString()),
        new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES),
        new MarketDepth(fullBookDepth));

    final MDReqGrp mdReqGrp = createMdReqGrp(this.mdEntryTypeNeeds);
    request.set(mdReqGrp);
    final InstrmtMDReqGrp instrmtMDReqGrp = createInstrmtMdReqGrp(Collections.singletonMap("FakeCusip", SecurityIDSource.CUSIP));
    request.set(instrmtMDReqGrp);
    return request;
  }

  private InstrmtMDReqGrp createInstrmtMdReqGrp(Map<String, String> instruments) {
    final InstrmtMDReqGrp instrmtMDReqGrp = new InstrmtMDReqGrp();

    instruments.entrySet()
        .stream()
        .map(
            entry -> {
              final NoRelatedSym noRelatedSym = new NoRelatedSym();
              noRelatedSym.set(new SecurityID(entry.getKey()));
              noRelatedSym.set(new SecurityIDSource(entry.getValue()));
              return noRelatedSym;
            })
        .forEach(instrmtMDReqGrp::addGroup);

    return instrmtMDReqGrp;
  }

  private MDReqGrp createMdReqGrp(final Character... mdEntryTypes) {
    //TODO: Check that vararg contains only legal values or throw IllegalArgExc
    final MDReqGrp mdReqGrp = new MDReqGrp();
    final Set<NoMDEntryTypes> mdEntryTypesSet = createMdEntryTypes(mdEntryTypes);
    mdEntryTypesSet.forEach(mdReqGrp::addGroup);
    return mdReqGrp;
  }

  private Set<NoMDEntryTypes> createMdEntryTypes(final Character[] mdEntryTypes) {
    return Arrays.stream(mdEntryTypes)
        .map(m -> {
          final NoMDEntryTypes noMDEntryTypes = new NoMDEntryTypes();
          noMDEntryTypes.set(new MDEntryType(m));
          return noMDEntryTypes;
        })
        .collect(Collectors.toUnmodifiableSet());
  }

}
