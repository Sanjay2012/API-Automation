package dynamicJson;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RunWithDataProvider {
	String id = null;
	
	/**
	 * dataProviderClass =DataProviderClass.class 
	 * ---> not required if we prepare in same class
	 * @param name
	 * @param isbn
	 * @param aisle
	 * @param author
	 */

	@Test(dataProvider="CreateId", priority = 1)
	public void addBook(String name, String isbn, String aisle, String author) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		 String resp = given().log().all().header("Content-Type", "application/json").
				body(Payload.DynamicLibraryJson(name,isbn, aisle, author))
				.when().post("Library/Addbook.php").then().log().all().assertThat().statusCode(200)
				.body("Msg", equalTo("successfully added")).extract().response().asPrettyString();
		
		JsonPath js = ReusableMethod.rowToJson(resp);
		id=js.get("ID");
		System.out.println(id);

	}
	
	
	@Test(dependsOnMethods = "addBook")
	public void deleteBookById() {
		given().log().all().
		body("{\r\n"
				+ "    \"ID\": \""+id+"\"\r\n"
				+ "}").
		when().delete("/Library/DeleteBook.php").
		then().log().all().assertThat().statusCode(200).
		body("msg", equalTo("book is successfully deleted"));
	}
	
	
	@DataProvider(name="CreateId")
	public Object[][] createID() {
		return new Object[][] {
				{"Learn Selenium using Java",isbnGenerator(), aisleGenerator(), "Jems Smith"},
				{"Learn Cypress",isbnGenerator(),aisleGenerator(), "Smith Kooper"},
				{"Learn API automation",isbnGenerator(),aisleGenerator(), "Rahul Shetty"},
				{"Leran Selenium with Python",isbnGenerator(),aisleGenerator(), "Sanjay Kumar"}
		};
	}
	
	public String isbnGenerator() {
		return RandomStringUtils.randomAlphabetic(5);
	}
	
	
	public String aisleGenerator() {
		
		return RandomStringUtils.randomNumeric(4);
		
	}

}
