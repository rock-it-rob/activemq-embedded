package rob.test.activemq.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rob.test.activemq.BrokerConfig;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * @author rob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BrokerTest
{
    @Configuration
    @Import(BrokerConfig.class)
    private static class Config
    {
        @Autowired
        @Bean
        public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory)
        {
            SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory(connectionFactory);
            return new JmsTemplate(singleConnectionFactory);
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    /**
     * Sends a simple text message on the queue.
     */
    @Test
    public void testSend()
    {
        jmsTemplate.send(
            queue,
            (session) -> session.createTextMessage("testing 1 2 3")
        );
    }
}
