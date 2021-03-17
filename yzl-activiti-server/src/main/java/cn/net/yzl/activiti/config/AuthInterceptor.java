//package cn.net.yzl.activiti.config;
//
//import cn.net.yzl.common.entity.ComResponse;
//import cn.net.yzl.logger.json.JacksonUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Slf4j
//@Component
//public class AuthInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//
//    private void returnJson(HttpServletResponse response, ComResponse<Object> result) {
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json;charset=UTF-8");
//        try (PrintWriter writer = response.getWriter()) {
//            writer.print(JacksonUtil.toJsonString(result));
//        } catch (IOException e) {
//            log.error("response error", e);
//        }
//    }
//}