package googleAPI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import files.ReusableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class ReadPayloadFromFile {
	
	/**
	 * Given---> all inputs When ---> Submit API-- resources, http methods Then --->
	 * validate the response
	 */

String response=null;
String placeId=null;
String newAddress="70 winter walk, USA";

@BeforeMethod
public void baseURI() {
	
	RestAssured.baseURI = "https://rahulshettyacademy.com";
}

	/**
	 *  POST Request
	 * @throws IOException 
	 */
@Test(priority = 1)
public void postPlace() throws IOException {
	response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
			.body(new String(Files.readAllBytes(Paths.get("./Contents/googlePlace.json"))))
			.when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
			.body("scope", equalTo("APP")).header("Content-Type", equalTo("application/json;charset=UTF-8"))
			.extract().response().asPrettyString();

	System.out.println(response);
	JsonPath path = ReusableMethod.rowToJson(response);
	//JsonPath path = new JsonPath(response);// for json parsing
	placeId = path.getString("place_id");
	System.out.println(placeId);
}

	/**
	 *  Put request
	 */
	
@Test(dependsOnMethods = "postPlace", priority = 2)
public void putPlace() {
	//String expectedAdd="70 winter walk, USA";
	String putResp = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
	.body("{\r\n"
			+ "\"place_id\":\""+placeId+"\",\r\n"
			+ "\"address\":\"70 winter walk, USA\",\r\n"
			+ "\"key\":\"qaclick123\"\r\n"
			+ "}") 
	.when().put("/maps/api/place/update/json")
	.then().log().all().assertThat().statusCode(200)
	.body("msg",equalTo("Address successfully updated"))
	.extract().response().asPrettyString();
	
	
	System.out.println(putResp);
	
	JsonPath js = ReusableMethod.rowToJson(putResp);
	//JsonPath js = new JsonPath(response); // for json parsing
	
	String newAddress = js.getString("address");
	System.out.println(newAddress);
}
	
	/**
	 * Get Request
	 */
@Test(dependsOnMethods = "putPlace", priority = 3)
public void getPlace() {
	
	given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
	.when().get("/maps/api/place/get/json")
	.then().log().all().assertThat().statusCode(200);
	//.body("address",equalTo("70 winter walk, USA"));
	Assert.assertEquals(newAddress, "70 winter walk, USA");
}

@Test(dependsOnMethods = "getPlace" , priority = 4)
public void deletePlace() {
	/**
	 * delete request
	 */
	
	given().log().all().queryParam("key", "qaclick123")
	.body("{\r\n"
			+ "\r\n"
			+ "    \"place_id\":\""+placeId+"\"\r\n"
			+ "}")
	.when().delete("/maps/api/place/delete/json")
	.then().log().all().assertThat().statusCode(200)
	.body("status",equalTo("OK"));
	
}

	
	/**
	 *  again send delete request
	 */
@Test(dependsOnMethods = "deletePlace", priority = 5)
public void deletePlaceAgain() {
	
	given().log().all().queryParam("key", "qaclick123")
	.body("{\r\n"
			+ "\r\n"
			+ "    \"place_id\":\""+placeId+"\"\r\n"
			+ "}")
	.when().delete("/maps/api/place/delete/json")
	.then().log().all().assertThat().statusCode(404)
	.body("msg",equalTo("Delete operation failed, looks like the data doesn't exists"));
}
	/**
	 *  check for Get request after delete place
	 */
	
	@Test(dependsOnMethods = "deletePlace", priority = 6)
	public void checkGetPlace() {
	
	given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
	.when().get("/maps/api/place/get/json")
	.then().log().all().assertThat().statusCode(404)
	.body("msg",equalTo("Get operation failed, looks like place_id  doesn't exists"));
}

}
