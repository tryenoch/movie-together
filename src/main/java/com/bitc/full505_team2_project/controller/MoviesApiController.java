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


@Controller // 메인 화면으로 보내주는 컨트롤러 -kdu
public class MoviesApiController {
    @Autowired
    private  MovieService movieService;
    @GetMapping({"/MovieTogetherMain", "/"})
    public String MovieTogetherView() throws Exception{
        return "MovieTogetherMain";
    }
//    메인 화면 Controller((실제로 실행되진 않음 : 자바스크립쪽으로 바로 받기 떄문, 예비용)) - kdu
    @ResponseBody
    @PostMapping("/MovieTogetherMain")
    public Object MovieTogetherProcess(@RequestParam("targetDt")String targetDt)throws Exception{
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=0cef8373c2ef480da57a59e3967ca38f&targetDt="+targetDt;
        List<DailyMovieDTO> dailyMovieDTOList = movieService.getDailyMovieList(url);
        return dailyMovieDTOList;
    }
//    영화 검색 Controller
    @GetMapping("/MovieTogeherSearch")
    public String MTSearchView() throws Exception{
        return "MTResarch";
    }
    @PostMapping("/MovieTogeherSearch") // 영화 검색 실행 프로세스 컨트롤러(실제로 실행되진 않음 : 자바스크립쪽으로 바로 받기 떄문, 예비용)
    public Object MTSearchProcess(@RequestParam("movie_name")String movie_name)throws Exception{
        String url = "https://api.themoviedb.org/3/search/movie?api_key=ad2f7390e457d7dc76e7fda8dcae77b2&language=ko-KR&page=1&query="+movie_name;
        List<MovieDetailDTO> MovieResarchList = movieService.getMovieResarchList(url);
        return MovieResarchList;
    }
    @GetMapping("/MovieList") // 영화 리스트를 받아오는 컨트롤러 kdu
    public String MovieList() throws Exception{
        return "MovieList20";
    }
        }