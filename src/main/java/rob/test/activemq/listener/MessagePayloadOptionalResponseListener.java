package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.ErrorHandler;
import rob.test.activemq.model.MessagePayload;
import rob.test.activemq.model.MessagePayloadResponse;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * MessagePayloadOptionalResponseListener provides a listener with similar functionality to {@link MessagePayloadResponseListener}
 * but if the incoming message does not have a 'reply-to' header then no response will be sent.
 *
 * @author Rob Benton
 */
public class MessagePayloadOptionalResponseListener implements MessageListener, ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadOptionalResponseListener.class);

    @Autowired
    @Qualifier("messagePayloadResponseJmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public void onMessage(Message message) //throws JMSException
    {
        try
        {
            final MessagePayload messagePayload = convertIncomingMessage(message);
            log.info("Got MessagePayload | Sent: " + messagePayload.getSent() + " Content: " + messagePayload.getContent());
            final Destination destination = message.getJMSReplyTo();
            log.info("Reply-To: " + destination);

            if (destination != null)
            {
                respond(destination);
            }
        }
        catch (JMSException e)
        {
            throw new RuntimeException(e);
        }
    }

    private MessagePayload convertIncomingMessage(final Message message) throws JMSException
    {
        return (MessagePayload) messageConverter.fromMessage(message);
    }

    private void respond(Destination destination)
    {
        final MessagePayloadResponse response = new MessagePayloadResponse();
        response.setResponse("ok");
        jmsTemplate.convertAndSend(destination, response);
    }

    @Override
    public void handleError(Throwable t)
    {
        log.error("Problem: " + t.getMessage());
    }
}
