package rob.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * Runs an embedded ActiveMQ broker.
 *
 * @author rob
 */
@Configuration
@PropertySource(name = "broker", value = "classpath:broker.properties")
public class BrokerConfig
{
    private static final Logger log = LoggerFactory.getLogger(BrokerConfig.class);

    @Value("${broker.url}")
    private String url;

    @Value("${queue.name}")
    private String queueName;

    /**
     * Default MQ connection factory.
     *
     * @return ConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory()
    {
        log.debug("Creating new connection factory at url: " + url);
        return new CachingConnectionFactory(new ActiveMQConnectionFactory(url));
    }

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
     * Default message queue.
     *
     * @return Queue
     */
    @Bean
    public Queue queue()
    {
        log.debug("Creating new queue: " + queueName);
        return new ActiveMQQueue(queueName);
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
}
