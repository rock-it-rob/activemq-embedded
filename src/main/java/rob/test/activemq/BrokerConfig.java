package rob.test.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;

import javax.jms.ConnectionFactory;

/**
 * BrokerConfig is a spring configuration class that sets up the message
 * producers and message consumers. Users of this class should provide, in the
 * application context, a ConnectionFactory and a Queue.
 *
 * @author rob
 */
@Configuration
@EnableJms
public class BrokerConfig
{
    private static final Logger log = LoggerFactory.getLogger(BrokerConfig.class);

    /**
     * Default message producer.
     *
     * @param connectionFactory ConnectionFactory
     * @return JmsTemplate
     */
    @Autowired
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory)
    {
        return new JmsTemplate(connectionFactory);
    }

    /**
     * Default listener container factory
     *
     * @param connectionFactory autowired ConnectionFactory
     * @return JmsListenerContainerFactory
     */
    @Autowired
    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory)
    {
        DefaultJmsListenerContainerFactory cf = new DefaultJmsListenerContainerFactory();
        cf.setConnectionFactory(connectionFactory);
        cf.setSessionTransacted(true);
        cf.setConcurrency("3-10");
        return cf;
    }

    /**
     * Message handler.
     *
     * @param message Message<String>
     */
    @JmsListener(destination = "${broker.queue}")
    public void onMessage(Message<String> message)
    {
        log.info("Received message: " + message.getPayload());
    }
}
