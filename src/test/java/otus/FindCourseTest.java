package otus;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.MainPage;

public class FindCourseTest extends BaseTest {

    @Test
    @DisplayName("Тест поиска курса по фильтру")
    public void testFindCourseByName() {
        new MainPage(driver)
                .checkFilterCourseByName();
    }

    @Test
    @DisplayName("Тест поиска самого раннего курса")
    public void testFindEarliestCourse() {
        new MainPage(driver)
                .findEarliestCourse();
    }

    @Test
    @DisplayName("Тест поиска самого позднего курса")
    public void testGetLatestCourse() {
        new MainPage(driver)
                .findLatestCourse();
    }
}
