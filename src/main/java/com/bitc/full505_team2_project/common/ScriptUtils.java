package com.bitc.full505_team2_project.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ScriptUtils {

    /* 사용법 (작성자 chanmi, 문제 있으면 얘기해주세요)
    * 해당 Controller 메소드 매개변수로 HttpServletResponse response 추가
    * 알림창만 띄울 때 예시 : ScriptUtils.alert(response, "회원만 이용 가능 합니다.");
    * 알림창 띄운 후 특정 주소로 이동 시킬 때 : ScriptUtils.alertAndMovePage(response, "회원만 이용 가능 합니다.", "/member/login");
    * 알림창 뛰운 후 이전 페이지로 이동 시킬 떄 : ScriptUtils.alertAndBackPage(response, "잘못된 접근입니다.")
    * */

    public static void init(HttpServletResponse response){
        response.setContentType("text/html; charset=euc-kr");
        response.setCharacterEncoding("euc-kr");
    }

    public static void alert(HttpServletResponse response, String alertText) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "');</script>");
        out.flush();
    }

    public static void alertAndMovePage(HttpServletResponse response, String alertText, String nextPage) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "');");
        out.println("location.href='" + nextPage + "';</script>");
        out.flush();
    }

    public static void alertAndBackPage(HttpServletResponse response, String alertText) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + alertText + "');");
        out.println("history.back(); + </script>");
        out.flush();
    }

}
