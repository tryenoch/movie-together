package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.crowling.MovieInfo;
import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.crowling.TimeTable;
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
    public String makeMovieData(@PathVariable String movieTitle) throws Exception {
        int pk;
        // 영화제목에 해당하는 pk값을 가져옴
        try{pk = mds.getMoviePk(movieTitle);}
        // db에 저장되지 않은 경우 오류가 발생하므로 db에 새로운 영화제목을 저장후 pk값을 가져옴
        catch(Exception e){
            mds.makeData(movieTitle);
            pk = mds.getMoviePk(movieTitle);
        }

        return "redirect:/detail/view/" + String.valueOf(pk);
    }

    // pk값을 이용하여 영화 정보를 가져와서 modelAndView 반환
    @RequestMapping(value = "/view/{moviePk}", method = RequestMethod.GET)
    public ModelAndView movieDetailView( @PathVariable String moviePk) throws Exception {
        int pk = Integer.parseInt(moviePk);
        ModelAndView mv = new ModelAndView("movie/detail");

        // pk를 가지고 영화정보(pk, title, likeCnt)를 db에서가져옴
        MovieDTO movie = mds.selectMovieInfo(pk);
        mv.addObject("movie",movie);

        MovieInfo movieInfo = new MovieInfo();
        movieInfo.getInfo(movie.getMovieTitle());

        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/cgv/getTimetable.do", method = RequestMethod.GET)
    public Object cgvAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        TimeTable timeTable = new TimeTable(date);
        List<MovieTimeTableDto> timeTableMB = timeTable.getCgvSchedule(title);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/megaBox/getTimetable.do", method = RequestMethod.GET)
    public Object megaBoxAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        TimeTable timeTable = new TimeTable(date);
        List<MovieTimeTableDto> timeTableMB = timeTable.getMegaBoxSchedule(title);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/lotteCinema/getTimetable.do", method = RequestMethod.GET)
    public Object lotteCinemaAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        TimeTable timeTable = new TimeTable(date);
        List<MovieTimeTableDto> timeTableMB = timeTable.getLotteCinemaSchedule(title);
        System.out.println(timeTableMB);
        return timeTableMB;
    }
}
