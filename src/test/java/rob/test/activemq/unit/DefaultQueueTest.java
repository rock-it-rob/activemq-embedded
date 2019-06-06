package rob.test.activemq.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rob.test.activemq.BrokerConfig;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * DefaultQueueTest tests sending plain text messages to the default message queue.
 *
 * @author Rob Benton
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(DefaultQueueTest.TEST_PROPERTIES)
public class DefaultQueueTest
{
    private static final Logger log = LoggerFactory.getLogger(DefaultQueueTest.class);

    static final String TEST_PROPERTIES = "classpath:test.properties";

    @Configuration
    @Import(BrokerConfig.class)
    static class Config
    {
        @Bean
        @Autowired
        public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory)
        {
            return new JmsTemplate(connectionFactory);
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("defaultQueue")
    private Queue queue;

    /**
     * Sends a simple text message on the queue.
     */
    @Test
    public void testSend()
    {
        jmsTemplate.send(
                queue,
                (session) -> session.createTextMessage("testing sample message")
        );
    }
}
