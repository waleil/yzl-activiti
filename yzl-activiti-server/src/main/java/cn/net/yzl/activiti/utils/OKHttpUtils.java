package cn.net.yzl.activiti.utils;

import cn.net.yzl.logger.annotate.SysAccessLog;
import cn.net.yzl.logger.enums.DefaultDataEnums;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @title: HTTPClient
 * @Author lichanghong
 * @Date: 2020/12/3 11:41 上午
 * @Version 1.0
 */
@Component
@Slf4j
public class OKHttpUtils {

//    private static final String Authorization = "bearer e45f6d0f-a863-425a-8d80-bd8821771046";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OKHttpUtils instance;
    private OkHttpClient okHttpClient;

    /**
     * 客户端
     */
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();


    public static OKHttpUtils getInstance() {
        if (instance == null) {
            synchronized (LOCKER) {
                if (instance == null) {
                    instance = new OKHttpUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 同步调用
     *
     * @param url     请求地址
     * @param headers header
     * @return
     */
    @SysAccessLog(logKeyParamName = {"url"},
            source = DefaultDataEnums.Source.THIRD_API,
            action = DefaultDataEnums.Action.QUERY,
            requestMetod = "GET")
    public String synchronizedGetRequests(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                builder.addHeader(key, value);
            });
        }
        final Request request = builder.get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String res = response.body().string();
                return res;
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("url:{},code:{},调用失败", ex);
            return null;
        }
    }



    /**
     * 设置content-type
     * @param type
     * @return
     */
    public MediaType getMediaType(String type){
        if(!type.equals("")){
            return MediaType.parse(type);//"application/x-www-form-urlencoded; charset=utf-8"
        }
        return MediaType.parse("application/json;charset=utf-8");
    }
    /**
     * delete请求
     * @param url
     * @return
     */
    @SysAccessLog(logKeyParamName = {"url"},
            source = DefaultDataEnums.Source.THIRD_API,
            action = DefaultDataEnums.Action.DEL,
            requestMetod = "DELETE")
    public String doDelete(String url){
        if (isBlankUrl(url)){
            return null;
        }
        Request request = getRequestForDelete(url);
        return commonRequest(request);
    }
    private Request getRequestForDelete(String url) {
        Request request = new Request.Builder()
                .url(url)
                .delete(null)
                .build();
        return request;
    }

    /**
     * get请求 不带参数
     * @param url
     * @return
     */
    public String doGet(String url){
        if (isBlankUrl(url)){
            return null;
        }
        Request request = getRequestForGet(url);
        return commonRequest(request);
    }
    /**
     * get请求 实现
     * @param url
     * @return
     */
    private Request getRequestForGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }



    /**
     *  get请求 带Map参数
     * @param url
     * @param params
     * @return
     */
    public String doGet(String url, Map<String, String> params){
        System.out.println("参数^-^："+params.toString());
        if (isBlankUrl(url)){
            return null;
        }
        Request request = getRequestForGet(url, params);
        return commonRequest(request);
    }
    /**
     * get请求 实现
     * @param url
     * @param params
     * @return
     */
    private Request getRequestForGet(String url, Map<String, String> params) {
        Request request = new Request.Builder()
                .url(getUrlStringForGet(url, params))
                .build();
        return request;
    }


    /**
     * post请求  参数为jsonStr
     * @param url
     * @param json
     * @param type
     * @return
     */
    public String doPostJson(String url, String json,String type){
        if (isBlankUrl(url)){
            return null;
        }
        Request request = getRequestForPostJson(url, json,type);
        return commonRequest(request);
    }


    /**
     * put请求  参数为jsonStr
     * @param url
     * @param json
     * @param type
     * @return
     */
    public String doPutJson(String url, String json,String type){
        if (isBlankUrl(url)){
            return null;
        }
        Request request = getRequestForPutJson(url, json,type);
        return commonRequest(request);
    }

    /**
     *  实现post请求
     * @param url
     * @param json
     * @param type
     * @return
     */
    private Request getRequestForPostJson(String url, String json,String type) {
        RequestBody body = RequestBody.create(getMediaType(type), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }
    /**
     *  实现Put请求
     * @param url
     * @param json
     * @param type
     * @return
     */
    private Request getRequestForPutJson(String url, String json,String type) {
        RequestBody body = RequestBody.create(getMediaType(type), json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        return request;
    }
    /**
     * 新post请求  参数为map
     * @param url
     * @param params
     * @return
     */
    public String newDoPostForm(String url, Map<String, String> params,String type){
        System.out.println("参数^-^："+params.toString());
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForPostJson(url,  JSONObject.toJSONString(params),type);
        return commonRequest(request);
    }
    public String newsDoPostForm(String url, Map<String, Object> params,String type){
        if(params!=null){
            System.out.println("参数^-^："+params.toString());
        }

        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForPostJson(url,  JSONObject.toJSONString(params),type);
        return commonRequest(request);
    }

    /**
     * post请求  参数为map
     * @param url
     * @param params
     * @return
     */
    public String doPostForm(String url, Map<String, String> params){
        if (isBlankUrl(url)) {
            return null;
        }
        Request request = getRequestForPostForm(url, params);
        return commonRequest(request);
    }
    /**
     * 实现post请求
     * @param url
     * @param params
     * @return
     */
    private Request getRequestForPostForm(String url, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }
    private Boolean isBlankUrl(String url){
        if (StringUtils.isBlank(url)){
            System.out.println("url是null");
            return true;
        }else{
            return false;
        }
    }

    /**
     * 请求解析
     * @param request
     * @return
     */
    private String commonRequest(Request request){
        String re = "";
        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()){
                re = response.body().string();
                log.info("请求^-^："+request.url().toString()+" \r\n响应^-^："+re);
            }else {
                log.error("请求失败 url:"+request.url().toString()+"\r\nmessage:"+response.message());
            }
        }catch (Exception e){
            log.error("请求异常"+e);
        }
        return re;
    }



    /**
     * get请求参数封装
     * @param url
     * @param params
     * @return
     */
    private String getUrlStringForGet(String url, Map<String, String> params) {
        int num = 0;
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);
        urlBuilder.append("?");
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    if(num == 0){
                        urlBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        num = 1;
                    }else{
                        urlBuilder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    }
                } catch (Exception e) {
                    urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            num = 0;
        }
        return urlBuilder.toString();
    }

}
