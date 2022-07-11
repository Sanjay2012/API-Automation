package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethod {
	public static JsonPath rowToJson(String postResponse) {
		JsonPath js = new JsonPath(postResponse);
		return js;
	}

}
