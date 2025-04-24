package br.com.ntsfixclient.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.HeartBtInt;
import quickfix.field.MsgType;

@Component
public class FixMessageHandler extends MessageCracker implements Application {

    private static final Logger log = LoggerFactory.getLogger(FixMessageHandler.class);

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("onCreate: {}", sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("onLogon: {}", sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.info("onLogout: {}", sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        try {
            String msgTypeValue = message.getHeader().getField(new MsgType()).getValue();
            if (MsgType.HEARTBEAT.equals(msgTypeValue)) {
                log.debug("Sending Heartbeat message to {}: {}", sessionID.getTargetCompID(), message.toString().replace('\u0001', '|'));
            } else {
                log.info("ToAdmin - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
            }
        } catch (FieldNotFound e) {
            log.error("Could not find MsgType in admin message: {}", message.toString().replace('\u0001', '|'), e);
        }
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        try {
            String msgTypeValue = message.getHeader().getField(new MsgType()).getValue();
            if (MsgType.HEARTBEAT.equals(msgTypeValue)) {
                log.debug("Received Heartbeat message from {}: {}", sessionID.getTargetCompID(), message.toString().replace('\u0001', '|'));
            } else {
                log.info("FromAdmin - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
            }
        } catch (FieldNotFound e) {
            log.error("Could not find MsgType in admin message: {}", message.toString().replace('\u0001', '|'), e);
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        log.info("ToApp - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("FromApp - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
        crack(message, sessionID);
    }

    // Example of handling an ExecutionReport (you can add other handlers here)
    public void onMessage(quickfix.fix50sp2.ExecutionReport message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Received ExecutionReport - {}: {}", sessionID, message.toString().replace('\u0001', '|'));
        // Here you can process the details of the ExecutionReport
    }
}