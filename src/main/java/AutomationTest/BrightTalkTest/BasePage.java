package AutomationTest.BrightTalkTest;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class BasePage {
    public static WebDriver driver;
    public static ExtentHtmlReporter reporter;
	public static ExtentReports extentReports;
	public static ExtentTest reports;
	public static Reporter report;
}
