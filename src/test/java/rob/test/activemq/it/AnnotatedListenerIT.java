package rob.test.activemq.it;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rob.test.activemq.BrokerConfig;
import rob.test.activemq.model.MessagePayload;

import javax.jms.ConnectionFactory;
import javax.validation.Valid;
import java.util.Date;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(AnnotatedListenerIT.TEST_PROPERTIES)
public class AnnotatedListenerIT
{
    private static final Logger log = LoggerFactory.getLogger(AnnotatedListenerIT.class);

    static final String TEST_PROPERTIES = "classpath:test.properties";

    @Configuration
    @Import(BrokerConfig.class)
    @PropertySource(Config.PROPERTIES)
    @EnableJms
    static class Config
    {
        static final String PROPERTIES = "classpath:queue.properties";

        @Value("${annotatedListener.queue}")
        private String queueName;

        @Autowired
        private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;

        @JmsListener(destination = "${annotatedListener.queue}")
        public void annotatedListener(@Valid MessagePayload messagePayload)
        {
            log.info("Got a message: " + messagePayload.getContent());
        }

        @Bean
        @Autowired
        public JmsTemplate testTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter)
        {
            JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
            jmsTemplate.setMessageConverter(messageConverter);
            jmsTemplate.setDefaultDestinationName(queueName);

            return jmsTemplate;
        }
    }

    @Autowired
    private JmsTemplate testTemplate;

    private MessagePayload messagePayload;

    @Before
    public void before()
    {
        messagePayload = new MessagePayload();
        messagePayload.setSent(new Date());
    }

    @AfterClass
    public static void afterClass() throws InterruptedException
    {
        Thread.sleep(2000);
    }

    @Test
    public void send()
    {
        messagePayload.setContent("Just some stuff.");
        testTemplate.convertAndSend(messagePayload);
    }

    @Ignore
    @Test
    public void sendBadContet()
    {
        testTemplate.convertAndSend("bad message");
    }

    @Test
    public void sendInvalidPayload()
    {
        messagePayload.setSent(null);
        messagePayload.setContent("This is an invalid object");
        testTemplate.convertAndSend(messagePayload);
    }
}
