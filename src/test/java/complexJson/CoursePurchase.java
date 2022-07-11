package complexJson;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class CoursePurchase {
	
	/**
	 * Verify if Sum of all Course prices matches with Purchase Amount
	 */
	@Test
	public void coursePurchaseValidation() {
		
		System.out.println("========== Verify if Sum of all Course prices matches with Purchase Amount ==========");
		int sum=0;
		Payload load=new Payload();
		JsonPath js=new JsonPath(load.PurchaseCourse());
		int courseCount = js.getInt("courses.size()");
		
		for (int i = 0; i < courseCount; i++) {
			int price = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			int amount = price*copies;
			System.out.println(amount);
			sum=sum+amount;
		}
		
		System.out.println("actual purchase amount is Rs."+sum);
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sum, purchaseAmount);
		
		
	}

}
