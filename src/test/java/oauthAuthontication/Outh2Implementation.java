package oauthAuthontication;

import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
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
		
		String url="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AdQt8qhVBrEWel_nX179F64tuZ5otX0PjUNYT0hwWROn0vGbWGIZmDkk0K-3f6Raabscqw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
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
		
		String response = given().queryParam("access_token", accessToken).
		when().log().all().
		get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println(response);
		
		
	}

}
