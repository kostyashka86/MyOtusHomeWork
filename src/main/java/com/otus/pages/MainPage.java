package com.otus.pages;

import com.google.inject.Inject;
import com.otus.actions.CommonActions;
import com.otus.data.Months;
import com.otus.datatable.DataTableCourse;
import com.otus.diconfig.GuiceScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPage extends CommonActions {

    @Inject
    public MainPage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    private String getUrl() {
        return System.getProperty("base.url");
    }

    public void openMainPage() {
        guiceScoped.driver.get(getUrl());
    }

    @FindBy(xpath = "//div[@class='lessons']//a[contains(@class,'lessons__new-item')]")
    public List<WebElement> allCoursesBlockList;

    public void checkFilterCourseByName(String filter) {
        getNamesOfAllCourses()
                .stream()
                .filter(p -> p.contains(filter))
                .collect(Collectors.toList())
                .forEach(i -> assertTrue(i.contains(filter)));
    }

    private List<String> getNamesOfAllCourses() {
        List<String> names = new ArrayList<>();
        for (WebElement element : allCoursesBlockList) {
            names.add(element.findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]")).getText());
        }
        return names;
    }

    public void findEarliestCourse() {
        System.out.println("Самый ранний курс: " + getMinMaxDateOfCourse(getNamesAndDates(), false).getText());
    }

    public void findLatestCourse() {
        System.out.println("Самый поздний курс: " + getMinMaxDateOfCourse(getNamesAndDates(), true).getText());
    }

    private HashMap<WebElement, DataTableCourse> getNamesAndDates() {
        HashMap<WebElement, DataTableCourse> nameAndDate = new HashMap<>();
        String nameCourse;
        String dateCourse;
        for (WebElement element : allCoursesBlockList) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[contains(@class, 'lessons__new-item-time') or contains(@class, 'lessons__new-item-start')]"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }
        return nameAndDate;
    }

    private WebElement getMinMaxDateOfCourse(HashMap<WebElement, DataTableCourse> nameAndDate, Boolean minMax) {
        changeStringDateOnDate(nameAndDate);
        BinaryOperator<Map.Entry<WebElement, DataTableCourse>> binaryOperator = getBinaryOperator(minMax);
        WebElement result = nameAndDate
                .entrySet()
                .stream()
                .filter(p -> p.getValue().getDate() != null)
                .reduce(binaryOperator)
                .map(Map.Entry::getKey)
                .orElse(null);
        return result;
    }

    private static BinaryOperator<Map.Entry<WebElement, DataTableCourse>> getBinaryOperator(Boolean minMax) {
        BinaryOperator<Map.Entry<WebElement, DataTableCourse>> binaryOperator = minMax ?
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s1 : s2) :
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s2 : s1);
        return binaryOperator;
    }

    private void changeStringDateOnDate(HashMap<WebElement, DataTableCourse> nameAndDate) {
        for (Map.Entry<WebElement, DataTableCourse> entry : nameAndDate.entrySet()) {
            Date date = dateParser(entry.getValue().getDateString());
            if (date != null) {
                entry.getValue().setDate(date);
            }
        }
    }

    private Date dateParser(String stringDateFromSite) {
        int day;
        String month;
        String year;
        Pattern p = Pattern.compile("(?<day>\\d{1,2})\\W{1,3}(?<month>янв|фев|мар|апр|май|июн|июл|авг|сен|окт|ноя|дек)\\W{1,2}(?<year>\\d{4})?",
                Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher m = p.matcher(stringDateFromSite);
        if (m.find()) {
            day = Integer.parseInt(m.group("day"));
            month = m.group("month");
            year = m.group("year");
            return stringToDate(day, month, year);
        } else
            return null;
    }

    private Date stringToDate(int day, String month, String year) {
        LocalDate date = LocalDate.now();
        String monthNumber = getMonth(month);
        try {
            String str = String.format("%d/%s/%d", day, monthNumber, year == null ? date.getYear() : Integer.parseInt(year));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return formatter.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMonth(String month) {
        String monthRUS = String.valueOf(month.toCharArray(), 0, 3);
        return Months.findMonth(monthRUS);
    }
}
