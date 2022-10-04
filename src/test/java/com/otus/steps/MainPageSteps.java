package com.otus.steps;

import com.google.inject.Inject;
import com.otus.pages.MainPage;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;

public class MainPageSteps {

    @Inject
    private final MainPage mainPage;
    public MainPageSteps(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Пусть("Открываем главную страницу")
    public void open() {
        mainPage.openMainPage();
    }

    @Если("Получен фильтр из настроек properties")
    public String getFilter() {
        return System.getProperty("filter");
    }

    @Тогда("Произойдёт поиск курсов с этим фильтром")
    public void findCourseByFilter() {
        mainPage.checkFilterCourseByName(getFilter());
    }
}
