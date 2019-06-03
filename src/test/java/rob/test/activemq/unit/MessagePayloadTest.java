package rob.test.activemq.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rob.test.activemq.BrokerConfig;
import rob.test.activemq.model.MessagePayload;

import javax.jms.Queue;
import java.util.Date;

/**
 * MessagePayloadTest tests sending strongly-typed objects of {@link rob.test.activemq.model.MessagePayload}
 * over a queue.
 *
 * @author Rob Benton
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class MessagePayloadTest
{
    private static final Logger log = LoggerFactory.getLogger(MessagePayloadTest.class);

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
    @Qualifier("messagePayloadQueue")
    private Queue queue;

    @Test
    public void testConvertAndSend() throws Exception
    {

        final MessagePayload messagePayload = new MessagePayload();
        messagePayload.setSent(new Date());
        messagePayload.setContent("Sample " + messagePayload.getClass().getName());
        jmsTemplate.convertAndSend(queue, messagePayload);
    }

    @Test
    public void testBadMessage()
    {
        jmsTemplate.send(queue, (session) -> session.createTextMessage("This is a bad message."));
    }
}
