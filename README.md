# activemq-embedded

This project demonstrates using the Spring JMSTemplate and JMSListener
to send and receive JMS messages. The broker service is ActiveMQ and is
run as an embedded instance within the JVM.

Files of interest:

* **BrokerConfig**:
This spring `@Configuration` class creates the JMS beans and defines a 
message handler method for the default queue annotated with `@JmsListener`.

* **MessagePayload**:
A POJO message type.

* **MessagePayloadQueueListener**:
A class that handles JMS messages received of type `MessagePayload`.
 

* **DefaultQueueTest**:
A JUnit test class that sends a `TextMessage` using a `JmsTemplate` to 
the default queue.

* **MessagePayloadTest**:
A JUnit test class that creates an instance of `MessagePayload` and
sends it to a test queue.

## Running the Application

The project is run by executing the maven test goal:

`mvn test`

The result should look similar to the following:

<pre>
2019-06-03 15:54:01.385 [INFO] BrokerConfig: Received message: testing sample message
2019-06-03 15:54:01.627 [ERROR] MessageErrorHandler: Could not find type id property [classname] on message [ID:MTI-ATPLT12-65380-1559595240927-7:1:5:1:1] from destination [queue://messagePayloadQueue]
2019-06-03 15:54:01.698 [INFO] MessagePayloadQueueListener: MessagePayload / Sent: 2019-06-03 03:54:01.621 Content: Sample rob.test.activemq.model.MessagePayload
2019-06-03 15:54:07.397 [WARN] DefaultMessageListenerContainer: Setup of JMS message listener invoker failed for destination 'defaultQueue' - trying to recover. Cause: peer (vm://localhost#3) stopped.

Process finished with exit code 0
</pre>