# activemq-embedded

This project demonstrates using the Spring JMSTemplate and JMSListener
to send and receive JMS messages. The broker service is ActiveMQ and is
run as an embedded instance within the JVM.

Files of interest:

* **BrokerConfig**:
This spring `@Configuration` class creates beans of `JmsListenerContainerFactory`
and `JmsTemplate`. It also defines a message handler method annotated
with `@JmsListener`.

* **BrokerTest**:
A JUnit test class that sends a message using the `JmsTemplate` bean.

* **broker-test-spring.xml**:
A spring configuration xml that defines beans for an embedded ActiveMQ
broker service, a connection factory, and a queue.

## Running the Application

The project is run by executing the maven test goal:

`mvn test`

The single JUnit test in `BrokerTest` will use the `JMSTemplate` bean
to send a sample message and the listener method defined in `BrokerConfig`
will print its contents out using logging.

The result should look similar to the following:

<pre>
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running rob.test.activemq.unit.BrokerTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.332 sec
2019-05-31 08:12:25.972 [INFO] BrokerConfig: Received message: testing sample message

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
</pre>