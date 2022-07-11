package googleAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;
public class PostPutGet_ParsingJson {

	public static void main(String[] args) {
		/**
		 * Given---> all inputs When ---> Submit API-- resources, http methods Then --->
		 * validate the response
		 */
		
		
// calling payload method
		
		Payload load=new Payload();
		

		/**
		 *  POST Request
		 */

		RestAssured.baseURI = "https://rahulshettyacademy.com";

		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(load.PostPayload()) // logging request
				.when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Content-Type", equalTo("application/json;charset=UTF-8"))
				.extract().response().asPrettyString();

		System.out.println(response);
		JsonPath path = new JsonPath(response);
		String placeId = path.getString("place_id");
		System.out.println(placeId);

		/**
		 *  Put request
		 */
		
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
		JsonPath js = new JsonPath(response);
		String newAddress = js.getString("address");
		System.out.println(newAddress);
		
		
		/**
		 * Get Request
		 */
		
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200)
		.body("address",equalTo("70 winter walk, USA"));
		
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
		
		/**
		 *  again send delete request
		 */
		
		given().log().all().queryParam("key", "qaclick123")
		.body("{\r\n"
				+ "\r\n"
				+ "    \"place_id\":\""+placeId+"\"\r\n"
				+ "}")
		.when().get("/maps/api/place/delete/json")
		.then().log().all().assertThat().statusCode(404)
		.body("msg",equalTo("Delete operation failed, looks like the data doesn't exists"));
		
		/**
		 *  check for Get request after delete place
		 */
		
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(404)
		.body("msg",equalTo("Get operation failed, looks like place_id  doesn't exists"));
		
		
		
	}

}
