package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.FIRST_NAME;
import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.LAST_NAME;
import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.PASSWORD;
import static com.udacity.jwdnd.course1.cloudstorage.controller.AccessControllerTest.USERNAME;

public class TestUtils {

    public static void clickWebElement(
            WebDriver driver, WebDriverWait driverWait, JavascriptExecutor executor, String elementId
    ) {
        driverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(elementId))));
        executor.executeScript("arguments[0].click();", driver.findElement(By.id(elementId)));
    }

    public static void clickWebElementWithValue(
            WebDriver driver, WebDriverWait driverWait, JavascriptExecutor executor, String elementId, String value
    ) {
        clickWebElement(driver, driverWait, executor, elementId);
        executor.executeScript("arguments[0].value='" + value + "';", driver.findElement(By.id(elementId)));
    }

    public static void fillLoginForm(WebDriver driver, WebDriverWait driverWait, JavascriptExecutor executor) {
        // Input the username
        clickWebElementWithValue(driver, driverWait, executor, "inputUsername", USERNAME);
        // Input the password
        clickWebElementWithValue(driver, driverWait, executor, "inputPassword", PASSWORD);
        // Submit the login form
        clickWebElement(driver, driverWait, executor, "loginFormSubmit");
    }

    public static void fillSignupForm(WebDriver driver, WebDriverWait driverWait, JavascriptExecutor executor) {
        // Input the first name
        clickWebElementWithValue(driver, driverWait, executor, "inputFirstName", FIRST_NAME);
        // Input the last name
        clickWebElementWithValue(driver, driverWait, executor, "inputLastName", LAST_NAME);
        // Input the username
        clickWebElementWithValue(driver, driverWait, executor, "inputUsername", USERNAME);
        // Input the password
        clickWebElementWithValue(driver, driverWait, executor, "inputPassword", PASSWORD);
        // Submit the signup form
        clickWebElement(driver, driverWait, executor, "signupFormSubmit");
    }

    public static void loginHome(WebDriver driver, WebDriverWait driverWait, JavascriptExecutor executor, int port) {
        driver.get("http://localhost:" + port + "/signup");
        fillSignupForm(driver, driverWait, executor);
        driver.get("http://localhost:" + port + "/login");
        fillLoginForm(driver, driverWait, executor);
        driver.get("http://localhost:" + port + "/home");
    }

}