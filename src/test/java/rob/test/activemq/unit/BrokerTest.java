package rob.test.activemq.unit;

import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rob.test.activemq.BrokerConfig;

import javax.jms.Queue;

/**
 * @author rob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BrokerTest
{
    private static BrokerService brokerService;
    private static final String BROKER_NAME = "localhost";

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        brokerService = new BrokerService();
        brokerService.setBrokerName(BROKER_NAME);
        brokerService.start();
    }

    @AfterClass
    public static void afterClass() throws Exception
    {
        Thread.sleep(2000);
        brokerService.stop();
    }

    protected final static Logger log = LoggerFactory.getLogger(BrokerTest.class);

    @Configuration
    @Import(BrokerConfig.class)
    @EnableJms
    static class Config
    {
        @JmsListener(destination = "${queue.name}")
        public void jmsListener(Message<String> message)
        {
            log.info("Message received: " + message.getPayload());
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
                (session) -> session.createTextMessage("testing sample message")
        );
    }
}
