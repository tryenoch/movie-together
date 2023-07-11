package com.bitc.full505_team2_project.controller;


import com.bitc.full505_team2_project.common.ScriptUtils;
import com.bitc.full505_team2_project.dto.MemberDto;
import com.bitc.full505_team2_project.service.Inject;
import com.bitc.full505_team2_project.service.MemberService;
import com.bitc.full505_team2_project.service.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//View를 반환하기 위해 사용
@Controller
@RequestMapping("/member")
public class MemberController {

    @Inject

    // menu.do를 클릭하면 login으로 이동

    @Autowired
    private MemberService memberService;


    //회원가입 페이지 출력
    @RequestMapping(value = {"/join"}, method = RequestMethod.GET)
    public String join() throws Exception {
        return "member/join";
    }
    
    
    //로그인 페이지 출력
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() throws Exception {
        return "member/login";
    }

//  마이페이지 출력
    @RequestMapping(value = {"/mypage"},method = RequestMethod.GET)
    public String myPage(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String likeList;
        if(session.getAttribute("userId") != null){
            String userId = session.getAttribute("userId").toString();
            likeList = memberService.selectLikeList(userId);
            session.setAttribute("userLikeList", likeList);
        }

        return "member/mypage";
    }

//  수정페이지 출력
    @RequestMapping(value = {"/correction"},method = RequestMethod.GET)
    public String correction() throws Exception {
        return "member/correction";
    }
    
    
//// 주석 처리 해야될수도 있는것
//    @PostMapping("/login")
//    public String loginForm(@ModelAttribute MemberDto memberDto,
//                            HttpSession session) {
//        boolean loginResult = memberService.loginCheck(memberDto);
//        if (loginResult) {
//            session.setAttribute("loginEmail",memberDto.getMemberEmail());
//            return "main";
//        } else{
//            return "login";
//        }
//    }

//    //수정 화면 요청
//    @GetMapping("/correction")
//    public String Update(HttpSession session, Model model) {
//            // 세션에 저장된 나의 이메일 가져오기
//        String loginEmail = (String) session.getAttribute("loginEmail");
//        MemberDto memberDto = memberService.findByMemberEmail(loginEmail);
//        model.addAttribute("member",memberDto);
//        return "correction";
//    }

    
    //수정 페이지 확인
    @RequestMapping(value = "/correction",method = RequestMethod.POST)
    public String correction(MemberDto memberDto) {
        memberService.Correction(memberDto);
        return "redirect:/member/mypage";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void loginCheck(@ModelAttribute MemberDto dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int result = memberService.loginCheck(dto.getMemberId(), dto.getMemberPw());

        if (result == 1) { //로그인 성공 시
            // 사용자 아이디 패스워드 기반으로 사용자 정보를 가져오기
            MemberDto member = memberService.selectMember(dto.getMemberId(), dto.getMemberPw());
            // 세션 생성
            HttpSession session = request.getSession();
            session.setAttribute("userId", member.getMemberId());
            session.setAttribute("userName", member.getMemberName());
            session.setAttribute("userEmail", member.getMemberEmail());
            session.setAttribute("userGrade", member.getMemberGrade());
            session.setAttribute("userLikeList",member.getMemberLikeList()); // likeList가 필요해서 LikeList도 들고왔어요!
            session.setAttribute("userBirth",member.getMemberBirth()); // 생일도 필요하더라구요
            System.out.println(member.getMemberLikeList());
            session.setAttribute("userGrade", member.getMemberGrade());
            session.setMaxInactiveInterval(1800);

            // 세션에 가져온 사용자 정보 등록하기


//            mav.setViewName("/member/loginok"); //뷰의 이름
            // return "redirect:/member/loginok";
            ScriptUtils.alertAndMovePage(response,"로그인 되었습니다.", "/MovieTogetherMain");
            /*return "redirect:/MovieTogetherMain"; // 로그인 성공시 이동*/
        } else { //로그인 실패 시
//            mav.setViewName("member/Login");
//            mav.addObject("message","error");
            ScriptUtils.alertAndMovePage(response,"회원 정보가 일치하지 않습니다.", "/member/login");
            /*return "redirect:/member/login";*/
        }
    }



    //로그아웃 완료
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws Exception {
        ScriptUtils.alertAndMovePage(response,"로그아웃 되었습니다.", "/MovieTogetherMain");
        memberService.logout(session);
        // return "redirect:/MovieTogetherMain"; // 로그아웃 시 메인으로 이동
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
    public void join(MemberDto member,HttpServletResponse response) throws Exception {
        System.out.println("MemberController.join");
        memberService.memberJoin(member);
        ScriptUtils.alertAndMovePage(response,"회원가입 완료 되었습니다.", "/MovieTogetherMain");
        // return "/member/login";
    }


    //기존계정 새로운 계정 존재 여부 확인
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

}

