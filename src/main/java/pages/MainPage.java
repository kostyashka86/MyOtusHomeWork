package pages;

import actions.CommonActions;
import com.google.inject.Inject;
import com.otus.data.Months;
import com.otus.datatable.DataTableCourse;
import diconfig.GuiceScoped;
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
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MainPage extends CommonActions {

    private static final String url = "https://otus.ru/";

    @Inject
    public MainPage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    public void open() {
        guiceScoped.driver.get(url);
    }

    @FindBy(xpath = "//div[@class='lessons']//a[contains(@class,'lessons__new-item')]")
    public List<WebElement> allCoursesBlockList;

    public void checkFilterCourseByName(String filter) {
        getNamesOfAllCourses()
                .stream()
                .filter(p -> p.contains(filter))
                .collect(Collectors.toList())
                .forEach(i -> assertThat(i.contains(filter)).isTrue());
    }

    private List<String> getNamesOfAllCourses() {
        List<String> names = new ArrayList<>();
        for (WebElement element : allCoursesBlockList) {
            names.add(element.findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]")).getText());
        }
        return names;
    }

    public HashMap<WebElement, DataTableCourse> getNamesAndDates() {
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
            nameAndDate.put(element, DataTableCourse.builder().name(nameCourse).dateString(dateCourse).build());
        }
        for (Map.Entry<WebElement, DataTableCourse> entry : nameAndDate.entrySet()) {
            Date dt = parserDateRegex(entry.getValue().getDateString());
            if (dt != null) {
                entry.getValue().setDate(dt);
            }
        }
        return nameAndDate;
    }

    private Date parserDateRegex(String stringDateFromSite) {
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

    private Date stringToDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

    public List<DataTableCourse> searchCourseByStartDate(String dateCourse, Boolean equalsOrLater) throws Exception {
        if (dateCourse == null)
            throw new Exception("Date of course is empty");
        HashMap<WebElement, DataTableCourse> nameAndDate = getNamesAndDates();
        List<DataTableCourse> courseAfterFilterByDate;
        if (equalsOrLater) {
            courseAfterFilterByDate = filterCourseByDate(nameAndDate, (Date d) -> d.equals(stringToDate(dateCourse)));
        } else {
            courseAfterFilterByDate = filterCourseByDate(nameAndDate, (Date d) -> d.after(stringToDate(dateCourse)));
        }
        if (courseAfterFilterByDate.isEmpty())
            throw new Exception("Course not found");
        return courseAfterFilterByDate;
    }

    private List<DataTableCourse> filterCourseByDate(HashMap<WebElement, DataTableCourse> nameAndDate,
                                                     Predicate<Date> filterPredicate) {
        List<DataTableCourse> result = nameAndDate.values()
                .stream()
                .filter(dataTableCourse -> dataTableCourse.getDate() != null)
                .filter(dataTableCourse -> filterPredicate.test(dataTableCourse.getDate()))
                .collect(Collectors.toList());
        return result;
    }
}
