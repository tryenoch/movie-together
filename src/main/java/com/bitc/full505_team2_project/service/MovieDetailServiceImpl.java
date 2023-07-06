package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.mapper.MovieDetailMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Driver;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class MovieDetailServiceImpl implements MovieDetailService {

    @Autowired MovieDetailMapper mdm;
    @Override
    public void makeData(String movieTitle) throws Exception {
        mdm.makeData(movieTitle);
    }

    @Override
    public int getMoviePk(String movieTitle) throws Exception {
        int pk = mdm.getMoviePk(movieTitle);
        return pk;
    }
    @Override
    public MovieDTO selectMovieInfo(int moviePk) throws Exception {
        MovieDTO movie = mdm.selectMovieInfo(moviePk);
        return movie;
    }


    // 아래는 크롤링

    private WebDriver driver;
    private WebElement element;
    private String url;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH = "C:\\chromeDriver\\chromedriver.exe";
    @Override
    public List<MovieTimeTableDto> getCgvSchedule(String title, String date) throws Exception{
        // 이미 켜져있던 크롬드라이버를 강제종료하는 트라이캐치문
//        try {
//            Process process = Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe /t");
//            int exitCode = process.waitFor();
////            if (exitCode == 0) {
////                System.out.println("크롬드라이버 강제 종료 성공");
////            } else {
////                System.out.println("크롬드라이버 강제 종료 실패");
////            }
//        } catch (IOException | InterruptedException e) {
////            System.out.println("크롬드라이버 강제 종료 실패");
////            e.printStackTrace();
//        }
        String[] targetTitle = this.titleSplit(title);
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        // 지역: 부산/울산 지역, 굳이 극장이 있는 지역과 일치하지 않아도됨
        // (서울지역코드를 입력해도 극장을 0005로 선택하면 cgv서면의 시간표가 출력됨)
        String areacode = "05%2C207";
        String theaterCode = "0005"; // 극장: cgv서면
        String url = String.format("http://www.cgv.co.kr/reserve/show-times?areacode=%s&theaterCode=%s&date=%s", areacode, theaterCode, date);
        Timer timer = new Timer();

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
            Duration duration = Duration.ofSeconds(5);
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

            // 이미 켜져있던 크롬드라이버를 강제종료하는 트라이캐치문
        try {
            Process process = Runtime.getRuntime().exec("taskkill /f /im chromedriver.exe /t");
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e2) {}

        } finally {
            driver.quit();
        }

        return dtoList;
    }

    @Override
    public List<MovieTimeTableDto> getMegaBoxSchedule(String title, String date) throws Exception {
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
                    "\"crtDe\":\""+date+"\", " +
                    "\"detailType\":\"area\", " +
                    "\"firstAt\":\"N\", " +
                    "\"masterType\":\"brch\", " +
                    "\"playDe\":\""+date+"\" " +
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

    @Override
    public List<MovieTimeTableDto> getLotteCinemaSchedule(String title, String date) throws Exception {
        String[] targetTitle = this.titleSplit(title);
        List<MovieTimeTableDto> dtoList = new ArrayList<>();
        String url  = "https://www.lottecinema.co.kr/LCWS/Ticketing/TicketingData.aspx";

        //  원하는 극장을 선택
        String cinemaID = "1|0101|2004"; // 극장코드 : 부산본점

        // 날짜 형식 바꾸기
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            // HTTP 클라이언트 생성 및 요청 전송
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


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

    @Override
    public String getDaumId(String title) throws Exception {

        String json = "";
        String daumId = "";

        try {
            String encodedTitle = URLEncoder.encode(title,"UTF-8");
            String url = "https://movie.daum.net/api/search/all?q="+encodedTitle+"&size=1";

            // URL 객체 생성
            URL urlObj = new URL(url);

            // HttpURLConnection 객체 생성 및 설정
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 데이터 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                json = response.toString();
                // 받아온 HTML 데이터 출력
//                System.out.println(json);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(json);

                // 원하는 값을 가져오기 위한 경로를 지정하여 JsonNode 객체를 찾습니다.
                JsonNode movieIdNode = rootNode.path("movie").path("result").path("search_result")
                        .path("documents").get(0).path("document").path("movieId");

                // JsonNode에서 원하는 값을 가져옵니다.
                daumId = movieIdNode.asText();

//                // 가져온 값을 출력합니다.
//                System.out.println("Movie ID: " + daumId);

            } else {
                System.out.println("HTTP Error: " + responseCode);
            }

            // 연결 해제
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return daumId;
    }

    @Override
    public MovieDTO addDaumInfo(MovieDTO dto, String daumId) throws Exception {
        String json = "";

        try {
            String url = "https://movie.daum.net/api/movie/"+daumId+"/main";

            // URL 객체 생성
            URL urlObj = new URL(url);

            // HttpURLConnection 객체 생성 및 설정
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 데이터 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                json = response.toString();
                // 받아온 HTML 데이터 출력
//                System.out.println(json);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(json);
//
//                // 원하는 값을 가져오기 위한 경로를 지정하여 JsonNode 객체를 찾습니다.
                JsonNode common = rootNode.path("movieCommon");
                ArrayList<String> genres = new ArrayList<>();
                for(JsonNode genre : common.path("genres")) {
                    genres.add(genre.asText());
                }
                dto.setGenres(genres);
                ArrayList<String> nations = new ArrayList<>();
                for(JsonNode nation : common.path("productionCountries")){
                    nations.add(nation.asText());
                }
                dto.setNations(nations);

                for(JsonNode countryMovieInformation : common.path("countryMovieInformation")) {
                    if(countryMovieInformation.path("country").path("id").asText().equals("KR")) {
                        dto.setGradeAge(countryMovieInformation.path("admissionCode").asText());
                        dto.setRunningTime(countryMovieInformation.path("duration").asText());
                        dto.setReleaseDate(countryMovieInformation.path("releaseDate").asText());
                    }
                }
                dto.setAudience(common.path("totalAudienceCount").asText());
                dto.setBoxOfficeRank(common.path("reservationRank").asText());

                ArrayList<String> casts = new ArrayList<>();
                for(JsonNode cast : rootNode.path("casts")) {
                    casts.add(cast.path("nameKorean").asText() + "(" + cast.path("movieJob").path("role").asText()+ ")");
                }
                dto.setCasts(casts);

            } else {
                System.out.println("HTTP Error: " + responseCode);
            }

            // 연결 해제
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dto;
    }

    @Override
    public String getLikedList(String id) throws Exception {
        String likedList = mdm.getLikedList(id);
        return likedList;
    }

    @Override
    public int setLikedList(String id, String likedList, String pk, boolean type) throws Exception {
        mdm.setLikedList(id, likedList);
        int moviePk = Integer.parseInt(pk);
        if(type){
            mdm.plusLikeCnt(moviePk);
        } else {
            mdm.minusLikeCnt(moviePk);
        }
        return mdm.likeCnt(moviePk);
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
