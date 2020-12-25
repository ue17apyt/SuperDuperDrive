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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.TIMEOUT_IN_SECOND;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileControllerTest {

    public static final String FILENAME = "POKERSTARS.txt";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String UPLOAD_FILE_PATH =
            System.getProperty("user.home") + File.separator + "Downloads" + File.separator + FILENAME;
    public static final String DOWNLOAD_FILE_PATH = System.getProperty("user.dir") + File.separator + FILENAME;
    public static final long AWAIT_IN_MILLISECONDS = 200;

    private final Logger logger = LoggerFactory.getLogger(FileControllerTest.class);

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
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.default_directory", System.getProperty("user.dir"));
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", preferences);
        this.driver = new ChromeDriver(options);
        this.driverWait = new WebDriverWait(this.driver, TIMEOUT_IN_SECOND);
        this.executor = (JavascriptExecutor) this.driver;
        TestUtils.loginHome(this.driver, this.driverWait, this.executor, this.port);
        // Click the file tab
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "nav-files-tab");
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Order(1)
    @Test
    public void uploadFileTest() {

        // Choose a new file to upload
        this.driver.findElement(By.id("fileUpload")).sendKeys(UPLOAD_FILE_PATH);

        // Click the button to upload the new file
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "fileCreate");

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        // Verify the filename
        this.driverWait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.id("getFilename"))));
        assertEquals(FILENAME, this.driver.findElement(By.id("getFilename")).getText());

        // Verify the content type
        this.driverWait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.id("getContentType"))));
        assertEquals(CONTENT_TYPE, this.driver.findElement(By.id("getContentType")).getText());

    }

    @Order(2)
    @Test
    public void uploadEmptyFileTest() {

        // Click the button to upload the new file
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "fileCreate");

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-error")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "error-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

    }

    @Order(3)
    @Test
    public void viewFileTest() {

        // Click the view button
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "fileView");

        // Wait some time for downloading the file
        Path downloadedFilePath = Paths.get(System.getProperty("user.dir"), FILENAME);
        await().atMost(AWAIT_IN_MILLISECONDS, MILLISECONDS).ignoreExceptions().until(
                () -> downloadedFilePath.toFile().exists()
        );

        // Check whether the file exists
        File downloadedFile = new File(DOWNLOAD_FILE_PATH);
        assertTrue(downloadedFile.exists());
        assertTrue(downloadedFile.isFile());

        // Delete the downloaded file
        downloadedFile.delete();
        assertFalse(downloadedFile.exists());

    }

    @Order(4)
    @Test
    public void deleteFileTest() {

        // Click the deletion button
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "fileDelete");

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        // Check the existence the filename, content type and the filesize of file
        assertTrue(this.driver.findElements(By.id("getFilename")).isEmpty());
        assertTrue(this.driver.findElements(By.id("getContentType")).isEmpty());
        assertTrue(this.driver.findElements(By.id("getFilesize")).isEmpty());

    }

}