package serializationDemo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

public class ImpSpecBuilder {
	@Test
	public void serializeTest(){
		
		AddPlace place=new AddPlace();
		
		place.setAccuracy(50);
		place.setName("Frontline house");
		place.setPhone_number("(+91) 983 893 3937");
		place.setAddress("70 winter walk, USA");
		place.setWebsite("http://google.com");
		place.setLanguage("French-IN");
		
		List<String> myList=new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		place.setTypes(myList);
		
		Location l=new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		
		place.setLocation(l);
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
						addQueryParam("key", "qaclick123").
						setContentType(ContentType.JSON).build();
		
		  RequestSpecification request = given().spec(req).
			body(place);
		  
		  
		  ResponseSpecification response = new ResponseSpecBuilder().expectContentType(ContentType.JSON).
		  expectStatusCode(200).build();
		  
		 
		 String res1 = request.when().post("/maps/api/place/add/json").		
				 then().spec(response).extract().response().toString();
		 
		System.out.println(res1);
	}

}
