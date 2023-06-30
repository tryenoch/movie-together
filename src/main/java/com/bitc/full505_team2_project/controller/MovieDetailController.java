package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.crowling.JsoupCrowling;
import com.bitc.full505_team2_project.dto.MovieDto;
import com.bitc.full505_team2_project.crowling.Selenium;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.service.MovieDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        MovieDto movie = mds.selectMovieInfo(pk);
        mv.addObject("movie",movie);

        Selenium selenium = new Selenium();
        List<MovieTimeTableDto> timeTable = selenium.getCgvSchedule("스파이더맨-어크로스 더 유니버스");
        System.out.println(timeTable);
        mv.addObject("timeTable", timeTable);

        return mv;
    }
}
