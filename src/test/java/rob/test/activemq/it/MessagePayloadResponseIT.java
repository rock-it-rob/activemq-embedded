package rob.test.activemq.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rob.test.activemq.BrokerConfig;
import rob.test.activemq.model.MessagePayload;
import rob.test.activemq.model.MessagePayloadResponse;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(MessagePayloadResponseIT.TEST_PROPERTIES)
public class MessagePayloadResponseIT
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadResponseIT.class);

    static final String TEST_PROPERTIES = "classpath:test.properties";

    @Configuration
    @Import(BrokerConfig.class)
    @ImportResource(Config.SPRING_XML)
    @PropertySource(Config.PROPERTIES)
    static class Config
    {
        static final String SPRING_XML = "classpath:messagePayload-test-spring.xml";
        static final String PROPERTIES = "classpath:queue.properties";
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue messagePayloadResponseQueue;

    @Autowired
    private Queue messagePayloadQueue;

    @Autowired
    private Queue messagePayloadOptionalResponseQueue;

    private MessagePayload messagePayload;

    @Before
    public void before()
    {
        messagePayload = new MessagePayload();
        messagePayload.setSent(new Date());
    }

    @Test
    public void convertAndSend() throws JMSException
    {
        messagePayload.setContent("Expecting response");
        final Message response = jmsTemplate.sendAndReceive(messagePayloadResponseQueue,
                (session) ->
                        jmsTemplate.getMessageConverter().toMessage(messagePayload, session)
        );
        log.info("Response: " + parseResponse(response));
    }

    @Test
    public void sendWithNoReplyTo()
    {
        messagePayload.setContent("This message has no reply-to");
        jmsTemplate.convertAndSend(messagePayloadResponseQueue, messagePayload);
    }

    @Test
    public void sendExpectingResponseButGettingNone()
    {
        messagePayload.setContent("Expecting response but won't get one.");
        final Message response = jmsTemplate.sendAndReceive(messagePayloadQueue,
                (session) ->
                        jmsTemplate.getMessageConverter().toMessage(messagePayload, session)
        );
        // response will be null.
        assertNull(response);
    }

    @Test
    public void retrieveOptionalResponse() throws JMSException
    {
        messagePayload.setContent("Retrieving optional response");
        final Message response = jmsTemplate.sendAndReceive(
                messagePayloadOptionalResponseQueue,
                (session) -> jmsTemplate.getMessageConverter().toMessage(messagePayload, session)
        );
        assertNotNull(response);
        log.info("Optional response: " + parseResponse(response));
    }

    @Test
    public void ignoreOptionalResponse()
    {
        messagePayload.setContent("Ignoring optional response");
        jmsTemplate.convertAndSend(messagePayloadOptionalResponseQueue, messagePayload);
    }

    private String parseResponse(Message message) throws JMSException
    {
        final MessagePayloadResponse response = (MessagePayloadResponse) jmsTemplate.getMessageConverter().fromMessage(message);

        return response.getResponse();
    }
}
