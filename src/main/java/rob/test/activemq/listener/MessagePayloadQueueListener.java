package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rob.test.activemq.model.MessagePayload;

import java.text.SimpleDateFormat;

public class MessagePayloadQueueListener
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadQueueListener.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    public void handleMessage(final MessagePayload messagePayload)
    {
        log.info(String.format("MessagePayload / Sent: %s Content: %s", sdf.format(messagePayload.getSent()), messagePayload.getContent()));
    }
}