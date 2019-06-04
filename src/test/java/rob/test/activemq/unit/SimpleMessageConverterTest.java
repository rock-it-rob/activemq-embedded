package rob.test.activemq.unit;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rob.test.activemq.BrokerConfig;
import rob.test.activemq.listener.MessagePayloadQueueListener;
import rob.test.activemq.model.MessagePayload;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import java.util.Date;

/**
 * SimpleMessageConverterTest uses a {@link rob.test.activemq.model.MessagePayload}
 * instance to send over JMS. In comparison to the {@link MessagePayloadTest} it uses a {@link org.springframework.jms.support.converter.SimpleMessageConverter}
 * to perform the message conversion rather than JSON.
 *
 * @author Rob Benton
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class SimpleMessageConverterTest
{
    @Configuration
    @Import({BrokerConfig.class, MessagePayloadQueueListener.class})
    @PropertySource(Config.PROPERTIES)
    static class Config
    {
        static final String PROPERTIES = "classpath:queue.properties";

        @Bean
        public Queue queue(@Value("${messagePayload.queue}") String queueName)
        {
            return new ActiveMQQueue(queueName);
        }

        @Bean
        @Autowired
        public MessageListenerAdapter messageListenerAdapter(MessagePayloadQueueListener messagePayloadQueueListener)
        {
            return new MessageListenerAdapter(messagePayloadQueueListener);
        }

        @Bean
        @Autowired
        public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, Queue queue, MessageListenerAdapter messageListenerAdapter)
        {
            final DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setDestination(queue);
            container.setMessageListener(messageListenerAdapter);

            return container;
        }

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
    private Queue queue;

    /**
     * In order for this test to work special configuration is necessary for ActiveMQ.
     * see: http://activemq.apache.org/objectmessage.html
     */
    @Ignore
    @Test
    public void testConvertAndSend()
    {
        final MessagePayload messagePayload = new MessagePayload();
        messagePayload.setSent(new Date());
        messagePayload.setContent("Message sent using SimpleMessageConverter.");
        jmsTemplate.convertAndSend(queue, messagePayload);
    }
}
