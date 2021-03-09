package cn.net.yzl.activiti.interceptor;

import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.activiti.properties.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Autowired
    private PropertiesUtils propertiesUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        log.info("----------c映射----------" + (handler instanceof HandlerMethod) + "----------------------");
        String appid = request.getHeader("appid");

        log.info(handler.toString());


        // gateway 已经验证过
        if(propertiesUtils.getRwAppidList().contains(appid)){
            return true;
        }else if(propertiesUtils.rAppidList.contains(appid)){
            String pathInfo = request.getPathInfo();

            int index = pathInfo.lastIndexOf("/");

            String substring = pathInfo.substring(index+1);

            if(substring.startsWith("get")){
                return true;
            }else{
                response.setStatus(200);
                ComResponse<String> rep = ComResponse.fail(ResponseCodeEnums.APPID_NO_WRITE_ERROR_CODE.getCode(), ResponseCodeEnums.APPID_NO_WRITE_ERROR_CODE.getMessage());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().append(JSONUtil.toJsonStr(rep));
                return false;
            }

        }else if(!propertiesUtils.equals(appid)){

            response.setStatus(200);
            ComResponse<String> rep = ComResponse.fail(ResponseCodeEnums.APPID_CHECK_ERROR_CODE.getCode(), ResponseCodeEnums.APPID_CHECK_ERROR_CODE.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().append(JSONUtil.toJsonStr(rep));
            return false;
        }
        return true;
    }



}
