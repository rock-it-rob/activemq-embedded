package rob.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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

    @Bean
    public ActiveMQConnectionFactory connectionFactory()
    {
        log.debug("Creating new connection factory at url: " + url);
        return new ActiveMQConnectionFactory(url);
    }

    @Bean
    public Queue queue()
    {
        log.debug("Creating new queue: " + queueName);
        return new ActiveMQQueue(queueName);
    }
}
