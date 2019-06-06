package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.ErrorHandler;

import javax.jms.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractMessageListener<M, R> implements SessionAwareMessageListener, ErrorHandler
{
    private MessageConverter messageConverter;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(Message message, Session session) throws JMSException
    {
        final M m = (M) messageConverter.fromMessage(message);
        receiveMessage(m);

        final Destination destination = message.getJMSReplyTo();
        if (destination != null)
        {
            final R r = generateReply(m);
            MessageProducer producer = session.createProducer(destination);
            final Message response = messageConverter.toMessage(r, session);
            producer.send(response);
        }
    }

    @Override
    public void handleError(Throwable t)
    {
        log.error("Unable to process message: " + t.getMessage());
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        log.error(sw.toString());
    }

    protected abstract void receiveMessage(M message);

    protected abstract R generateReply(M message);

    public void setMessageConverter(MessageConverter messageConverter)
    {
        this.messageConverter = messageConverter;
    }
}
