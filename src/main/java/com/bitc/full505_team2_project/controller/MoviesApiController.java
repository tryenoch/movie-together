package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.DailyMovieDTO;
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

//    @GetMapping("Movie")
//    public String getApi(){
//        HashMap<String, Object> result = new HashMap<String,Object>();
//        String jsonInString = "";
//
//        try{
//            RestTemplate restTemplate = new RestTemplate();
//
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<?> entity = new HttpEntity<>(headers);
//            String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
//            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url+"?"+"key=0cef8373c2ef480da57a59e3967ca38f&targetDt=20230605").build();
//
//            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
//            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
//            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
//            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인
//
//            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
//            ObjectMapper mapper = new ObjectMapper();
//            jsonInString = mapper.writeValueAsString(resultMap.getBody());
//
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            result.put("statusCode", e.getRawStatusCode());
//            result.put("body"  , e.getStatusText());
//            System.out.println(e.toString());
//
//        } catch (Exception e) {
//            result.put("statusCode", "999");
//            result.put("body"  , "excpetion오류");
//            System.out.println(e.toString());
//        }
//        return jsonInString;
//    }
    @GetMapping("/MovieTogetherMain")
    public String MovieTogetherView() throws Exception{
        return "MovieTogetherMain";
    }
    @ResponseBody
    @PostMapping("/MovieTogetherMain")
    public Object MovieTogetherProcess(@RequestParam("targetDt")String targetDt)throws Exception{
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=0cef8373c2ef480da57a59e3967ca38f&targetDt="+targetDt;

        List<DailyMovieDTO> dailyMovieDTOList = movieService.getDailyMovieList(url);
        return dailyMovieDTOList;
    }
        }