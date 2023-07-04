package com.bitc.full505_team2_project.crowling;

import com.bitc.full505_team2_project.dto.MovieTimeTableDto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TimeTable {
    private WebDriver driver;
    private WebElement element;
    private String url;
    private String targetDate;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";

    public TimeTable(String targetDate) {
        this.targetDate = targetDate;
    }

    public List<MovieTimeTableDto> getCgvSchedule(String title) {

        String[] targetTitle = this.titleSplit(title);
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
            options.addArguments("--disable-popup-blocking"); // 팝업 안띄움
            options.addArguments("--enable-automation");
            options.addArguments("--window-position=-100000,-100000");
            options.addArguments("--window-size=0,0");
            options.addArguments("--lang=ko");
            options.addArguments("--disable-gpu");            //gpu 비활성화
            options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음

            driver = new ChromeDriver(options);
            driver.get(url);

            // 대기시간 duration 객체 생성
            Duration duration = Duration.ofSeconds(60);
            // WebDriverWait 객체 생성 : 페이지가 열릴때까지 duration만큼 기다림
            WebDriverWait wait = new WebDriverWait(driver, duration);

            // iframe 찾기 및 전환
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("#ifrm_movie_time_table")));

            // iframe 내부 요소에 접근하여 조작
//            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("div.sect-showtimes")));
            List<WebElement> elements = driver.findElements(By.cssSelector("div.sect-showtimes div.col-times"));

            for (WebElement item : elements) {
                WebElement titleItem = item.findElement(By.cssSelector("div.info-movie a")); // 영화제목과 영화 링크를 가지는 요소
                String[] titleInPage = this.titleSplit(titleItem.getText());
                if (titleInPage[0].equals(targetTitle[0]) && titleInPage[1].equals(targetTitle[1])) {
                    List<WebElement> typeHalls = item.findElements(By.cssSelector("div.type-hall"));

                    for (WebElement typeHall : typeHalls) {
                        MovieTimeTableDto hallAndtimeTable = new MovieTimeTableDto();
                        hallAndtimeTable.setHall(

                                typeHall.findElement(By.cssSelector("li:nth-child(2)")).getText() +  // 상영관
                                        "-" +
                                        typeHall.findElement(By.cssSelector("li:first-child")).getText() // 상영종류(2d 등)
                        );
                        hallAndtimeTable.setTotalSeat(typeHall.findElement(By.cssSelector("li:nth-child(3)")).getText() // 상영관 총 좌석수
                                .replace("총 ","").replace("석","")); // 숫자만 뽑기
                        List<WebElement> timeTableList = typeHall.findElements(By.cssSelector("div.info-timetable li"));

                        for (WebElement timeTable : timeTableList) {
                            String time = timeTable.findElement(By.cssSelector("em")).getText();
                            String seat = timeTable.findElement(By.cssSelector("span")).getText().replace("석","");
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

    public List<MovieTimeTableDto> getMegaBoxSchedule(String title) {
        String[] targetTitle = this.titleSplit(title);
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        String urlString = "https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";

        //  원하는 극장을 선택
        String dataAreaCode = "55"; // 지역코드 : 부산/대구/경상, 사용하지는 않음
        String dataBrchNo = "6001"; // 극장코드 : 부산극장


        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // POST 데이터 설정
            String postData = "{" +
                    "\"brchNo\":\""+dataBrchNo+"\", " +
                    "\"brchNo1\":\""+dataBrchNo+"\", " +
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
                    String[] titleInPage = this.titleSplit(rpstMovieNm);
                    if (titleInPage[0].equals(targetTitle[0]) && titleInPage[1].equals(targetTitle[1])){
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

    public List<MovieTimeTableDto> getLotteCinemaSchedule(String title) {
        String[] targetTitle = this.titleSplit(title);
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        String url  = "https://www.lottecinema.co.kr/LCWS/Ticketing/TicketingData.aspx";

        //  원하는 극장을 선택
        String cinemaID = "1|0101|2004"; // 극장코드 : 부산본점

        // 날짜 형식 바꾸기
        LocalDate date = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 롯데시네마가 paramList라는 key의 value로 아래와같은 map타입을 받으므로 이를 문자열로 표현
        String paramList = "{\"MethodName\":\"GetPlaySequence\"," +
                "\"channelType\":\"HO\"," +
                "\"osType\":\"W\"," +
                "\"osVersion\":\"\"," +
                "\"playDate\":\""+formattedDate+"\"," +
                "\"cinemaID\":\""+cinemaID+"\"," +
                "\"representationMovieCode\":\"\"}";

        // 요청 바디 구성
        // 롯데시네마는 multipart/fomr-data를 post타입으로 받는데 이는 바운더리에 둘러쌓인 형태임
        String requestBody = "--boundary\r\n"
                + "Content-Disposition: form-data; name=\"paramList\"\r\n\r\n"
                + paramList + "\r\n"
                + "--boundary--";

        // 요청 헤더 설정
        // post로 보내지는 값의 형식과 바운더리를 설정
        String contentTypeHeader = "multipart/form-data; boundary=boundary";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", contentTypeHeader)
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        try {
            // HTTP 클라이언트 생성 및 요청 전송
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());


            // 응답 처리
            int statusCode = response.statusCode();
            HttpHeaders headers = response.headers();
            String responseBody = response.body();


            // Jackson ObjectMapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON 데이터 파싱
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode items = jsonNode.get("PlaySeqs").get("Items");

            for (int i = 0; i < items.size(); i++) {
                JsonNode movieNode = items.get(i);
                String MovieNameKR = movieNode.get("MovieNameKR").asText();

                String IsBookingYN = null; // 영화관 정보인지 실제 시간표 정보인지
                if(movieNode.get("IsBookingYN") != null){
                    IsBookingYN = movieNode.get("IsBookingYN").asText();
                } else {
                    continue;
                }
                String TranslationDivisionCode = movieNode.get("TranslationDivisionCode").asText(); // 자막여부
                if(TranslationDivisionCode.equals("100")) {
                    TranslationDivisionCode = "자막";
                } else if (TranslationDivisionCode.equals("50")) {
                    TranslationDivisionCode = "더빙";
                } else {
                    TranslationDivisionCode = "";
                }
                String ScreenNameKR = movieNode.get("ScreenNameKR").asText(); // 몇관인지
                String FilmNameKR = movieNode.get("FilmNameKR").asText(); // 2d 3d
                String ScreenDivisionNameKR = movieNode.get("ScreenDivisionNameKR").asText(); // 좌석분리
                if(ScreenDivisionNameKR.equals("일반")) {
                    ScreenDivisionNameKR = "";
                } else if (!TranslationDivisionCode.equals("")) {
                    ScreenDivisionNameKR = " " + ScreenDivisionNameKR;
                }
                // MovieTimeTableDto에 들어갈 hall값 ex) 3관-2D(자막)
                String hall;
                if((TranslationDivisionCode + ScreenDivisionNameKR).equals("")){
                    hall = ScreenNameKR;
                } else {
                    hall = ScreenNameKR + "-" + FilmNameKR +"("+TranslationDivisionCode + ScreenDivisionNameKR+")";
                }

                String StartTime = movieNode.get("StartTime").asText();
                String TotalSeatCount = movieNode.get("TotalSeatCount").asText();
                String BookingSeatCount = movieNode.get("BookingSeatCount").asText();

                String[] titleInPage = titleSplit(MovieNameKR);
                if(IsBookingYN != null && titleInPage[0].equals(targetTitle[0]) && titleInPage[1].equals(targetTitle[1])){
                    if(dtoList.isEmpty()){
                        MovieTimeTableDto movieDto = new MovieTimeTableDto(hall, TotalSeatCount);
                        movieDto.addTimeAndSeat(StartTime, BookingSeatCount);
                        dtoList.add(movieDto);
                    }
                    else {
                        for(int j = 0; j < dtoList.size(); j++){
                            if(dtoList.get(j).getHall().equals(hall)){
                                dtoList.get(j).addTimeAndSeat(StartTime, BookingSeatCount);
                                break;
                            }
                            else if(j == dtoList.size()-1){
                                MovieTimeTableDto movieDto = new MovieTimeTableDto(hall, TotalSeatCount);
                                movieDto.addTimeAndSeat(StartTime, BookingSeatCount);
                                dtoList.add(movieDto);
                            }
                        }
                    }
                }

            }
            return dtoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    public String[] titleSplit(String title) {
        String[] str = new String[2];

        if(title.contains(":")){
            str = title.split(":",2);
        }
        else if (title.contains("-")) {
            str = title.split("-",2);
        }
        else if (title.contains("\"")) {
            str = title.split("\"", 3);
        }
        else {
            str[0] = title;
            str[1] = "";
        }
        for (int i = 0; i < str.length; i++){
            str[i] = str[i].strip();
        }
        return str;
    }
}
