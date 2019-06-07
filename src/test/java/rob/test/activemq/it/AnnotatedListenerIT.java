package rob.test.activemq.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rob.test.activemq.BrokerConfig;
import rob.test.activemq.model.MessagePayload;

import javax.jms.ConnectionFactory;
import java.util.Date;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(AnnotatedListenerIT.TEST_PROPERTIES)
public class AnnotatedListenerIT
{
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

        @JmsListener(destination = "${annotatedListener.queue}")
        public void annotatedListener(@Payload MessagePayload messagePayload)
        {
            System.out.println("Got a message: " + messagePayload.getContent());
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

    @Test
    public void send()
    {
        messagePayload.setContent("Just some stuff.");
        testTemplate.convertAndSend(messagePayload);
    }
}
