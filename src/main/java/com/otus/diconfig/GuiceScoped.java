package com.otus.diconfig;

import com.otus.driver.DriverFactory;
import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;

@ScenarioScoped
public class GuiceScoped {
    public final WebDriver driver = new DriverFactory().getDriver();
}
