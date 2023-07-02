package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.crowling.TimeTable;
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

        // pk를 가지고 영화정보(pk, title, likeCnt)를 db에서가져옴
        MovieDTO movie = mds.selectMovieInfo(pk);
        mv.addObject("movie",movie);

        // 날짜 설정해서 TimeTable클래스 객체 생성후 클래스의 메소드를 이용하여 3개 영화관에서 시간표를 가져옴
        // 제목과 부제목이 :이나 - 아무거나 써도 알아서 각 영화관 홈페이지의 양식에 맞게 변환하여 검색해줌
        TimeTable timeTable = new TimeTable("20230703");
        List<MovieTimeTableDto> timeTableCgv = timeTable.getCgvSchedule("인디아나 존스:운명의 다이얼");
        System.out.println(timeTableCgv);
        List<MovieTimeTableDto> timeTableMegaBox = timeTable.getMegaBoxSchedule("인디아나 존스:운명의 다이얼");
        System.out.println(timeTableMegaBox);
        List<MovieTimeTableDto> timeTableLC = timeTable.getLotteCinemaSchedule("인디아나 존스:운명의 다이얼");
        System.out.println(timeTableLC);

//        mv.addObject("timeTableCgv", timeTableCgv);

        return mv;
    }
    @RequestMapping(value = "/megabox")
    public String megaboxText() throws Exception {
        return "redirect:https://www.megabox.co.kr/on/oh/ohc/Brch/schedulePage.do";
    }
}
