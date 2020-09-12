package AutomationTest.BrightTalkTest;

import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.util.concurrent.TimeUnit;

import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Hook extends BasePage {
	
	
    BrowserSetup browsersetup = new BrowserSetup();
    private static final int WAIT_SEC = 20;

    @Before("@Sample")
    public void initializeTest() {
        browsersetup.selectBrowser();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(WAIT_SEC, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(WAIT_SEC, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(WAIT_SEC, TimeUnit.SECONDS);
        new iniClass();
    	
    }

    @Before("@currentTest")
    public void setUp(cucumber.api.Scenario scenario)
    {
    	String ScenarioName = scenario.getName();
    	ScenarioName = ScenarioName.replaceAll(" ", "_");
    	System.out.println("Scenarioo Name: "+ScenarioName);
    	String filePath = System.getProperty("user.dir");
    	reporter = new ExtentHtmlReporter(filePath+"\\Results\\"+ScenarioName+".html");
		System.out.println(filePath+"\\Results\\"+ScenarioName+".html");
		extentReports = new ExtentReports();
		extentReports.attachReporter(reporter);		
		reports = extentReports.createTest(ScenarioName);
    }
    @After("@Sample")
    public void screenshot() {
        driver.close();
        driver.quit();
        extentReports.flush();
    }
    
    @After("@currentTest")
    public void tearDown() {
        extentReports.flush();
    }
}

