package otus;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;

public class BaseTest {

  public WebDriver driver;

  @Before
  public void init() throws MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
    capabilities.setCapability(CapabilityType.BROWSER_VERSION, "107.0");
    capabilities.setCapability("enableVNC", true);
    driver = new RemoteWebDriver(
            URI.create("http://localhost:4444/wd/hub").toURL(),
            capabilities
    );
    driver.get("https://otus.ru");
  }

  @After
  public void tearDown() {
    if (driver != null) {
      driver.close();
      driver.quit();
    }
  }
}
