package pages;

import data.Months;
import datatable.DataTableCourse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class MainPage extends Page {

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public MainPage open() {
        driver.get(url);
        return new MainPage(driver);
    }

    @FindBy(xpath = "//div[@class='container container-lessons']")
    public WebElement popularCourses;

    @FindBy(xpath = "//div[@class='container-padding-bottom']")
    public WebElement specializationsCourses;

    @FindBy(xpath = "//div[@class='lessons']//a[contains(@class,'lessons__new-item')]")
    public List<WebElement> allCourses;

    public MainPage checkFilterCourseByName() {
        String filter = System.getProperty("filter");
        getNamesOfAllCourses()
                .stream()
                .filter(p -> p.contains(filter))
                .collect(Collectors.toList())
                .forEach(i -> assertTrue(i.contains(filter)));
        return this;
    }

    private List<String> getNamesOfAllCourses() {
        List<String> names = new ArrayList<>();
        for (WebElement element : allCourses) {
            names.add(element.findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]")).getText());
        }
        return names;
    }

    public MainPage findEarliestCourse() {
        System.out.println("Самый ранний курс: " + getMinMaxDateOfCourse(getNamesAndDates(), false).getText());
        return this;
    }

    public MainPage findLatestCourse() {
        System.out.println("Самый поздний курс: " + getMinMaxDateOfCourse(getNamesAndDates(), true).getText());
        return this;
    }

    public static String getNameOfCourse(WebElement course) {
        return course.findElement(By.className("lessons__new-item-title")).getText();
    }

    private HashMap<WebElement, DataTableCourse> getNamesAndDates() {
        HashMap<WebElement, DataTableCourse> nameAndDate = new HashMap<>();
        String nameCourse;
        String dateCourse;
        List<WebElement> blockPopular = popularCourses.findElements(By.xpath("./div[@class='lessons']/a"));
        for (WebElement element : blockPopular) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[@class='lessons__new-item-start']"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }
        List<WebElement> blockSpecial = specializationsCourses.findElements(By.xpath("./div[@class='lessons']/a"));
        for (WebElement element : blockSpecial) {
            nameCourse = element
                    .findElement(By.xpath(".//div[contains(@class,'lessons__new-item-title')]"))
                    .getText();
            dateCourse = element
                    .findElement(By.xpath(".//div[@class='lessons__new-item-time']"))
                    .getText();
            nameAndDate.put(element, new DataTableCourse(nameCourse, dateCourse));
        }
        return nameAndDate;
    }

    private WebElement getMinMaxDateOfCourse(HashMap<WebElement, DataTableCourse> nameAndDate, Boolean minMax) {
        for (Map.Entry<WebElement, DataTableCourse> entry : nameAndDate.entrySet()) {
            Date date = dateParser(entry.getValue().getDateString());
            if (date != null) {
                entry.getValue().setDate(date);
            }
        }
        BinaryOperator<Map.Entry<WebElement, DataTableCourse>> binaryOperator = minMax ?
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s1 : s2) :
                (Map.Entry<WebElement, DataTableCourse> s1, Map.Entry<WebElement, DataTableCourse> s2)
                        -> (s1.getValue().getDate().after(s2.getValue().getDate()) ? s2 : s1);
        WebElement result = nameAndDate.entrySet().stream()
                .filter(p -> p.getValue().getDate() != null)
                .reduce(binaryOperator)
                .map(p -> p.getKey())
                .get();
        return result;
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

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        try {
            actions.moveToElement(element).build().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clickToElement(WebElement element) {
        element.click();
    }
}
