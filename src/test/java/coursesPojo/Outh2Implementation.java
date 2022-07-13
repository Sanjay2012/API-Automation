package coursesPojo;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Outh2Implementation {
	
	/**
	 * Steps:
	 * 1. Sign in into account by hitting google authontication server and Get code from google
	 * 2. Get access token --> use this code to hit google resource server in back  
	 *  and to get access token
	 * 3. Get actual request--> application grant access to user by validating access code
	 * @throws InterruptedException 
	 */
	
	@Test
	public void validationOfOAuth2() {
		
		//Get code by hitting google authentication server
//		WebDriverManager.chromedriver().setup();
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--incognito");
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//		options.merge(capabilities);
//		WebDriver driver=new ChromeDriver(options);
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("madeeasyctc@gmail.com");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(5000);
//		
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Shiv@123");
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
//		Thread.sleep(5000);
		
		String url="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AdQt8qgyw2auSaiY7M7E3N6KYTIoRu5ecz70kZSQAYVLsChuG7bNnMA5e9t2_cHsNl3oQA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		System.out.println(url);
		
		String partialUrl = url.split("code=")[1];
		String code=partialUrl.split("&scope")[0];
		System.out.println(code);
		
		//To get access token
		
		String accessTokenResponse = given().urlEncodingEnabled(false).
		queryParams("code", code).
		queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
		queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W").
		queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php").
		queryParams("grant_type", "authorization_code").
		when().log().all().
		post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		// Actual request---> to get courses
		
		GetCourse courseJson = given().queryParam("access_token", accessToken).
				expect().defaultParser(Parser.JSON).
		when().
		get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
		System.out.println(courseJson.getInstructor());
		System.out.println(courseJson.getUrl());
		System.out.println(courseJson.getServices());
		System.out.println(courseJson.getExpertise());
		System.out.println(courseJson.getLinkedIn());
		System.out.println(courseJson.getCourses().getWebAutomation().get(2).getCourseTitle());
		
	// print price of respective course without index
		
		List<WebAutomation> autoCourses = courseJson.getCourses().getWebAutomation();
		
		for (int i = 0; i < autoCourses.size(); i++) {
			if (autoCourses.get(i).getCourseTitle().equalsIgnoreCase("Protractor")) {
				System.out.println(autoCourses.get(i).getPrice());
				
			}
			
		}
		
		
		// print all courses from webAutomation

		for (int i = 0; i < autoCourses.size(); i++) {
			System.out.println(autoCourses.get(i).getCourseTitle());
		}
	
	
		//print and assert courses
		
		String [] courses= {"Selenium Webdriver Java","Cypress","Protractor"};
		
		ArrayList<String> a=new ArrayList<String>();
		
		for (int i = 0; i < autoCourses.size(); i++) {
			a.add(autoCourses.get(i).getCourseTitle());
		}
		List<String> expList=Arrays.asList(courses);
		
		Assert.assertTrue(a.equals(expList));
	}

}
