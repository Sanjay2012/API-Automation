package googleAPI;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;
public class PostPlace {

	public static void main(String[] args) {
		/**
		 * Given---> all inputs
		 * When ---> Submit API-- resources, http methods
		 * Then ---> validate the response
		 */
		
		//POST Google Place
		Payload load=new Payload();
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type", "application/json")
		.body(load.PostPayload())   // loging request
		.when().post("/maps/api/place/add/json")
		.then().log().all().assertThat()
		.statusCode(200)
		.body("scope", equalTo("APP"))
		.header("Content-Type", equalTo("application/json;charset=UTF-8"));

	}

}
