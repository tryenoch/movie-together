package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;
import com.bitc.full505_team2_project.dto.MovieDetailDTO;
import com.bitc.full505_team2_project.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class MoviesApiController {
    @Autowired
    private  MovieService movieService;
    @GetMapping("/MovieTogetherMain")
    public String MovieTogetherView() throws Exception{
        return "MovieTogetherMain";
    }
//    메인 화면 Controller
    @ResponseBody
    @PostMapping("/MovieTogetherMain")
    public Object MovieTogetherProcess(@RequestParam("targetDt")String targetDt)throws Exception{
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=0cef8373c2ef480da57a59e3967ca38f&targetDt="+targetDt;
//        String url2 = "https://api.themoviedb.org/3/search/movie?api_key=ad2f7390e457d7dc76e7fda8dcae77b2&language=ko-KR&page=1&query="+"엘리멘탈";
        List<DailyMovieDTO> dailyMovieDTOList = movieService.getDailyMovieList(url);
        return dailyMovieDTOList;
    }
//    영화 검색 Controller
    @GetMapping("/MovieTogeherSearch")
    public String MTSearchView() throws Exception{
        return "MTResarch";
    }
    @PostMapping("/MovieTogeherSearch")
    public Object MTSearchProcess(@RequestParam("movie_name")String movie_name)throws Exception{
        String url = "https://api.themoviedb.org/3/search/movie?api_key=ad2f7390e457d7dc76e7fda8dcae77b2&language=ko-KR&page=1&query="+movie_name;
        List<MovieDetailDTO> MovieResarchList = movieService.getMovieResarchList(url);
        return MovieResarchList;
    }
    @GetMapping("/MovieList")
    public String MovieList() throws Exception{
        return "MovieList20";
    }
        }