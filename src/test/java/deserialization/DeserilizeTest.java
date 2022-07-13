/**
 *  There is issue with response
 *  So, problem ariases while deserializing json response
 */

package deserialization;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import files.Payload;


public class DeserilizeTest {
	String placeId;
	Payload load ;
	@BeforeMethod
	public void baseURI() {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
	}

		/**
		 *  POST Request
		 */
	@Test(priority = 1)
	public void postPlace() {
		load=new Payload();
		 String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(load.PostPayload()) 
				.when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Content-Type", equalTo("application/json;charset=UTF-8"))
				.extract().response().asPrettyString();

		System.out.println(response);
		JsonPath path = new JsonPath(response);
		placeId = path.getString("place_id");
		System.out.println(placeId);
	}
		/**
		 * Get request for get place details
		 */
	@Test(dependsOnMethods = "postPlace", priority = 2)
	public void getPlace() {
		
		 AddPlace resp = given().queryParam("key", "qaclick123").queryParam("place_id", placeId).
					expect().defaultParser(Parser.JSON).
					when().get("/maps/api/place/get/json").as(AddPlace.class);

		 System.out.println(resp.getAddress());
	}
}
	
