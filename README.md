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

## Running the Application Tests

### Setup

The tests require a running ActiveMQ broker running on port 61616. This
can be setup by using the included `docker-compose.yml` file if docker
is installed. Or by using the maven profile `docker` when executing
the verify goal.

### Test Execution

Using docker-compose:

`docker-compose up -d`

`mvn verify`

Using the docker maven plugin:

`mvn -Pdocker verify`
