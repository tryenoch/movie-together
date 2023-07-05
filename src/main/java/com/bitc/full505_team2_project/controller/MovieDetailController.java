package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.service.MovieDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value = "/detail")
public class MovieDetailController {
    @Autowired MovieDetailService mds;

    @RequestMapping(value = "/list")
    public String movieList() throws Exception {
        return "movie/movieListEX";
    }

    // db에서 영화제목에 해당하는 pk 가져오기/없으면 새로만들어서 가져오기
    // 가져온 pk값으로 디테일 페이지로 이동
    @RequestMapping(value = "/data/{movieTitle}", method = RequestMethod.GET)
    public ModelAndView makeMovieData(@PathVariable String movieTitle) throws Exception {
        ModelAndView mv = new ModelAndView("movie/detail");

        int pk=0;
        // 영화제목에 해당하는 pk값을 가져옴
        try{pk = mds.getMoviePk(movieTitle);}
        // db에 저장되지 않은 경우 오류가 발생하므로 db에 새로운 영화제목을 저장후 pk값을 가져옴
        catch(Exception e){
            mds.makeData(movieTitle);
            pk = mds.getMoviePk(movieTitle);
        }
        mv.addObject("pk",pk);
        return mv;
    }

    // movie/detail 페이지에서 ajax통신하여 json파일로 영화정보 보내주기
    @ResponseBody
    @RequestMapping(value = "/getInfo/{moviePk}", method = RequestMethod.GET)
    public Object getInfo(@PathVariable String moviePk) throws Exception {
        int pk = Integer.parseInt(moviePk);

        // pk를 가지고 영화정보(pk, title, likeCnt)를 db에서가져옴
        MovieDTO movie = mds.selectMovieInfo(pk);

        // 다음에서 검색한 영화정보를 추가함
        String daumId = mds.getDaumId(movie.getMovieTitle());
        movie = mds.addDaumInfo(movie, daumId);
        System.out.println(movie);

        return movie;
    }

    @ResponseBody
    @RequestMapping(value = "/cgv/getTimetable.do", method = RequestMethod.GET)
    public Object cgvAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getCgvSchedule(title, date);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/megaBox/getTimetable.do", method = RequestMethod.GET)
    public Object megaBoxAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getMegaBoxSchedule(title, date);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/lotteCinema/getTimetable.do", method = RequestMethod.GET)
    public Object lotteCinemaAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getLotteCinemaSchedule(title, date);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
}