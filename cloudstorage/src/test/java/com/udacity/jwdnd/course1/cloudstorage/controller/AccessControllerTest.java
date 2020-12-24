package com.udacity.jwdnd.course1.cloudstorage.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccessControllerTest {

    public static final int TIMEOUT_IN_SECOND = 3;
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String USERNAME = "Administrator";
    public static final String PASSWORD = "Birthday";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait driverWait;
    private JavascriptExecutor executor;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.driverWait = new WebDriverWait(this.driver, TIMEOUT_IN_SECOND);
        this.executor = (JavascriptExecutor) this.driver;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Order(1)
    @Test
    public void attemptToVisitHomeDirectlyTest() {
        this.driver.get("http://localhost:" + this.port + "/home");
        assertEquals("Login", this.driver.getTitle());
    }

    @Order(2)
    @Test
    public void userLoginWithoutSignupTest() {

        // Enter the login page
        this.driver.get("http://localhost:" + this.port + "/login");
        assertEquals("Login", this.driver.getTitle());

        // Fill the login form with not-signed-up data and try to sign in the home page
        TestUtils.fillLoginForm(this.driver, this.driverWait, this.executor);
        assertDoesNotThrow(() -> this.driver.findElement(By.id("error-msg")));
        assertEquals("Login", this.driver.getTitle());

    }

    @Order(3)
    @Test
    public void signupTest() {

        // Enter the login page
        this.driver.get("http://localhost:" + this.port + "/login");
        assertEquals("Login", this.driver.getTitle());

        // Direct to the signup page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "signup-link");
        assertEquals("Sign Up", this.driver.getTitle());

        // Fill the signup form with data and submit the signup form
        TestUtils.fillSignupForm(this.driver, this.driverWait, this.executor);
        assertDoesNotThrow(() -> this.driver.findElement(By.id("success-msg")));
        assertEquals("Sign Up", this.driver.getTitle());

        // Return to the login page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "login-link");
        assertEquals("Login", this.driver.getTitle());

        // Fill the login form with signed-up data and sign in the home page
        TestUtils.fillLoginForm(this.driver, this.driverWait, this.executor);
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        // Logout the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "logout");
        this.driverWait.until(ExpectedConditions.titleContains("Login"));
        assertEquals("Login", this.driver.getTitle());

    }

    @Order(4)
    @Test
    public void signupWithSameUsernameTest() {

        // Enter the signup page
        this.driver.get("http://localhost:" + this.port + "/signup");
        assertEquals("Sign Up", this.driver.getTitle());

        // Fill the signup form with same data and try to submit the signup form
        TestUtils.fillSignupForm(this.driver, this.driverWait, this.executor);
        assertDoesNotThrow(() -> this.driver.findElement(By.id("error-msg")));
        assertEquals("Sign Up", this.driver.getTitle());

    }

}