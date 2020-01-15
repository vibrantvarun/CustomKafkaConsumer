package PlainJson

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.time.Duration
import java.util.*


fun main(args: Array<String>) {
    SimpleConsumer("localhost:9092").process()
}

class SimpleConsumer(brokers: String) {
    private val logger = LogManager.getLogger(javaClass)
    //private val producer= createProducer(brokers)
    private val consumer = createConsumer(brokers)

    /* private fun createProducer(brokers: String): Producer<String, String> {
          val props = Properties()
          props["bootstrap.servers"] = brokers
          props["key.serializer"] = StringSerializer::class.java.canonicalName
          props["value.serializer"] = StringSerializer::class.java.canonicalName
          return KafkaProducer<String, String>(props)

      }*/

    
    private fun createConsumer(brokers: String): Consumer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        //props["group.id"]="person-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = StringDeserializer::class.java
        props["auto.offset.reset"] = "earliest"
        return KafkaConsumer<String, String>(props)
    }

    fun process() {
        //consumer.subscribe(listOf(personsTopic))
        /*    val tp = TopicPartition(personsTopic,0)
            consumer.assign(listOf(tp))
            logger.info("Consuming and processing data")

            while(true){

                val pos=consumer.seek(tp,0)

                logger.info("Received ${pos} is the position ")
                val records=consumer.poll(Duration.ofSeconds(1))


                logger.info("Received ${records.count()} records")

                records.iterator().forEach {
                    val personjson= it.value()
                    logger.debug("JSON data: $personjson")

                    val person= jsonMapper.readValue(personjson, Person::class.java)
                    logger.debug("model.Person: $person")

                    println("${person.birthDate},${person.lastName},${pos}")
                    val birthDateLocal= person.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

                    val age= Period.between(birthDateLocal, LocalDate.now()).years

                    logger.debug("Age: $age")

                    val future = producer.send(ProducerRecord(agesTopic, "${person.firstName} ${person.lastName}", "$age"))
                    future.get()
                }
            }*/

        val partitionToReadFrom = TopicPartition("first_topic", 0)
        val partitionToReadFrom1= TopicPartition("first_topic", 1)
        val partitionToReadFrom2= TopicPartition("first_topic", 2)
        val partitionToReadFrom3= TopicPartition("first_topic", 3)
        val offsetToReadFrom = 12260L
        consumer.assign(Arrays.asList(partitionToReadFrom,partitionToReadFrom1,partitionToReadFrom2,partitionToReadFrom3))

        // seek
        // seek
        consumer.seek(partitionToReadFrom3, offsetToReadFrom)

        val numberOfMessagesToRead = 500
        var keepOnReading = true
        var numberOfMessagesReadSoFar = 0

        // poll for new data
        // poll for new data
        while (keepOnReading) {
            val records =
                consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
               // numberOfMessagesReadSoFar += 1
                logger.info("Key: " + record.key() + ", Value: " + record.value())
                logger.info("Partition: " + record.partition() + ", Offset:" + record.offset())
                println("Key: " + record.key() + ", Value: " + record.value())
                println("Partition: " + record.partition() + ", Offset:" + record.offset())
               /* if (numberOfMessagesReadSoFar >= numberOfMessagesToRead) {*/
               /*     keepOnReading = false*/
               /*     break */
               /* }*/

                if(record.partition()==1) {
                    numberOfMessagesReadSoFar += 1
                    if (numberOfMessagesReadSoFar >= numberOfMessagesToRead) {
                        keepOnReading = false
                        break
                    }
                }
            }
        }

        logger.info("Exiting the application")

    }
}