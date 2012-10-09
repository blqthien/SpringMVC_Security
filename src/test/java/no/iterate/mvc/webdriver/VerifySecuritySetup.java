package no.iterate.mvc.webdriver;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class VerifySecuritySetup {

	private final WebDriver driver = new FirefoxDriver();
	
	private static final String BASE_URL = "http://localhost:8080/SpringMVCWithSecurity/";
	private static final String SECURED_URL = BASE_URL;
	private static final String LOGIN_URL = BASE_URL+"spring_security_login";
	private static final String LOGOUT_URL = BASE_URL+"j_spring_security_logout";

	@After
	public void tearDown() {
		driver.close();
	}

	@Test
	public void shouldBeAskedToAuthenticateWhenTryingToVisitASecurePageAndNotLoggedIn() {
		//Act
		driver.get(SECURED_URL);

		//Verify
		assertThat(driver.getTitle(), is(not("Home")));
		assertThat(driver.getTitle(), is("Login Page"));
	}

	@Test
	public void successfulAuthenticationGivesAuthorizationToAccessSecurePage() {
		//Setup
		login();

		//Act
		driver.get(SECURED_URL);

		//Verify
		assertThat(driver.getTitle(), is("Home"));

	}

	@Test
	public void successfulLogoutRemovesAuthorization() {
		//Setup
		login();
		logout();
		
		//Act
		driver.get(SECURED_URL);
		
		//Verify
		assertThat(driver.getTitle(), is("Login Page"));

	}

	private void logout() {
		driver.get(LOGOUT_URL);
	}
	
	private void login() {
		driver.get(LOGIN_URL);
		
		WebElement userElement = driver.findElement(By.name("j_username"));
		userElement.sendKeys("user");
		WebElement passwordElement = driver.findElement(By.name("j_password"));
		passwordElement.sendKeys("user");

		passwordElement.submit();
	}

}
