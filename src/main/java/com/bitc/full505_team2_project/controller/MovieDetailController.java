package com.bitc.full505_team2_project.controller;

import com.bitc.full505_team2_project.dto.MovieDTO;
import com.bitc.full505_team2_project.dto.MovieTimeTableDto;
import com.bitc.full505_team2_project.dto.ReviewDto;
import com.bitc.full505_team2_project.dto.TheaterDto;
import com.bitc.full505_team2_project.service.MovieDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


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

    // db에 저장된 영화정보를 가져옴
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

    // 아래는 상영시간표 가져오는 메소드
    @ResponseBody
    @RequestMapping(value = "/cgv/getTimetable.do", method = RequestMethod.GET)
    public Object cgvAjax(@RequestParam String title, @RequestParam String date, @RequestParam String code) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getCgvSchedule(title, date, code);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/megaBox/getTimetable.do", method = RequestMethod.GET)
    public Object megaBoxAjax(@RequestParam String title, @RequestParam String date, @RequestParam String code) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getMegaBoxSchedule(title, date, code);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }
    @ResponseBody
    @RequestMapping(value = "/lotteCinema/getTimetable.do", method = RequestMethod.GET)
    public Object lotteCinemaAjax(@RequestParam String title, @RequestParam String date, @RequestParam String code) throws Exception {

        List<MovieTimeTableDto> timeTableMB = mds.getLotteCinemaSchedule(title, date, code);
//        System.out.println(timeTableMB);
        return timeTableMB;
    }

    // 영화관 타입(cgv 등)을 선택하면 해당영화관회사에서 사용하는 지역명 리스트를 반환해줌
    @ResponseBody
    @RequestMapping(value = "/getAreas.do", method = RequestMethod.GET)
    public Object getAreasAjax(@RequestParam String type) throws Exception {

        List<String> areas = mds.getAreas(type);

        return areas;
    }

    // 영화관 타입(cgv 등)과 지역을 선택하면 해당영화관회사의 영화관이름 (CGV서면 등)을 반환
    @ResponseBody
    @RequestMapping(value = "/getTheaters.do", method = RequestMethod.GET)
    public Object getTheatersAjax(@RequestParam String type, @RequestParam String area) throws Exception {

        List<TheaterDto> theaters = mds.getTheaters(type, area);
        return theaters;
    }

    // 좋아요리스트 가져오기: id값을 가지고 DB에서 좋아요리스트를 n,m,k 형식의 String 그대로 return함
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

    // 리뷰 좋아요를 눌렀을 때
    @ResponseBody
    @RequestMapping(value = "/updateReviewLike.do", method = RequestMethod.GET)
    public int setReviewLikedList(@RequestParam String id, @RequestParam String likedList,@RequestParam String reviewPk, @RequestParam boolean type) throws Exception {
        int likeCnt = mds.setReviewLikedList(id,likedList,reviewPk,type);
        return likeCnt;
    }


    // 리뷰 리스트 보내주기
    @ResponseBody
    @RequestMapping(value = "/getReviewList.do", method = RequestMethod.GET)
    public List<ReviewDto> getReviewList(@RequestParam("moviePk") int moviePk,@RequestParam("page") int page,@RequestParam("num") int num,@RequestParam("id") String id, @RequestParam("all") String all) throws Exception {

        List<ReviewDto> list = mds.getReviewList(moviePk,page,num, id, all);
        return list;
    }

    // 리뷰 쓰기
    @ResponseBody
    @RequestMapping(value = "/writeReview.do", method = RequestMethod.POST)
    public void writeReview(ReviewDto dto) throws Exception {
        mds.writeReview(dto);
    }

    // 리뷰 수정
    @ResponseBody
    @RequestMapping(value = "/editReview.do", method = RequestMethod.PUT)
    public void editReview(ReviewDto dto) throws Exception {
        mds.editReview(dto);
    }

    // 리뷰 삭제
    @ResponseBody
    @RequestMapping(value = "/delReview.do", method = RequestMethod.DELETE)
    public void delReview(ReviewDto dto) throws Exception {

        mds.delReview(dto);
    }

    // 영화관 정보 업데이트
    @ResponseBody
    @RequestMapping(value = "/updateTheater.do", method = RequestMethod.GET)
    public void updateCgvTheater(@RequestParam String type) throws Exception {
        switch(type) {
            case "cgv":
                mds.updateCgvTheater();
                break;
            case "megaBox":
                mds.updateMegaBoxTheater();
                break;
            case "lotteCinema":
                mds.updateLotteCinemaTheater();
                break;
        }
    }

    // 영화관 정보 업데이트
    @ResponseBody
    @RequestMapping(value = "/getReviewLike.do", method = RequestMethod.GET)
    public String getReviewLike(@RequestParam String id) throws Exception {
        return mds.getReviewLike(id);
    }
}
