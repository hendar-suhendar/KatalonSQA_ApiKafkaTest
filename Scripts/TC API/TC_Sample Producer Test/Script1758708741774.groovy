import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import groovy.json.JsonSlurper

// Ambil object GET dari repository
def getUserObj = findTestObject('Object Repository/API_Endpoints/List User')

// Kirim request
def response = WS.sendRequest(getUserObj)

// Verifikasi status code
WS.verifyResponseStatusCode(response, 200)

// Parse response JSON
def actualUsers = new JsonSlurper().parseText(response.getResponseText())

// Expected data
def expectedUsers = [
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

// Verifikasi satu per satu
for (int i = 0; i < expectedUsers.size(); i++) {
    def expected = expectedUsers[i]
    def actual = actualUsers.find { it.id == expected.id }

    assert actual != null : "User ID ${expected.id} not found in response"
    assert actual.name == expected.name : "Name mismatch for ID ${expected.id}"
    assert actual.email == expected.email : "Email mismatch for ID ${expected.id}"
    assert actual.gender == expected.gender : "Gender mismatch for ID ${expected.id}"
    assert actual.status == expected.status : "Status mismatch for ID ${expected.id}"

    println "Verified user: ${expected.name} (${expected.email})"
}
