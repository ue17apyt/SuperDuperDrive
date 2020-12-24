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

import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.TIMEOUT_IN_SECOND;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteControllerTest {

    public static final String NOTE_TITLE = "Example Note Title";
    public static final String NOTE_DESCRIPTION = "Example Note Description";
    public static final String NEW_NOTE_TITLE = "New Note Title";
    public static final String NEW_NOTE_DESCRIPTION = "New Note Description";

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
        // Click the note tab
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "nav-notes-tab");
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Order(1)
    @Test
    public void createNoteTest() {

        // Click the button to create a new note
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "noteCreate");

        // Input the note title and the note description and submit the note form
        fillNoteForm(NOTE_TITLE, NOTE_DESCRIPTION);

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        verifyNote(NOTE_TITLE, NOTE_DESCRIPTION);

    }

    @Order(2)
    @Test
    public void editNoteTest() {

        // Click the edition button
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "noteEdit");

        // Input the new note title and the new note description and submit the new note form
        fillNoteForm(NEW_NOTE_TITLE, NEW_NOTE_DESCRIPTION);

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        verifyNote(NEW_NOTE_TITLE, NEW_NOTE_DESCRIPTION);

    }

    @Order(3)
    @Test
    public void deleteNoteTest() {

        // Click the deletion button
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "noteDelete");

        // Direct to the result page
        this.driverWait.until(ExpectedConditions.titleContains("Result"));
        assertEquals("Result", this.driver.getTitle());
        assertDoesNotThrow(() -> this.driver.findElement(By.id("result-success")));

        // Click the link of the home page
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "success-home");

        // Redirect to the home page
        this.driverWait.until(ExpectedConditions.titleContains("Home"));
        assertEquals("Home", this.driver.getTitle());

        // Click the note tab
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "nav-notes-tab");

        // Check the existence the note title and the note description
        assertTrue(this.driver.findElements(By.id("getNoteTitle")).isEmpty());
        assertTrue(this.driver.findElements(By.id("getNoteDescription")).isEmpty());

    }

    private void fillNoteForm(String noteTitle, String noteDescription) {

        // Input the note title
        TestUtils.clickWebElementWithValue(
                this.driver, this.driverWait, this.executor, "note-title", noteTitle
        );

        // Input the note description
        TestUtils.clickWebElementWithValue(
                this.driver, this.driverWait, this.executor, "note-description", noteDescription
        );

        // Submit the note form
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "noteFormSubmit");

    }

    private void verifyNote(String noteTitle, String noteDescription) {

        // Verify the note title
        this.driverWait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.id("getNoteTitle"))));
        assertEquals(noteTitle, this.driver.findElement(By.id("getNoteTitle")).getText());

        // Verify the note description
        this.driverWait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.id("getNoteDescription"))));
        assertEquals(noteDescription, this.driver.findElement(By.id("getNoteDescription")).getText());

        // Display the note record
        TestUtils.clickWebElement(this.driver, this.driverWait, this.executor, "noteCreate");
        assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + noteTitle + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + noteDescription + "']"));
        });

    }

}