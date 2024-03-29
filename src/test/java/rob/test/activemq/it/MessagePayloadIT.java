package rob.test.activemq.it;

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

import javax.jms.Queue;
import java.util.Date;

/**
 * MessagePayloadIT tests sending strongly-typed objects of {@link rob.test.activemq.model.MessagePayload}
 * over a queue.
 *
 * @author Rob Benton
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(MessagePayloadIT.TEST_PROPERTIES)
public class MessagePayloadIT
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadIT.class);

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
    private Queue messagePayloadQueue;

    @Test
    public void testConvertAndSend()
    {
        final MessagePayload messagePayload = new MessagePayload();
        messagePayload.setSent(new Date());
        messagePayload.setContent("Sample " + messagePayload.getClass().getName());
        jmsTemplate.convertAndSend(messagePayloadQueue, messagePayload);
    }

    @Test
    public void testBadMessage()
    {
        jmsTemplate.send(messagePayloadQueue, (session) -> session.createTextMessage("This is a bad message."));
    }
}
