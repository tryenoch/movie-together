package com.bitc.full505_team2_project.crowling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MovieInfo {
    public Map<String,String> getInfo(String title) {
        TimeTable timeTable = new TimeTable("20230705");
        String[] targetTitle = timeTable.titleSplit(title);
        HashMap<String,String> info = new HashMap<>();

        String url = "https://movie.daum.net/api/search/all?q="+title+"&size=1"; // 접속할 URL

        try {
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

                // 받아온 HTML 데이터 출력
                System.out.println(response.toString());
            } else {
                System.out.println("HTTP Error: " + responseCode);
            }

            // 연결 해제
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info;
    }
}
