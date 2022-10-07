package pages;

import actions.CommonActions;
import diconfig.GuiceScoped;
import org.openqa.selenium.By;

public class CoursePage extends CommonActions {

    public CoursePage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    public String getTitleByCourse(String titleBeforeClick) {
        By locator;
        if (!titleBeforeClick.toUpperCase().contains("СПЕЦИАЛИЗАЦИЯ"))
            locator = By.cssSelector(".course-header2__title");
        else
            locator = By.tagName("title");
        return guiceScoped.driver.findElement(locator).getAttribute("innerText");
    }
}
