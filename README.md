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