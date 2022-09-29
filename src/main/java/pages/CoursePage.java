package pages;

import actions.CommonActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CoursePage extends CommonActions {

    public CoursePage(WebDriver driver) {
        super(driver);
    }

    public String getTitleByCourse(String titleBeforeClick) {
        By locator;
        if (!titleBeforeClick.toUpperCase().contains("СПЕЦИАЛИЗАЦИЯ"))
            locator = By.cssSelector(".course-header2__title");
        else
            locator = By.tagName("title");
        return driver.findElement(locator).getAttribute("innerText");
    }
}
