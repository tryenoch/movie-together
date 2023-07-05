package com.bitc.full505_team2_project.controller;


import com.bitc.full505_team2_project.common.ScriptUtils;
import com.bitc.full505_team2_project.dto.MemberDto;
import com.bitc.full505_team2_project.service.Inject;
import com.bitc.full505_team2_project.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Inject

    // menu.do를 클릭하면 login으로 이동

    @Autowired
    private MemberService memberService;


    //회원가입 페이지 출력 요청
    @RequestMapping(value = {"/join"}, method = RequestMethod.GET)
    public String join() throws Exception {
        return "member/join";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String Login() throws Exception {
        return "member/login";
    }

    @RequestMapping(value = {"/mypage"},method = RequestMethod.GET)
    public String Mypage() throws Exception {
        return "member/mypage";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login_check(@ModelAttribute MemberDto dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int result = memberService.loginCheck(dto.getMemberId(), dto.getMemberPw());

        if (result == 1) { //로그인 성공 시
            // 사용자 아이디 패스워드 기반으로 사용자 정보를 가져오기
            MemberDto member = memberService.selectMember(dto.getMemberId(), dto.getMemberPw());
            // 세션 생성
            HttpSession session = request.getSession();
            session.setAttribute("userId", member.getMemberId());
            session.setAttribute("userName", member.getMemberName());
            session.setAttribute("userEmail", member.getMemberEmail());
            session.setAttribute("userGradle", member.getMemberGrade());
            session.setMaxInactiveInterval(1800);
            // 세션에 가져온 사용자 정보 등록하기


//            mav.setViewName("/member/loginok"); //뷰의 이름
            // return "redirect:/member/loginok";
            return "redirect:/MovieTogetherMain"; // 로그인 성공시 이동
        } else { //로그인 실패 시
//            mav.setViewName("member/Login");
//            mav.addObject("message","error");
            ScriptUtils.alertAndMovePage(response,"회원 정보가 일치하지 않습니다.", "/member/login");
            return "redirect:/member/login";
        }
    }



    @RequestMapping("logout")
    public String logout(HttpSession session) {
        memberService.logout(session);
        /*mav.addObject("message", "logout");
        return mav;*/
        return "redirect:/MovieTogetherMain"; // 로그아웃 시 메인으로 이동
    }


//    RequestParam 이라는걸 사용해서 네임 값을 여기다 적어준다 담겨온 값을 오른쪽에 집어 넣는다
//    public String join(@RequestParam("joinId")String joinId,
//                       @RequestParam("joinPw")String joinPw,
//                       @RequestParam("joinPwd")String joinPwd,
//                       @RequestParam("joinName")String joinName,
//                       @RequestParam("joinDay")String joinDay,
//                       @RequestParam("joinEmail")String joinEmail) {


    //회원가입 가입 완료시 로그인할 화면 출력
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String join(MemberDto member) {
        System.out.println("MemberController.join");
//        System.out.println("joinId = " + joinId + ", joinPw = " + joinPw + ", joinPwd = " + joinPwd + ", joinName = " + joinName + ", joinDay = " + joinDay + ", joinEmail = " + joinEmail);
        memberService.memberJoin(member);

        return "/member/login";
    }

    @ResponseBody
    @RequestMapping(value = "/IdCheck",method = RequestMethod.POST)
    public String IdCheck(@RequestParam("userId") String userId){
        int Check = memberService.IdCheck(userId);

        if (Check == 0) {
            return "true";
        }
        else {
            return "false";
        }
    }

    // 로그인시 임시로 만든 loginok 화면 출력
    @RequestMapping(value = "/loginok", method = RequestMethod.GET)
    public String ok(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String A = (String) session.getAttribute("uId");
        System.out.println("\n-------------------\n");
        System.out.println(A);

        return "member/loginok";
    }

}
