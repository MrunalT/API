package AutomationTest.BrightTalkTest;

import org.testng.Assert;

import com.aventstack.extentreports.Status;

import io.restassured.response.Response;

public class CommonFunctions extends BasePage {
	
	
	
	public void validateStatusCode(Response response, int statusCode)
	{
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, statusCode);
		reports.log(Status.PASS, "Retrieved Status code: "+statusCode+" as expected.");
		
	}
	
	
	public String getResponseAttribute(Response response, String attribute, String ObjectName,Boolean printReport)
	{
		String attributeValue =  response.jsonPath().getString(attribute);
		if(printReport)
		reports.log(Status.PASS, "Retrieved "+ObjectName+" from Response: "+attributeValue);
		return attributeValue;
	}
	
	
	public void compareText(String expectedValue , String actualValue, String objectName, Boolean printReport)
	{
			Assert.assertEquals(expectedValue, actualValue);
			if(printReport)
			reports.log(Status.PASS, objectName+" is verified. verification sucess. Verified value: "+expectedValue);
		
	}

}
