import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import groovy.json.JsonOutput
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

// 🔹 Generate data random
def timestamp = System.currentTimeMillis()
def nameList = ['Tonny Wijaya', 'Dewi Lestari', 'Budi Santoso', 'Siti Aminah']
def genderList = ['male', 'female']
def name = nameList[new Random().nextInt(nameList.size())]
def gender = genderList[new Random().nextInt(genderList.size())]
def email = name.toLowerCase().replaceAll(" ", ".") + "." + timestamp + "@mail.com"
def status = "active"

// 🔹 Buat payload JSON
def payload = [
	name: name,
	gender: gender,
	email: email,
	status: status
]
def jsonBody = JsonOutput.toJson(payload)

// 🔹 Ambil object POST dari repository
TestObject postUserObj = findTestObject('Object Repository/API_Endpoints/Post User')

// 🔹 Inject body JSON ke object
postUserObj.setBodyContent(new HttpTextBodyContent(jsonBody, "UTF-8", "application/json"))

// 🔹 Kirim request
def response = WS.sendRequest(postUserObj)

// 🔹 Verifikasi status code
WS.verifyResponseStatusCode(response, 201)

// 🔹 Cetak hasil
println "User created: ${name} | ${email} | ${gender}"
