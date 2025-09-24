import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.Properties
import java.time.Duration
import groovy.json.JsonSlurper

// Setup Kafka Consumer
Properties props = new Properties()
props.put("bootstrap.servers", "localhost:9092")
props.put("group.id", "test-group")
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("auto.offset.reset", "earliest")

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)
consumer.subscribe(["user-topic"])

// Poll data
ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5))
for (ConsumerRecord<String, String> record : records) {
    def json = new JsonSlurper().parseText(record.value())
    println "passed: Kafka Consumer received → ${json.name} (${json.email})"
}
consumer.close()
