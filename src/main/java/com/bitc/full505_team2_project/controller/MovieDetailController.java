package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.MovieDto;
import com.bitc.full505_team2_project.service.MovieDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/detail")
public class MovieDetailController {
    @Autowired MovieDetailService mds;

    @RequestMapping(value = "/list")
    public String movieList() throws Exception {
        return "movie/movieListEX";
    }

    // 메인페이지에서 영화를 선택했을 때 db에 해당 영화의 데이터를 생성하겨나
// 이미 존재하는 경우에는 생성하지 않고 movie_pk값을 받아와서 해당 MovieDetailView로 이동
    @RequestMapping(value = "/data/{movieTitle}", method = RequestMethod.GET)
    public String makeMovieData(@PathVariable String movieTitle) throws Exception {
        mds.makeData(movieTitle);
        int pk = mds.getMoviePk(movieTitle);
        return "redirect:/detail/view/" + String.valueOf(pk);
    }

    @RequestMapping(value = "/view/{moviePk}", method = RequestMethod.GET)
    public ModelAndView movieDetailView( @PathVariable String moviePk) throws Exception {
        int pk = Integer.parseInt(moviePk);
        ModelAndView mv = new ModelAndView("movie/detail");
        MovieDto movie = mds.selectMovieInfo(pk);
        mv.addObject("movie",movie);
        return mv;
    }
}
