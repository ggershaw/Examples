package org.gershaw.quickfixj.springboot.server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import io.allune.quickfixj.spring.boot.starter.model.FromApp;
import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.gershaw.quickfixj.springboot.client.component.MarketDataRequestor;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import quickfix.Acceptor;
import quickfix.FixVersions;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MDEntryType;
import quickfix.field.SecurityIDSource;
import quickfix.fix50sp2.MarketDataRequest;

@ExtendWith(MockitoExtension.class)
class MarketDataServiceTest {

  @Mock
  private QuickFixJTemplate quickFixJTemplate;
  @Mock
  private Acceptor acceptor;
  private MarketDataService marketDataService;

  @BeforeEach
  void setUp() {
    marketDataService = new MarketDataService(quickFixJTemplate, acceptor, 100,
        TimeUnit.MILLISECONDS);
  }

  @Test
  @SneakyThrows
  @Disabled
  void handle() {
    try (MockedStatic<Utils> dummy = Mockito.mockStatic(Utils.class)) {
      final FromApp fromApp = createFromApp();
      marketDataService.handle(fromApp);
      Thread.sleep(105);
      dummy.verify(() -> Utils.send(eq(quickFixJTemplate), eq(acceptor), any(Message.class)),
          times(1));
      marketDataService.stop();
    }
  }

  private static FromApp createFromApp() {
    final MarketDataRequest marketDataRequest = MarketDataRequestor.of(
        new Character[]{MDEntryType.OFFER},
        Map.of("ISIN1", SecurityIDSource.ISIN_NUMBER));

    return FromApp.of(marketDataRequest, new SessionID(
        FixVersions.BEGINSTRING_FIXT11, "sender", "target"));
  }
}