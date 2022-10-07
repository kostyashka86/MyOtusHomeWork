package diconfig;

import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;

@ScenarioScoped
public class GuiceScoped {
    public String browser;
    public WebDriver driver;
}
