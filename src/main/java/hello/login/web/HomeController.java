package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required=false) Long memberId, Model model)  {
        if(memberId == null) { // 로그인 쿠키가 없는 사용자는 기존 home으로 보낸다.
            return "home";
        }

        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) { // 로그인 쿠키가 있어도 회원이 없으면 home으로 보낸다.
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 쿠키가 있는 사용자는 로그인 사용자 전용 홈 화면으로 보낸다.
    }

   // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model)  {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }

        // 로그인
        model.addAttribute("member", member);
        return "loginHome"; // 로그인 쿠키가 있는 사용자는 로그인 사용자 전용 홈 화면으로 보낸다.
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model)  {

        // 세션이 없으면 home
        // 세션을 찾아서 사용하는 시점에는 false 처리해야한다.
        // 로그인을 하지 않은 사용자도 의미없는 세션이 생성되기 때문이다.
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "home";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 쿠키가 있는 사용자는 로그인 사용자 전용 홈 화면으로 보낸다.
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model)  {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 쿠키가 있는 사용자는 로그인 사용자 전용 홈 화면으로 보낸다.
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model)  {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 쿠키가 있는 사용자는 로그인 사용자 전용 홈 화면으로 보낸다.
    }
}