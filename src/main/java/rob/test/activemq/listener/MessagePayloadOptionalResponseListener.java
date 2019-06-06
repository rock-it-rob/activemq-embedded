package rob.test.activemq.listener;

import org.springframework.jms.support.converter.MessageConverter;
import rob.test.activemq.model.MessagePayload;
import rob.test.activemq.model.MessagePayloadResponse;

/**
 * MessagePayloadOptionalResponseListener provides a listener with similar functionality to {@link MessagePayloadResponseListener}
 * but if the incoming message does not have a 'reply-to' header then no response will be sent.
 *
 * @author Rob Benton
 */
public class MessagePayloadOptionalResponseListener extends AbstractMessageListener<MessagePayload, MessagePayloadResponse>
{
    public MessagePayloadOptionalResponseListener(MessageConverter messageConverter)
    {
        setMessageConverter(messageConverter);
    }

    @Override
    protected void receiveMessage(MessagePayload message)
    {
        log.info("Received payload: " + message.toString());
    }

    @Override
    protected MessagePayloadResponse generateReply(MessagePayload message)
    {
        final MessagePayloadResponse response = new MessagePayloadResponse();
        response.setResponse("responding to: " + message.getContent());

        return response;
    }
}
