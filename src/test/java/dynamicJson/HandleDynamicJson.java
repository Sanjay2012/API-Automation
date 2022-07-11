package dynamicJson;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.commons.lang3.RandomStringUtils;

public class HandleDynamicJson {
	String bookId=null;
	String bookAuthor=null;

	@BeforeMethod
	public void setBaseUri() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
	}

	@Test(priority = 1, dataProvider="CreateId")
	public void addBook(String isbn, String aisle) {
		 String postResponse = given().log().all().header("Content-Type", "application/json").
				body(Payload.LibraryAPI(isbn, aisle))
				.when().post("Library/Addbook.php").then().log().all().assertThat().statusCode(200)
				.body("Msg", equalTo("successfully added")).extract().response().asPrettyString();

		System.out.println(postResponse);
		JsonPath js = ReusableMethod.rowToJson(postResponse);
		bookId = js.getString("ID");
		bookAuthor = js.getString("author");
		System.out.println(bookId);
		System.out.println(bookAuthor);

	}

	@Test(priority = 2, dependsOnMethods = "addBook")
	public void getBookById() {
		given().log().all().queryParam("ID", bookId).
		when().get("/Library/GetBook.php").
		then().log().all().assertThat().statusCode(200);
	}
	
	
	@Test(priority = 3, dependsOnMethods = "addBook")
	public void deleteBookById() {
		given().log().all().
		body("{\r\n"
				+ "    \"ID\": \""+bookId+"\"\r\n"
				+ "}").
		when().delete("/Library/DeleteBook.php").
		then().log().all().assertThat().statusCode(200).
		body("msg", equalTo("book is successfully deleted"));
	}
	
	
	@DataProvider(name="CreateId")
	public Object[][] createID() {
		return new Object[][] {
				{isbnGenerator(), aisleGenerator()},
		};
	}
	
	public String isbnGenerator() {
		return RandomStringUtils.randomAlphabetic(5);
	}
	
	
	public String aisleGenerator() {
		return RandomStringUtils.randomNumeric(4);
		
	}

}
