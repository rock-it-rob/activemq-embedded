package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;
import rob.test.activemq.model.MessagePayload;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class MessagePayloadQueueListener implements ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadQueueListener.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    public void handleMessage(final MessagePayload messagePayload)
    {
        log.info(String.format("MessagePayload / Sent: %s Content: %s", sdf.format(messagePayload.getSent()), messagePayload.getContent()));
    }

    @Override
    public void handleError(Throwable t)
    {
        log.error("Problem handling message. Exception message is: " + t.getMessage());
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        log.error(sw.toString());
    }
}
