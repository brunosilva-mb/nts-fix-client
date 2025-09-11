package br.com.ntsfixclient.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import quickfix.*;

@Slf4j
@RequiredArgsConstructor
public abstract class FixService {

  private final Initiator initiator;
  private final SessionSettings settings;
  private SessionID sessionID;

  @SneakyThrows
  public void send(Message message) {
    sessionID = new SessionID("FIXT.1.1", settings.getString("SenderCompID"), settings.getString("TargetCompID"));
    Session session = Session.lookupSession(sessionID);
    if (session != null && session.isLoggedOn()) {
      session.send(message);
      log.info("Message sent: {}", message.toString().replace('\u0001', '|'));
    } else {
      log.warn("Session not active. Cannot send message.");
    }
  }

  @PreDestroy
  public void destroySession() {
    if (initiator != null && initiator.isLoggedOn()) {
      Session session = Session.lookupSession(sessionID);
      if (session != null) {
        session.logout("User requested logout");
      }
      initiator.stop();
      log.info("FIX Client stopped.");
    }
  }

}
