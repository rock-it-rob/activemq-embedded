package rob.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.jms.Queue;
import javax.jms.Session;

/**
 * Runs an embedded ActiveMQ broker.
 *
 * @author rob
 */
@Configuration
@PropertySource(name = "broker", value = "classpath:broker.properties")
public class BrokerConfig
{
    @Value("${broker.url}")
    private String url;

    @Value("${broker.queuename}")
    private String queueName;

    @Bean
    public ActiveMQConnectionFactory connectionFactory()
    {
        return new ActiveMQConnectionFactory(url);
    }

    @Bean
    public Queue queue()
    {
        return new ActiveMQQueue(queueName);
    }
}
