package com.otus.driver;

import com.google.inject.Inject;
import com.otus.driver.impl.ChromeWebDriver;
import com.otus.driver.impl.FireFoxWebDriver;
import com.otus.driver.impl.OperaWebDriver;
import com.otus.exeptions.DriverTypeNotSupported;
import diconfig.GuiceScoped;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class DriverFactory implements IDriverFactory {

    public GuiceScoped guiceScoped;

    @Inject
    public DriverFactory(GuiceScoped guiceScoped) {
        this.guiceScoped = guiceScoped;
    }

    @Override
    public EventFiringWebDriver getDriver() {
        switch (this.guiceScoped.browser) {
            case "chrome": {
                return new EventFiringWebDriver(new ChromeWebDriver().newDriver());
            }
            case "firefox": {
                return new EventFiringWebDriver(new FireFoxWebDriver().newDriver());
            }
            case "opera": {
                return new EventFiringWebDriver(new OperaWebDriver().newDriver());
            }
            default:
                try {
                    throw new DriverTypeNotSupported(this.guiceScoped.browser);
                } catch (DriverTypeNotSupported ex) {
                    ex.printStackTrace();
                    return null;
                }
        }
    }
}
