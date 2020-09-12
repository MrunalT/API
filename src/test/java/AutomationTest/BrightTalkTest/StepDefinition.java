package AutomationTest.BrightTalkTest;

import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.get;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import org.apache.http.params.CoreConnectionPNames;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.hamcrest.Matchers;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

import static io.restassured.RestAssured.*;




public class StepDefinition extends BasePage {

	CommonFunctions commonFunction = new CommonFunctions();
	private Response response;
	private String baseURL = "https://reqres.in/";

	@Given("^I get the default list of users for on (\\d+)st page$")
	public void i_get_the_default_list_of_users_for_on_st_page(int pageNo) throws Throwable {

		//Send GET response for list user
		response = get(baseURL+"api/users?page");

		//validate http status 
		commonFunction.validateStatusCode(response, 200);

		//retrieve default page number 
		String page =  commonFunction.getResponseAttribute(response, "page", "Default page",true);
		
		// validate default page number with expected 
		commonFunction.compareText(String.valueOf(pageNo), page, "Default page number", true);
		
		
		//retrieve total user 
		String totalUser =  commonFunction.getResponseAttribute(response, "total", "Total user",true);
	
		
		//Send GET response for total list user
		response = given().get(baseURL+"api/users?per_page="+totalUser);

	}

	@When("^I get the list of all users$")
	public void i_get_the_list_of_all_users() throws Throwable {
		//retrieve data of all user
		commonFunction.getResponseAttribute(response, "data", "List of all users",true);
	}

	@Then("^I should see total users count equals to number of user ids$")
	public void i_should_see_total_users_count_equals_to_number_of_user_ids() throws Throwable {

		//retrieve total user per page
		String totalUser =  commonFunction.getResponseAttribute(response, "total", "Total User count per Page",true);
		
		//retrieve number of ids present on page
		String noOfID =  commonFunction.getResponseAttribute(response, "data.id.size()", "Number of ID present on page",true);

		//validate total user per page with number of ids present on page
		commonFunction.compareText(totalUser, noOfID, "Total users count equals to number of user ids", true);

	}

	@Given("^I make a search for user (\\d+)$")
	public void i_make_a_search_for_user(int userNo) throws Throwable {

		//retrieve data for given user
		response = given().get(baseURL+"api/users/"+userNo);
		reports.log(Status.PASS, "Response for User : "+userNo+"<br>"+response+"</br>");
	}

	@Then("^I should see following user data$")
	public void i_should_see_following_user_data(DataTable inputData) throws Throwable {

		List<Map<String,String>> data = inputData.asMaps(String.class,String.class);
		//retrieve user details from response
		String firstName =  commonFunction.getResponseAttribute(response, "data.first_name", "Total User count per Page",false);
		String email =  commonFunction.getResponseAttribute(response, "data.email", "Total User count per Page",false);

		//compare user details 
		commonFunction.compareText(data.get(0).get("first_name"), firstName, "Expected First Name", true);
		commonFunction.compareText(data.get(0).get("email"), email, "Expected Email", true);
	}


	@Then("^I receive error code (\\d+) in response$")
	public void i_receive_error_code_in_response(int errorCode) throws Throwable {
		//validate given error code
		commonFunction.validateStatusCode(response, errorCode);
	}


	@Given("^I create user with following \"([^\"]*)\" \"([^\"]*)\"$")
	public void i_create_user_with_following(String name, String job) throws Throwable {

		RestAssured.baseURI ="https://reqres.in/";
		RestAssured.basePath = "/api";

		//Put data in map which is used to create record 
		Map<String, String> request = new HashMap<>();
		request.put("name", name);
		request.put("job", job);

		//Post data to create data and validate status code
		response = given().contentType("application/json")
				.body(request)
				.when()
				.post("/users").then().using().extract().response();
		
		commonFunction.validateStatusCode(response, 201);
		reports.log(Status.PASS, "User: "+name+", "+job+" is created successfully.");

	}

	@Then("^response should contain folowing data$")
	public void response_should_contain_folowing_data(DataTable parameter) throws Throwable {

		//code to handle Data Table
		List<List<String>> data = parameter.raw();

		//Validate given datatable parameter are retrieved in response
		for (List<String> list : data) { 
			reports.log(Status.PASS, "Following user information is displayed: ");
			for (String item : list) { 
				commonFunction.getResponseAttribute(response, item, item, true);
			} 
		} 
	}


	@Given("^I login succesfully with following data$")
	public void i_login_succesfully_with_following_data(DataTable credential) throws Throwable {

		List<Map<String,String>> data = credential.asMaps(String.class,String.class);
		RestAssured.baseURI = "https://reqres.in/";
		RestAssured.basePath = "/api";

		// put basic auth details in map
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("username", data.get(0).get("Email"));
		jsonAsMap.put("password", data.get(0).get("Password"));  


		// login with valid credentials and check response
		response= given().
				contentType("application/json").
				body(jsonAsMap).
				when().
				post("/login")
				.then().using().extract().response();
	
		commonFunction.validateStatusCode(response, 200);
		commonFunction.getResponseAttribute(response, "token", "Token after successful logged in", true);
	}


	@Given("^I login unsuccesfully with following data$")
	public void i_login_unsuccesfully_with_following_data(DataTable credential) throws Throwable {


		List<Map<String,String>> data = credential.asMaps(String.class,String.class);
		RestAssured.baseURI = "https://reqres.in/";
		RestAssured.basePath = "/api";
		
		// put basic auth details in map
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("username", data.get(0).get("Email"));
		jsonAsMap.put("password", data.get(0).get("Password"));  

		// login with invalid credentials and check response
		response= given().
				contentType("application/json").
				body(jsonAsMap).
				when().
				post("/login")
				.then().using().extract().response();

		commonFunction.validateStatusCode(response, 400);
		commonFunction.getResponseAttribute(response, "error", "Error Message after unsuccessful log in", true);
		

	}

	@Given("^I wait for user list to load$")
	public void i_wait_for_user_list_to_load() throws Throwable {

		//get response with delay 3
		response = given().get("https://reqres.in/api/users?delay=3");

		//wait until condition satisfied
		await().untilAsserted(() -> Assertions.assertThat(commonFunction.getResponseAttribute(response, "per_page", "", false)).isEqualTo("6"));
		
		commonFunction.validateStatusCode(response, 200);

	} 


	@Then("^I should see that every user has a unique id$")
	public void i_should_see_that_every_user_has_a_unique_id() throws Throwable {

		//verification of every user has unique id
		String getID = response.jsonPath().getString("data.id");
		String getID1 =getID.replaceAll(",", "").replaceAll(" ", "");
		int cnt = 0;
		char[] arrId = getID1.toCharArray();
		for (int i = 0; i < getID1.length(); i++) {
			for (int j = i + 1; j < getID1.length(); j++) {
				if (arrId[i] == arrId[j]) {
					System.out.println(arrId[j]);
					cnt++;
					break;
				}
			}
		}

		System.out.println("count: "+cnt);
		Assert.assertEquals(cnt, 0);
		reports.log(Status.PASS, "Data list is loaded successfullly.");
		reports.log(Status.PASS, "every user has a unique id is verified" + getID);

	}
	
	@Given("^I am on the home page$")
	public void i_am_on_the_home_page() throws Throwable {
	   HomePage.homePage();
	}
	
	@Given("^I log in \".*\"$")
	public void i_login(String term, DataTable data)
	{
		
	}
}
