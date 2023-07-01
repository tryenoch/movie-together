package com.bitc.full505_team2_project.crowling;

import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class Selenium {
    private WebDriver driver;
    private WebElement element;
    private String url;
    private String targetDate;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";

    public Selenium(String targetDate) {
        this.targetDate = targetDate;
    }

    // 테스트용 메소드
    public void activateBot() {
        url = "https://www.melon.com/chart/index.htm";
        try {
            // WebDriver 경로 설정
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

            // WebDriver 옵션 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-popup-blocking");

            driver = new ChromeDriver(options);

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
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        // 지역: 부산/울산 지역, 굳이 극장이 있는 지역과 일치하지 않아도됨
        // (서울지역코드를 입력해도 극장을 0005로 선택하면 cgv서면의 시간표가 출력됨)
        String areacode = "05%2C207";
        String theaterCode = "0005"; // 극장: cgv서면
        String date = targetDate;
        String url = String.format("http://www.cgv.co.kr/reserve/show-times?areacode=%s&theaterCode=%s&date=%s", areacode, theaterCode, date);
        try {
            // WebDriver 경로 설정
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

            // WebDriver 옵션 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-popup-blocking");

            driver = new ChromeDriver(options);
            driver.get(url);

            // 대기시간 duration 객체 생성
            Duration duration = Duration.ofSeconds(60);
            // WebDriverWait 객체 생성 : 페이지가 열릴때까지 duration만큼 기다림
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
                        hallAndtimeTable.setHall(typeHall.findElement(By.cssSelector("li:nth-child(2)")).getText() + "-"+typeHall.findElement(By.cssSelector("li:first-child")).getText());
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

    public List<MovieTimeTableDto> getMegaBoxSchedule(String targetTitle) {

        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        String urlString = "https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";

        //  원하는 극장을 선택
        String dataAreaCode = "55"; // 지역코드 : 부산/대구/경상
        String dataBrchNo = "6001"; // 극장코드 : 부산극장


        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // POST 데이터 설정
            String postData = "{" +
                    "\"brchNo\":\"6001\", " +
                    "\"brchNo1\":\"6001\", " +
                    "\"crtDe\":\""+targetDate+"\", " +
                    "\"detailType\":\"area\", " +
                    "\"firstAt\":\"N\", " +
                    "\"masterType\":\"brch\", " +
                    "\"playDe\":\""+targetDate+"\" " +
                    "}";

            // POST 데이터 전송
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(postData);
            outputStream.flush();
            outputStream.close();

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // JSON 파싱 또는 처리
                String jsonResponse = response.toString();

                // Jackson ObjectMapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON 데이터 파싱
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode movieFormList = jsonNode.get("megaMap").get("movieFormList");

                // movieFormList(영화제목과 상영관, 시간표 등의 정보를 가진 jsonNode) 배열 순회
                int dtoListIdx = 0;
                for (int i = 0; i < movieFormList.size(); i++) {
                    JsonNode movieNode = movieFormList.get(i);
                    String rpstMovieNm = movieNode.get("rpstMovieNm").asText();
                    String theabExpoNm = movieNode.get("theabExpoNm").asText(); // 상영관
                    String playKindNm = movieNode.get("playKindNm").asText(); // 상영종류 ex) 2d(자막)
                    playKindNm = playKindNm.replace("&#40;","(").replace("&#41;",")");
                    String playStartTime = movieNode.get("playStartTime").asText();
                    String totSeatCnt = movieNode.get("totSeatCnt").asText();
                    String restSeatCnt = movieNode.get("restSeatCnt").asText();
                    String hall = theabExpoNm + "-" + playKindNm; // MovieTimeTableDto에 들어갈 hall값
                    if(rpstMovieNm.equals(targetTitle)){
                        if(dtoList.isEmpty()){
                            MovieTimeTableDto movieDto = new MovieTimeTableDto(hall, totSeatCnt);
                            movieDto.addTimeAndSeat(playStartTime, restSeatCnt);
                            dtoList.add(movieDto);
                        }
                        else {
                            if(dtoList.size()>dtoListIdx && dtoList.get(dtoListIdx).getHall().equals(hall)){
                                dtoList.get(dtoListIdx).addTimeAndSeat(playStartTime, restSeatCnt);
                            }
                            else{
                                MovieTimeTableDto movieDto = new MovieTimeTableDto(hall, totSeatCnt);
                                movieDto.addTimeAndSeat(playStartTime, restSeatCnt);
                                dtoList.add(movieDto);
                                dtoListIdx += 1;
                            }
                        }
                    }

                }
//                System.out.println(dtoList);
            } else {
                System.out.println("HTTP request failed. Response Code: " + responseCode);
            }

            // 연결 닫기
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }
}
