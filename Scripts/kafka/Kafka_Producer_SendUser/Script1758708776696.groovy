import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties
import groovy.json.JsonOutput

// Setup Kafka Producer
Properties props = new Properties()
props.put("bootstrap.servers", "localhost:9092")
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

KafkaProducer<String, String> producer = new KafkaProducer<>(props)

// Generate user data
def timestamp = System.currentTimeMillis()
def user = [
    id: timestamp,
    name: "KafkaUser_" + timestamp,
    email: "kafka.user.${timestamp}@mail.com",
    status: "active"
]
def message = JsonOutput.toJson(user)

// Kirim ke topic
ProducerRecord<String, String> record = new ProducerRecord<>("user-topic", message)
producer.send(record)
producer.close()

println "passed: Kafka Producer sent → ${message}"
