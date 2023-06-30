package com.bitc.full505_team2_project.crowling;

import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class Selenium {
    private WebDriver driver;
    private WebElement element;
    private String url;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";

    public Selenium() {
        // WebDriver 경로 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");

        driver = new ChromeDriver(options);


    }

    public void activateBot() {
        url = "https://www.melon.com/chart/index.htm";
        try {
            driver.get(url);

            Thread.sleep(2000);

            // 곡 제목 파싱
            // /html/body/div[1]/div[3]/div/div/div[3]/form/div/table/tbody/tr[1]/td[6]/div/div/div[1]/span/a
            element = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[3]/form/div/table/tbody/tr[1]/td[6]/div/div/div[1]/span/a"));
            String title = element.getAttribute("title");

            // 좋아요 수 파싱
            element = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[3]/form/div/table/tbody/tr[1]/td[8]/div/button/span[2]"));
            String cntLike = element.getText();


            System.out.println("1위 노래는 [" + title + "]입니다.");
            System.out.println("좋아요 수는 [" + cntLike + "]입니다.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }

    public List<MovieTimeTableDto> getCgvSchedule(String targetTitle) {
        String areacode = "05%2C207";
        String theaterCode = "0005";
        String date = "20230630";
        String url = String.format("http://www.cgv.co.kr/reserve/show-times/?areacode=%s&theaterCode=%s&date=%s", areacode, theaterCode, date);
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        try {
            driver.get(url);

            // iframe 찾기
            Duration duration = Duration.ofSeconds(60);
            // WebDriverWait 객체 생성
            WebDriverWait wait = new WebDriverWait(driver, duration);

            // iframe 찾기 및 전환
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("#ifrm_movie_time_table")));
            System.out.println("iframe loaded");

            // iframe 내부 요소에 접근하여 조작
            List<WebElement> elements = driver.findElements(By.cssSelector("div.sect-showtimes div.col-times"));

            for (WebElement item : elements) {
                WebElement titleItem = item.findElement(By.cssSelector("div.info-movie a"));

                if (titleItem.getText().equals(targetTitle)) {
                    List<WebElement> typeHalls = item.findElements(By.cssSelector("div.type-hall"));

                    for (WebElement typeHall : typeHalls) {
                        MovieTimeTableDto hallAndtimeTable = new MovieTimeTableDto();
                        hallAndtimeTable.setHall(typeHall.findElement(By.cssSelector("li:first-child")).getText());
                        hallAndtimeTable.setTotalSeat(typeHall.findElement(By.cssSelector("li:nth-child(3)")).getText());
                        List<WebElement> timeTableList = typeHall.findElements(By.cssSelector("div.info-timetable li"));

                        for (WebElement timeTable : timeTableList) {
                            String time = timeTable.findElement(By.cssSelector("em")).getText();
                            String seat = timeTable.findElement(By.cssSelector("span")).getText();
                            hallAndtimeTable.addTimeAndSeat(time, seat);
                        }
                        dtoList.add(hallAndtimeTable);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
        return dtoList;
    }
}
