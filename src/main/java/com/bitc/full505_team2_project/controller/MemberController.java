package com.bitc.full505_team2_project.controller;


import com.bitc.full505_team2_project.dto.MemberDto;
import com.bitc.full505_team2_project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;


    //회원가입 페이지 출력 요청
    @RequestMapping(value = {"/join"}, method = RequestMethod.GET)
    public String login() throws Exception {
        return "member/join";
    }

    @PostMapping("/join")
//    RequestParam 이라는걸 사용해서 네임 값을 여기다 적어준다 담겨온 값을 오른쪽에 집어 넣는다
//    public String join(@RequestParam("joinId")String joinId,
//                       @RequestParam("joinPw")String joinPw,
//                       @RequestParam("joinPwd")String joinPwd,
//                       @RequestParam("joinName")String joinName,
//                       @RequestParam("joinDay")String joinDay,
//                       @RequestParam("joinEmail")String joinEmail) {


    public String join(MemberDto member) {
        System.out.println("MemberController.join");
//        System.out.println("joinId = " + joinId + ", joinPw = " + joinPw + ", joinPwd = " + joinPwd + ", joinName = " + joinName + ", joinDay = " + joinDay + ", joinEmail = " + joinEmail);
        memberService.memberJoin(member);

        return "index";
    }
}
