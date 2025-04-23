package br.com.ntsfixclient.app;

import lombok.extern.slf4j.Slf4j;
import quickfix.*;

@Slf4j
public class FixApplication implements Application {
    
    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("Logon successful for session: {}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("Logout for session: {}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        if (message.isAdmin()) {
            log.info("Sending admin message: {}", message);
        }
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.info("Admin message received: {}", message);
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        log.info("Sending application message: {}", message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Application message received: {}", message);
    }

    public void onMessage(quickfix.fix50sp2.ExecutionReport message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Received ExecutionReport - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
    }

}