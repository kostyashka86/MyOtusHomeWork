package otus;

import annotaion.Driver;
import extensions.UIExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

@ExtendWith(UIExtension.class)
public class FindCourseTest {
    @Driver
    public WebDriver driver;

    @Test
    @DisplayName("Тест поиска курса по фильтру")
    public void testFindCourseByName() {
        new MainPage(driver)
                .open()
                .checkFilterCourseByName();
    }

    @Test
    @DisplayName("Тест поиска самого раннего курса")
    public void testFindEarliestCourse() {
        new MainPage(driver)
                .open()
                .findEarliestCourse();
    }

    @Test
    @DisplayName("Тест поиска самого позднего курса")
    public void testGetLatestCourse() {
        new MainPage(driver)
                .open()
                .findLatestCourse();
    }
}
