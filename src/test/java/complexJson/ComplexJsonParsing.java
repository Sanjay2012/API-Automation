package complexJson;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParsing {
	
	public static void main(String[] args) {
		Payload load=new Payload();
		
		JsonPath js=new JsonPath(load.PurchaseCourse());
		
		/**
		 * Print No of courses returned by API
		 */
		System.out.println("============ Print No of courses returned by API ============");
		int courseCount = js.getInt("courses.size()");
		System.out.println("Total courses count :"+courseCount);
		
		
		/**
		 * Print Purchase Amount
		 */
		System.out.println("=========== Print Purchase Amount ===========");
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Total purchase Amount is  Rs.:"+purchaseAmount);
		
		/**
		 * Print Title of the first course
		 */
		System.out.println("=========== Print Title of the first course ===========");
		// Object firstCourseName = js.get("courses[0].title");
		System.out.println(("The title of first course is  :"+js.get("courses[0].title")).toString());
		
		
		/**
		 * Print All course titles and their respective Prices
		 */
		System.out.println("========== Print All course titles and their respective Prices =========");
		for (int i = 0; i < courseCount; i++) {
			String courseTitle = js.get("courses["+i+"].title");
			String coursePrice = (js.get("courses["+i+"].price").toString());
			System.out.println(courseTitle +" --->  Rs."+coursePrice);
		}
		
		/**
		 * Print no of copies sold by RPA Course
		 */
		
		System.out.println("========= Print no of copies sold by RPA Course ==========");
		
		for (int i = 0; i < courseCount; i++) {
			String courseTitle = js.get("courses["+i+"].title");
			if (courseTitle.equals("RPA")) {
				 int copiesCount = js.getInt("courses["+i+"].copies");
				System.out.println("RPA copies sold count is  Rs."+copiesCount );
				break;
			}
			
		}
		
	}

}
