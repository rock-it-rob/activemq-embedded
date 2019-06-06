package rob.test.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;

import javax.annotation.PostConstruct;

/**
 * BrokerConfig is a spring configuration class that sets up the message
 * producers and message consumers. Users of this class should provide, in the
 * application context, a ConnectionFactory and a Queue.
 *
 * @author rob
 */
@Configuration
@ImportResource(BrokerConfig.SPRING_XML)
@PropertySource(BrokerConfig.PROPERTIES)
@EnableJms
public class BrokerConfig
{
    static final String SPRING_XML = "classpath:broker-spring.xml";
    static final String PROPERTIES = "classpath:application.properties";

    private static final Logger log = LoggerFactory.getLogger(BrokerConfig.class);

    @Value("${broker.url}")
    private String brokerUrl;

    @Value("${broker.name}")
    private String brokerName;

    @Value("${default.queue}")
    private String defaultQueueName;

    @PostConstruct
    private void postConstruct()
    {
        final String conInfo = String.format("Connected to broker %s @ %s", brokerName, brokerUrl);
        log.info(conInfo);
        log.info("Default queue name is: " + defaultQueueName);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Message handler.
     *
     * @param message Message<String>
     */
    @JmsListener(destination = "${default.queue}")
    public void onMessage(Message<String> message)
    {
        log.info("Received message: " + message.getPayload());
    }
}
