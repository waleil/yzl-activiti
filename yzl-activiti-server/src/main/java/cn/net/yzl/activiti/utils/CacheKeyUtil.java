package cn.net.yzl.activiti.utils;

/**
 * @version 1.0
 * @date 2021/1/20 14:19
 */
public class CacheKeyUtil {
    final static String serverName ="asr";

    /**
     * 生成库存自增主键
     * @return
     */
    public static String maxStoreProductCacheKey(){
        return new StringBuilder(serverName).append("-").append("maxStockCode").toString();
    }

    /**
     * @Author: wangshuaidong
     * @Description: ASR_token
     * @Date: 2021/2/5 12:20 下午
     * @Return: java.lang.String
     */
    public static String generateAsrTokenKey(){
        return serverName + "-EHR_ASR_TOKEN";
    }

}
