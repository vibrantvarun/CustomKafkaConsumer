package PlainJson

import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.util.*


fun main(args: Array<String>) {
    SimpleProducer("localhost:9092").produce(2)
}

class SimpleProducer(brokers: String) {
    private val logger = LogManager.getLogger(javaClass)
    private val producer = createProducer(brokers)

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java.canonicalName
        props["value.serializer"] = StringSerializer::class.java.canonicalName
        return KafkaProducer<String, String>(props)

    }

    fun produce(ratePerSecond: Int) {
        /*    val waitTimeBetweenIterationMilliSeconds = 1000L / ratePerSecond
            val faker = Faker()
            while (true) {
                val fakePerson = Person(
                    firstName = faker.name().firstName(),
                    lastName = faker.name().lastName(),
                    birthDate = faker.date().birthday()

                )
                logger.info("Generated a person: $fakePerson")

                val fakePersonJson = jsonMapper.writeValueAsString(fakePerson)
                logger.debug("JSON data: $fakePersonJson")

                val futureResult = producer.send(ProducerRecord(personsTopic, fakePersonJson))
                logger.debug("Sent a record")

                Thread.sleep(waitTimeBetweenIterationMilliSeconds)

                // wait for the write acknowledgment
                futureResult.get()*/

        val key = "id_" + 1

        for (i in 0..4900) { // create a producer record
            val topic = "first_topic"
            val value = "hello world " + Integer.toString(i)
            val record =
                ProducerRecord(topic, key, value)
            logger.info("Key: $key")
            print("key is : $key")
            // log the key
            // id_0 is going to partition 1
// id_1 partition 0
// id_2 partition 2
// id_3 partition 0
// id_4 partition 2
// id_5 partition 2
// id_6 partition 0
// id_7 partition 2
// id_8 partition 1
// id_9 partition 2
// send data - asynchronous
            producer.send(record, object : Callback {
                override fun onCompletion(
                    recordMetadata: RecordMetadata,
                    e: Exception?
                ) { // executes every time a record is successfully sent or an exception is thrown
                    if (e == null) { // the record was successfully sent
                        logger.info(
                            "Received new metadata. \n" +
                                    "Topic:" + recordMetadata.topic() + "\n" +
                                    "Partition: " + recordMetadata.partition() + "\n" +
                                    "Offset: " + recordMetadata.offset() + "\n" +
                                    "Timestamp: " + recordMetadata.timestamp()
                        )
                        println("Received new metadata. \n" +
                                "Topic:" + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp())
                    } else {
                        logger.error("Error while producing", e)
                    }
                }
            }).get() // block the .send() to make it synchronous - don't do this in production!
        }

        // flush data
        // flush data
        producer.flush()
        // flush and close producer
        // flush and close producer
        producer.close()
    }
}
