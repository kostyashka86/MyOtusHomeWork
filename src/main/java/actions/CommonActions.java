package actions;

import com.otus.waiters.StandartWaiter;
import diconfig.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class CommonActions {

    protected GuiceScoped guiceScoped;
    protected StandartWaiter standartWaiter;

    public CommonActions(GuiceScoped guiceScoped) {
        this.guiceScoped = guiceScoped;
        PageFactory.initElements(guiceScoped.driver, this);
        standartWaiter = new StandartWaiter(guiceScoped.driver);
    }

    protected BiConsumer<By, Predicate<? super WebElement>> clickElementByPredicate = (By locator, Predicate<? super WebElement> predicate) -> {
        List<WebElement> elements = guiceScoped.driver.findElements(locator).stream().filter(predicate).collect(Collectors.toList());
        if (!elements.isEmpty()) {
            elements.get(0).click();
        }
    };

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(guiceScoped.driver);
        try {
            actions.moveToElement(element).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clickToElement(WebElement element) {
        element.click();
    }
}
