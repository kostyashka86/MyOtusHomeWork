package com.otus.steps;

import com.google.inject.Inject;
import com.otus.datatable.DataTableCourse;
import com.otus.driver.DriverFactory;
import diconfig.GuiceScoped;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.То;
import io.cucumber.java.ru.Тогда;
import pages.MainPage;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainPageSteps {

    @Inject
    public MainPage mainPage;
    @Inject
    private GuiceScoped guiceScoped;
    @Inject
    private DriverFactory factoryDriver;

    @Пусть("Открываем главную страницу в браузере {string}")
    public void openMainPage(String browser) {
        guiceScoped.browser = browser;
        guiceScoped.driver = factoryDriver.getDriver();
        mainPage.open(System.getProperty("url"));
    }

    @Если("Фильтр равен {string}")
    public String getFilter(String filter) {
        return filter;
    }

    @Если("Дата курса {string}")
    public String getDate(String date) {
        return date;
    }

    @Тогда("Произойдёт поиск курсов, в названии которых есть слово {string}")
    public void findCourseByFilter(String filter) {
        mainPage.checkFilterCourseByName(getFilter(filter));
    }

    @То("Произойдёт поиск курса, стартующего {string}")
    public void findCourseByDate(String date) throws Exception {
        List<DataTableCourse> dataTableCourse = mainPage.searchCourseByStartDate(date, true);
        printCourses(dataTableCourse, date, true);
    }

    @То("Произойдёт поиск курсов, стартующих после после {string}")
    public void findCourseAfterDate(String date) throws Exception {
        List<DataTableCourse> dataTableCourse = mainPage.searchCourseByStartDate(date, false);
        printCourses(dataTableCourse, date, false);
    }

    private void printCourses(List<DataTableCourse> dataTableCourse, String date, Boolean equalsOrLater) {
        if (equalsOrLater) {
            dataTableCourse.forEach(p -> System.out.printf("На дату %s найден курс: \"%s\"%n",
                    date, p.getName()));
        } else dataTableCourse.forEach(p -> System.out.printf("После %s найден курс: \"%s\", дата = %s%n",
                date, p.getName(), new SimpleDateFormat("dd.MM.yyyy").format(p.getDate())));
    }
}
