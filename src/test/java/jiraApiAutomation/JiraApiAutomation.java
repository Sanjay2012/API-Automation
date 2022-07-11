package jiraApiAutomation;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import files.ReusableMethod;
public class JiraApiAutomation {
	SessionFilter session=null;
	String loginResponse=null;
	String addBugIssueResponse=null;
	String bugIssueId=null;
	String bugCommentResponese=null;
	String bugCommentId=null;
	String updateBugCommentResponese=null;
	String getBugResp=null;
	
	
	@BeforeClass
	public void setBaseURI() {
		RestAssured.baseURI="http://localhost:8080";
	}
	
	@Test(priority = 1)
	public void loginToJira() {
		session=new SessionFilter();
		given().log().all().header("Content-Type", "application/json").
		body("{ \r\n"
				+ "    \"username\": \"Shiv2020\", \r\n"
				+ "    \"password\": \"Shiv@2020\" \r\n"
				+ "}").filter(session).
		when().post("/rest/auth/1/session").
		then().log().all().assertThat().statusCode(200);
	}
	
	@Test(priority = 2, dependsOnMethods = "loginToJira")
	public void addBugIssue() {
		addBugIssueResponse = given().log().all().header("Content-Type", "application/json").
				body("{ \r\n"
						+ "     \"fields\": {\r\n"
						+ "        \"project\": {\r\n"
						+ "            \"key\": \"IB\"\r\n"
						+ "        },\r\n"
						+ "        \"summary\": \"Credit card issue\",\r\n"
						+ "        \"description\": \"logging customer issue-1\",\r\n"
						+ "        \"issuetype\": {\r\n"
						+ "            \"name\": \"Bug\"\r\n"
						+ "        }\r\n"
						+ "     }\r\n"
						+ " }").filter(session).
				when().post("/rest/api/2/issue").
				then().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = ReusableMethod.rowToJson(addBugIssueResponse);
		bugIssueId = js.getString("id");
	
	}
	
	@Test(priority = 3, dependsOnMethods = "addBugIssue")
	public void addBugComment() {
		String addComment = "adding comment for issue";
		bugCommentResponese=given().log().all().header("Content-Type", "application/json").
				pathParam("id", bugIssueId).
				body("{\r\n"
				+ "    \"body\": \""+addComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}\r\n"
				+ "").filter(session).
		when().post("/rest/api/2/issue/{id}/comment").
		then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = ReusableMethod.rowToJson(bugCommentResponese);
		bugCommentId = js.getString("id");
		System.out.println(bugCommentId);
	}
	
	
	@Test(priority = 4, dependsOnMethods = "addBugIssue")
	public void updateBugComment() {
		updateBugCommentResponese=given().log().all().header("Content-Type", "application/json").
				pathParam("issueId", bugIssueId).pathParam("commentId", bugCommentId).
				body("{\r\n"
						+ "    \"body\": \"Hey I have updated the comment\",\r\n"
						+ "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n"
						+ "    }\r\n"
						+ "}").filter(session).
		when().put("/rest/api/2/issue/{issueId}/comment/{commentId}").
		then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReusableMethod.rowToJson(bugCommentResponese);
		bugCommentId = js.getString("id");
		System.out.println(bugCommentId);
	}
	
	
	
	@Test(priority = 5, dependsOnMethods = "addBugIssue")
	public void addAttachmentToBug() {
		given().log().all().header("X-Atlassian-Token", "no-check").
		pathParam("issueId", bugIssueId).filter(session).
		multiPart("file", new File("./Contents/sampleBug.png")).
		when().post("/rest/api/2/issue/{issueId}/attachments").
		then().log().all().assertThat().statusCode(200);
	}
	
	
	
	@Test(priority = 7, dependsOnMethods = "updateBugComment", enabled = false)
	public void deleteBugIssue() {
		given().log().all().pathParam("issueId", bugIssueId).filter(session).when()
				.delete("/rest/api/2/issue/{issueId}").then().log().all().assertThat().statusCode(204);
	}
	
	
	
	@Test(priority = 8, dependsOnMethods = "loginToJira", enabled = true)
	public void logoutSession() {
		given().log().all().filter(session).
				when().delete("/rest/auth/1/session").
				then().log().all().assertThat().statusCode(204);
	}
	
	@Test(priority = 6, dependsOnMethods = "addBugIssue")
	public void verifyBugAndIssue() {
		getBugResp = given().filter(session).
				pathParam("bugId", bugIssueId).queryParam("fields", "comment").log().all().
		when().get("/rest/api/2/issue/{bugId}").
		then().log().all().assertThat().statusCode(200).extract().response().asString();
		System.out.println(getBugResp);
		JsonPath js=new JsonPath(getBugResp);
		int commentCount = js.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentCount; i++) {
			String commentIdIssue = js.get("fields.comment.comments["+i+"].id").toString();
			if (commentIdIssue.equalsIgnoreCase(bugIssueId)) {
				String message = js.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals("message", "addComment");
			}
		}
		
	}
	

}
