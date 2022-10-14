package pages;

import diconfig.GuiceScoped;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage<T> {
    protected GuiceScoped guiceScoped;

    public BasePage(GuiceScoped guiceScoped) {
        this.guiceScoped = guiceScoped;
        PageFactory.initElements(guiceScoped.driver, this);
    }

    public T open(String url) {
        guiceScoped.driver.get(url);
        return (T) this;
    }
}
