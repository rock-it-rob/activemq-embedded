package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.adapter.ReplyFailureException;
import rob.test.activemq.model.MessagePayload;
import rob.test.activemq.model.MessagePayloadResponse;

import javax.jms.InvalidDestinationException;

/**
 * MessagePayloadResponseListener provides the same functionality as its superclass, {@link MessagePayloadListener}
 * except that it provides a response to the incoming message.
 *
 * @author Rob Benton
 */
public class MessagePayloadResponseListener extends MessagePayloadListener
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadResponseListener.class);

    private static final String RESPONSE = "ok";

    public MessagePayloadResponse handleMessageWithResponse(final MessagePayload messagePayload)
    {
        handleMessage(messagePayload);
        final MessagePayloadResponse response = new MessagePayloadResponse();
        response.setResponse(RESPONSE);

        return response;
    }

    @Override
    public void handleError(Throwable t)
    {
        if (t instanceof ReplyFailureException)
        {
            log.warn("Unable to reply to message: " + t.getMessage());
        }
        else
        {
            super.handleError(t);
        }
    }
}
