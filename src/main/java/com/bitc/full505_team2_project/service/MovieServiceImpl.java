package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;
import com.bitc.full505_team2_project.dto.MovieDTO;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
}
