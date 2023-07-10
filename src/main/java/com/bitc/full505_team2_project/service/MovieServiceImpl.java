package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;
import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieDetailDTO;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
//(영화 검색, 금일 박스오피스 순위 를 보여주기 위한 서비스),사용하진 않음 예비용
@Service
public class MovieServiceImpl implements MovieService {

    @Override
    public List<DailyMovieDTO> getDailyMovieList(String strUrl) throws Exception {
        List<DailyMovieDTO> itemList = null;
        URL url = null;
        HttpURLConnection urlConn = null;
        BufferedReader reader = null;;
        try {
            url = new URL(strUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            Gson gson = new Gson();
            MovieDTO movie = gson.fromJson(sb.toString(),MovieDTO.class);
            itemList = movie.getMovieResultDTO().getDailyBoxOfficeList();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            {
                if (reader != null){
                    urlConn.disconnect();
                }
            }
        }
        return itemList;
    }

    @Override
    public List<MovieDetailDTO> getMovieResarchList(String strUrl) throws Exception {
        List<MovieDetailDTO> itemList = null;
        URL url = null;
        HttpURLConnection urlConn = null;
        BufferedReader reader = null;;
        try {
            url = new URL(strUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            Gson gson = new Gson();
            MovieDTO movie = gson.fromJson(sb.toString(),MovieDTO.class);
            itemList = movie.getMovieResultDTO().getMovieDetailList();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            {
                if (reader != null){
                    urlConn.disconnect();
                }
            }
        }
        return itemList;
    }
}
