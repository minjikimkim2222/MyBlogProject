package org.myblog.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.myblog.web.login.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        // 세션 자체가 없어도, 세션은 있는데 해당 세션ID가 없어도..
        if (session == null || session.getAttribute(SessionConst.User_Login_Form) == null) {
            // 로그인 안한 사용자임

            // 로그인으로 redirect
            response.sendRedirect("/myblog/loginform");

            return false; // 그다음 단계 못감!! (redirect만 실행될뿐..)
        }

        return true; // 로그인 인증 성공!! -- 그다음단계 (컨트롤러 호출 - 뷰 리턴 - postHandle - 뷰 렌더링 - afterCompletion)
    }
}
