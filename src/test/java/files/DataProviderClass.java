package files;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public class DataProviderClass {
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
