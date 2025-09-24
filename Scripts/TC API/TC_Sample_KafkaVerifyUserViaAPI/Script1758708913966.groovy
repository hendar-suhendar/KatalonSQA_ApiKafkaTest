import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import groovy.json.JsonSlurper
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.Properties
import java.time.Duration

// Step 1: Ambil data dari API
def getUserObj = findTestObject('API_Endpoints/List User')
def response = WS.sendRequest(getUserObj)
WS.verifyResponseStatusCode(response, 200)

def actualUsers = new JsonSlurper().parseText(response.getResponseText())

// Step 2: Ambil data dari Kafka atau gunakan mock
def useKafka = false // ubah ke true jika Kafka tersedia
def kafkaUsers = []

if (useKafka) {
    Properties props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "verify-api-group")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)
    consumer.subscribe(["user-topic"])

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5))
    for (ConsumerRecord<String, String> record : records) {
        def json = new JsonSlurper().parseText(record.value())
        kafkaUsers << json
    }
    consumer.close()
} else {
    // Simulasi Kafka data
    kafkaUsers = [
        [id:8145378, name:"Rep. Tejas Kaniyar", email:"rep_kaniyar_tejas@legros.example", gender:"male", status:"inactive"],
        [id:8145377, name:"Shwet Devar", email:"devar_shwet@schaden.test", gender:"female", status:"active"],
        [id:8145376, name:"Rakesh Bhattacharya PhD", email:"bhattacharya_phd_rakesh@kunde-runolfsdottir.test", gender:"male", status:"inactive"],
        [id:8145375, name:"Amritambu Banerjee DDS", email:"banerjee_amritambu_dds@fadel-lang.test", gender:"female", status:"active"],
        [id:8145374, name:"Adityanandana Mehrotra", email:"adityanandana_mehrotra@kozey.example", gender:"female", status:"active"],
        [id:8145373, name:"Anish Shah", email:"anish_shah@cremin.example", gender:"female", status:"inactive"],
        [id:8145372, name:"Miss Jagdeep Bhattacharya", email:"bhattacharya_miss_jagdeep@kunze.example", gender:"female", status:"inactive"],
        [id:8145371, name:"Tej Malik", email:"malik_tej@rowe.example", gender:"male", status:"inactive"],
        [id:8145370, name:"Trisha Marar", email:"trisha_marar@deckow-cartwright.example", gender:"male", status:"inactive"],
        [id:8145369, name:"Durga Johar", email:"johar_durga@olson.example", gender:"male", status:"active"]
    ]
}

// Step 3: Verifikasi Kafka ke API
for (def expected : kafkaUsers) {
    def actual = actualUsers.find { it.id == expected.id }

    assert actual != null : "User ID ${expected.id} not found in API response"
    assert actual.name == expected.name : "Name mismatch for ID ${expected.id}"
    assert actual.email == expected.email : "Email mismatch for ID ${expected.id}"
    assert actual.gender == expected.gender : "Gender mismatch for ID ${expected.id}"
    assert actual.status == expected.status : "Status mismatch for ID ${expected.id}"

    println "passed: verified user ${expected.name} (${expected.email})"
}
