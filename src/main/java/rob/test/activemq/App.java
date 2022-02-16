package rob.test.activemq;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App
{
    public static void main(String[] args)
    {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BrokerConfig.class);

        while (true)
        {
            // wait for a term
        }
    }
}
