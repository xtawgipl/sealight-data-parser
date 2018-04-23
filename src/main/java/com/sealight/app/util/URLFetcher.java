package com.sealight.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 刚刚抓取
 *
 * @author zhangjj
 * @create 2017-12-10 13:03
 **/
public class URLFetcher {

    /** 默认重试次数 */
    public static final Integer TRY_NUM = 180;

    /** 所有年份 URL*/
    public static final String SYLVANIA_YEAR = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/#/";


    /** 根据年份获取所有制造商 URL*/
    public static final String SYLVANIA_MAKE = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getMake/%s/";

    /** 根据年份及制造商获取汽车类型 URL 第1个占位符为制造商id ，第二个占位符为年份id*/
    public static final String SYLVANIA_TYPE = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getModel/%s/%s/";

    /** 根据年份、制造商、类型 获取汽车灯类型 URL  第1个占位符为年份 ，第二个占位符为制造商，第三个点位符为汽车类型*/
    public static final String SYLVANIA_LIGHT_TYPE = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getPositions//%s/%s/%s/";


    /** 获取前灯列表, 参数:编号*/
    public static final String SYLVANIA_FORWARD_LIST = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getBulbTable/Forward/%s/";

    /** 获取外灯列表, 参数:编号*/
    public static final String SYLVANIA_EXTERIOR_LIST = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getBulbTable/Exterior/%s/";

    /** 获取内灯列表, 参数:编号*/
    public static final String SYLVANIA_INTERIOR_LIST = "https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getBulbTable/Interior/%s/";



    public static String pickData(String url) {

        return pickData(url, TRY_NUM);
    }
    /**
     * 爬取网页信息
     */
    public static String pickData(String url, Integer tryNum) {
        if(url == null){
            System.err.println("url 是空的!");
            return null;
        }
        System.out.println(Thread.currentThread().getName() + " (" + (TRY_NUM - tryNum + 1) + ") : " + url);
        url = url.replaceAll(" ", "%20");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000).setConnectionRequestTimeout(20000)
                .setSocketTimeout(20000).build();
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            if(tryNum-- > 0){
                try {
                    Thread.sleep(1500L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return pickData(url, tryNum);
            }else{
                System.err.println("errorUrl : "  + " (" + (TRY_NUM - tryNum + 1) + ") : " + url);
                e.printStackTrace();
            }
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String html = URLFetcher.pickData(URLFetcher.SYLVANIA_YEAR, TRY_NUM);
        System.out.println(html);

        /*String test = "<div class=\\\"product-banner\\\" data-action=\\\"https:\\/\\/www.sylvania-automotive.com\\/apps\\/vlrg-us\\/Vlrg\\/getProductDetails\\/26549\\/326\\/4\\/\\\">\\n                    <a href=\\\"javascript:void(0);\\\">\\n                        <img src=\\\"https:\\/\\/media.osram.info\\/im\\/img\\/osram-dam-2481584\\\">\\n                    <\\/a>\\n                <\\/div>\\n                            <div class=\\\"product-banner\\\" data-action=\\\"https:\\/\\/www.sylvania-automotive.com\\/apps\\/vlrg-us\\/Vlrg\\/getProductDetails\\/26549\\/332\\/4\\/\\\">\\n                    <a href=\\\"javascript:void(0);\\\">\\n                        <img src=\\\"https:\\/\\/media.osram.info\\/im\\/img\\/osram-dam-2481581\\\">\\n                    <\\/a>\\n                <\\/div>\\n                            <div class=\\\"product-banner\\\" data-action=\\\"https:\\/\\/www.sylvania-automotive.com\\/apps\\/vlrg-us\\/Vlrg\\/getProductDetails\\/26549\\/333\\/4\\/\\\">\\n                    <a href=\\\"javascript:void(0);\\\">\\n                        <img src=\\\"https:\\/\\/media.osram.info\\/im\\/img\\/osram-dam-2481578\\\">\\n                    <\\/a>\\n                <\\/div>\\n                            <div class=\\\"product-banner\\\" data-action=\\\"https:\\/\\/www.sylvania-automotive.com\\/apps\\/vlrg-us\\/Vlrg\\/getProductDetails\\/26549\\/327\\/4\\/\\\">\\n                    <a href=\\\"javascript:void(0);\\\">\\n                        <img src=\\\"https:\\/\\/media.osram.info\\/im\\/img\\/osram-dam-2481577\\\">\\n                    <\\/a>\\n                <\\/div>\\n                    <\\/div>\\n    <\\/div>\\n<\\/div>";
        Pattern pattern = Pattern.compile("(data-action=)(.*?)(\">)");
        Matcher matcher = pattern.matcher(test);
        while(matcher.find()){
            String group = matcher.group();
            System.out.println(group.replaceAll(Pattern.quote("\\"), "").substring(13));

        }*/

    }
}
