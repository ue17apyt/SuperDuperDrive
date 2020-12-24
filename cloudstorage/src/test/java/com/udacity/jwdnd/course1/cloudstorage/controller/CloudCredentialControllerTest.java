package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CloudCredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudCredential;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.PASSWORD;
import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.TIMEOUT_IN_SECOND;
import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.USERNAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudCredentialControllerTest {

    public static final String URL = "https://www.youtube.com";
    public static final String NEW_URL = "https://www.google.com";
    public static final String NEW_USERNAME = "Guest";
    public static final String NEW_PASSWORD = "Deadline";
    public static final String URL_PATTERN = "(\\D+)(\\d+)";

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private CloudCredentialMapper credentialMapper;

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
        TestUtils.loginHome(this.driver, this.driverWait, this.executor, this.port);
        // Click the credential tab
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "nav-credentials-tab");
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Order(1)
    @Test
    public void insertCredentialTest() {

        // Click the button to create a new credential
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "credentialCreate");

        // Input the URL, username and password of credential and submit the credential form
        fillCredentialForm(URL, USERNAME, PASSWORD);

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        verifyCredential(URL, USERNAME, PASSWORD);

    }

    @Order(2)
    @Test
    public void editCredentialTest() {

        // Click the edition button
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "credentialEdit");

        // Input the new URL, new username and new password of credential and submit the new credential form
        fillCredentialForm(NEW_URL, NEW_USERNAME, NEW_PASSWORD);

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        verifyCredential(NEW_URL, NEW_USERNAME, NEW_PASSWORD);

    }

    @Order(3)
    @Test
    public void deleteCredentialTest() {

        // Click the deletion button
        this.driverWait.until(
                ExpectedConditions.elementToBeClickable(this.driver.findElement(By.id("credentialDelete")))
        );
        this.executor.executeScript("arguments[0].click();", this.driver.findElement(By.id("credentialDelete")));

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        // Check the existence of the URL, username and password of credential
        assertTrue(this.driver.findElements(By.id("getUrl")).isEmpty());
        assertTrue(this.driver.findElements(By.id("getUsername")).isEmpty());
        assertTrue(this.driver.findElements(By.id("getPassword")).isEmpty());

    }

    private void fillCredentialForm(String url, String username, String password) {

        // Input the URL of credential
        TestUtils.clickWebElementWithValue(this.driver, this.driverWait, this.executor, "credential-url", url);

        // Input the username of credential
        TestUtils.clickWebElementWithValue(
                this.driver, this.driverWait, this.executor, "credential-username", username
        );

        // Input the password of credential
        TestUtils.clickWebElementWithValue(
                this.driver, this.driverWait, this.executor, "credential-password", password
        );

        // Submit the credential form
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "credentialFormSubmit");

    }

    private void verifyCredential(String url, String username, String password) {

        // Verify the URL of credential
        this.driverWait.until(ExpectedConditions.elementToBeClickable(this.driver.findElement(By.id("getUrl"))));
        assertEquals(url, this.driver.findElement(By.id("getUrl")).getText());

        // Verify the username of credential
        this.driverWait.until(ExpectedConditions.elementToBeClickable(this.driver.findElement(By.id("getUsername"))));
        assertEquals(username, this.driver.findElement(By.id("getUsername")).getText());

        // Verify the password of credential
        this.driverWait.until(ExpectedConditions.elementToBeClickable(this.driver.findElement(By.id("getPassword"))));
        String encryptedPassword = this.driver.findElement(By.id("getPassword")).getText();
        this.driverWait.until(
                ExpectedConditions.elementToBeClickable(this.driver.findElement(By.id("credentialDelete")))
        );
        WebElement credentialElement = this.driver.findElement(By.id("credentialDelete"));
        String[] hrefParser = credentialElement.getAttribute("href").split("/");
        Matcher matcher = Pattern.compile(URL_PATTERN).matcher(hrefParser[3]);
        if (matcher.find()) {
            Integer credentialId = Integer.parseInt(matcher.group(2));
            CloudCredential credential = this.credentialMapper.findCredentialById(credentialId);
            String decryptedPassword = this.encryptionService.decryptValue(encryptedPassword, credential.getUserKey());
            assertEquals(password, decryptedPassword);
        }

        // Display the credential record
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "credentialCreate");
        assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + url + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + username + "']"));
        });
        assertThrows(
                NoSuchElementException.class, () -> this.driver.findElement(By.xpath("//td[text()='" + password + "']"))
        );

    }

}