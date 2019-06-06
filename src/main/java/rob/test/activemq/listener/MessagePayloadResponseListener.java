package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rob.test.activemq.model.MessagePayload;
import rob.test.activemq.model.MessagePayloadResponse;

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
}
