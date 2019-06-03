package rob.test.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class MessageErrorHandler implements ErrorHandler
{
    private static final Logger log = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public void handleError(Throwable t)
    {
        log.error(t.getMessage());
    }
}
