package dynamicJson;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class NoDynamic {
	
	String bookId = null;
	String bookAuthor = null;

	@BeforeMethod
	public void setBaseUri() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
	}
	
	@Test(priority = 1)
	public void addBook() {

		String postResponse = given().log().all().header("Content-Type", "application/json").
				body(Payload.LibraryJson())
				.when().post("Library/Addbook.php").then().log().all().assertThat().statusCode(200)
				.body("Msg", equalTo("successfully added")).extract().response().toString();

		System.out.println(postResponse);
		JsonPath js = ReusableMethod.rowToJson(postResponse);
		bookId = js.getString("ID");

	}

}
