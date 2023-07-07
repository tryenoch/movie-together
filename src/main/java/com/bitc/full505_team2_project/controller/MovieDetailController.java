package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.dto.ReviewDto;
import com.bitc.full505_team2_project.service.MovieDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value = "/detail")
public class MovieDetailController {
    @Autowired MovieDetailService mds;

    // db에서 영화제목에 해당하는 pk 가져오기/없으면 새로만들어서 가져오기
    // 가져온 pk값으로 디테일 페이지로 이동
    @RequestMapping(value = "/data/{movieTitle}", method = RequestMethod.GET)
    public ModelAndView makeMovieData(@PathVariable String movieTitle, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

        // 로그인상태면
        HttpSession session = request.getSession();
        if(session.getAttribute("userId") != null){
            mv.addObject("userId",session.getAttribute("userId"));
//            mv.addObject("userName",session.getAttribute("userName"));
//            mv.addObject("userEmail",session.getAttribute("userEmail"));
//            mv.addObject("userGrade",session.getAttribute("userGrade"));
        }

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

        return movie;
    }

    @ResponseBody
    @RequestMapping(value = "/cgv/getTimetable.do", method = RequestMethod.GET)
    public Object cgvAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getCgvSchedule(title, date);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/megaBox/getTimetable.do", method = RequestMethod.GET)
    public Object megaBoxAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getMegaBoxSchedule(title, date);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/lotteCinema/getTimetable.do", method = RequestMethod.GET)
    public Object lotteCinemaAjax(@RequestParam String title, @RequestParam String date) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getLotteCinemaSchedule(title, date);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }

    // id값을 가지고 DB에서 좋아요리스트를 n,m,k 형식의 String 그대로 return함
    // 자바스크립트에서 let list = listStr.split(","); 으로 배열로 변환가능
    @ResponseBody
    @RequestMapping(value = "/getLikedList.do/{id}", method = RequestMethod.GET)
    public Object getLikedList(@PathVariable String id) throws Exception {
//        System.out.println(id+" 좋아요리스트 불러오기");
        String likedList = mds.getLikedList(id);
        return likedList;
    }

    // 전달된 id의 좋아요리스트 최신화
    @ResponseBody
    @RequestMapping(value = "/setLikedList.do", method = RequestMethod.GET)
    public int setLikedList(@RequestParam String id, @RequestParam String likedList,@RequestParam String pk, @RequestParam boolean type) throws Exception {
        int likeCnt = mds.setLikedList(id,likedList,pk,type);
        return likeCnt;
    }

    // 리뷰 리스트 보내주기
    @ResponseBody
    @RequestMapping(value = "/getReviewList.do", method = RequestMethod.GET)
    public List<ReviewDto> getReviewList(@RequestParam("moviePk") String moviePkStr,@RequestParam("page") String pageStr,@RequestParam("num") String numStr) throws Exception {
        int moviePk = Integer.parseInt(moviePkStr);
        int page = Integer.parseInt(pageStr);
        int num = Integer.parseInt(numStr);
        List<ReviewDto> likeCnt = mds.getReviewList(moviePk,page,num);
        return likeCnt;
    }
}
