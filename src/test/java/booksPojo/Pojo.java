package booksPojo;
import static io.restassured.RestAssured.*;

import org.testng.annotations.Test;

import io.restassured.parsing.Parser;

public class Pojo {
	@Test()
	public void Deserialization() {
		GetDetails resp = given().header("Content-Type", "application/json").
				expect().defaultParser(Parser.JSON).
		when().
		get("http://localhost:3000/books").as(GetDetails.class);
		
		System.out.println(resp.getBooks().get(1).getTitle());
	}

}
