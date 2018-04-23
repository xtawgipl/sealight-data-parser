package com.sealight.app.util;

import com.sealight.app.ParamFactory;
import com.sealight.app.bean.Constants;
import com.sealight.app.bean.LightTypeListData;
import com.sealight.app.bean.ParamBean;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 页面解析器
 *
 * @author zhangjj
 * @create 2017-12-10 13:02
 **/
public class DataParser {

    private final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * 解析html select 标签
     * @param selectElm
     * @return
     */
    public static Map<Integer, String> optionParser(Element selectElm){
        Map<Integer, String> optionMap = new HashMap<>();
        Elements options = selectElm.getElementsByTag("option");
        int size = options.size();
        for(int i = 0; i < size; ++i){
            String value = options.get(i).attr("value");
            if(value != null && !value.trim().equals("") && !value.trim().equals("-")){
//                System.out.println(value + "  -- >  " + options.get(i).text());
                optionMap.put(Integer.valueOf(value), options.get(i).text());
            }
        }
        return optionMap;
    }


    /**
     *  解析年份
     * @param
     * @author zhangjj
     * @Date 2017/12/10 13:37
     * @return
     * @exception
     */
    public static Map<Integer, String> yearParser(String yearHtml){
        Document document = Jsoup.parse(yearHtml);
        Element yearSelectElm = document.getElementById("selectYear");
        return optionParser(yearSelectElm);
    }

    /**  解析制造商
     * @param
     * @author zhangjj
     * @Date 2017/12/10 13:37
     * @return
     * @exception
     */
    public static Map<Integer, String> makeParser(String makeHtml){
        Element makeEle = Jsoup.parse(makeHtml).body();
        return optionParser(makeEle);
    }

    /**   解析汽车型号
     * @param
     * @author zhangjj
     * @Date 2017/12/10 14:00
     * @return
     * @exception
     */
    public static Map<Integer, String> typeParser(String typeHtml){
        Element typeEle = Jsoup.parse(typeHtml).body();
        return optionParser(typeEle);
    }

    /**   获取灯类型
     * @param
     * @author zhangjj
     * @Date 2017/12/10 16:00
     * @return
     * @exception
     */
    public static Map<String, Map<String, String>> lightTypeParser(String lightTypeHtml){
        Map<String, Map<String, String>> lightTypeMap = new HashMap<>();
        Element lightTypeEle = Jsoup.parse(lightTypeHtml).body();
        Elements forwardElements = lightTypeEle.getElementsByClass("pick-list pl-forward");//前灯
        if(forwardElements != null && forwardElements.size() != 0){
            Element forwordLightEle = forwardElements.get(0);
            Elements liEmements = forwordLightEle.getElementsByTag("li");
            int size = liEmements.size();
            Map<String, String> forwardLightMap = new HashMap<>();
            for(int i = 0; i < size; ++i){
                Element element = liEmements.get(i);
                Elements aEle = element.getElementsByTag("a");
                String dataAction = aEle.attr("data-action");
                forwardLightMap.put(element.text(), dataAction);
            }
            lightTypeMap.put("forward", forwardLightMap);
        }

        Elements exteriorElements = lightTypeEle.getElementsByClass("pick-list pl-exterior");//车外灯
        if(exteriorElements != null && exteriorElements.size() != 0){
            Element exteriorLightEle = exteriorElements.get(0);
            Elements liEmements = exteriorLightEle.getElementsByTag("li");
            int size = liEmements.size();
            Map<String, String> exteriorLightMap = new HashMap<>();
            for(int i = 0; i < size; ++i){
                Element element = liEmements.get(i);
                Elements aEle = element.getElementsByTag("a");
                String dataAction = aEle.attr("data-action");
                exteriorLightMap.put(element.text(), dataAction);
            }
            lightTypeMap.put("exterior", exteriorLightMap);
        }

        Elements interiorElements = lightTypeEle.getElementsByClass("pick-list pl-interior");//车内灯
        if(interiorElements != null && interiorElements.size() != 0){
            Element interiorLightEle = interiorElements.get(0);
            Elements liEmements = interiorLightEle.getElementsByTag("li");
            int size = liEmements.size();
            Map<String, String> interiorLightMap = new HashMap<>();
            for(int i = 0; i < size; ++i){
                Element element = liEmements.get(i);
                Elements aEle = element.getElementsByTag("a");
                String dataAction = aEle.attr("data-action");
                interiorLightMap.put(element.text(), dataAction);
            }
            lightTypeMap.put("interior", interiorLightMap);
        }

        return lightTypeMap;
    }

    public static List<String> getDetailUrlList(String detailHtml){
        List<String> urlList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(data-action=)(.*?)(\">)");
        Matcher matcher = pattern.matcher(detailHtml);
        while(matcher.find()){
            String detailUrl = matcher.group().replaceAll(Pattern.quote("\\"), "").substring(13);
            detailUrl = detailUrl.substring(0, detailUrl.length() - 3);
//            System.out.println("detailUrl = " + detailUrl);
            if(detailUrl != null && detailUrl.contains("getProductDetails")){
                urlList.add(detailUrl);
            }
        }
        return urlList;
    }


    public static String lightDetailHtml(String detailHtml){
        Element lightDetailEle = Jsoup.parse(detailHtml).body();
        Elements forwardElements = lightDetailEle.getElementsByTag("h1");//灯类型 详情 名称
        if(forwardElements != null && forwardElements.size() != 0){
            Element element = forwardElements.get(0);
            String lightTypeDetailName = element.text();
//            System.out.println("lightTypeDetailName = " + lightTypeDetailName);
            return lightTypeDetailName;
        }
        System.err.println("无灯类型详情!");
        return null;
    }

    /**
     * 编号
     * 提取最后一个数字
     * @param
     * @author zhangjj
     * @Date 2018/4/16 14:28
     * @return
     * @exception
     *
     */
    public static String getLightOrder(String url){
        //https://www.sylvania-automotive.com/apps/vlrg-us/Vlrg/getVehicleNotes/1/608/
        if(StringUtil.isBlank(url)){
            System.err.println("error url : " + url);
            return null;
        }
        url = url.substring(0, url.length() - 1);
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * 解析灯的table表格，获取 key,value(key = 灯类型,value=灯型号)
     * @param
     * @author zhangjj
     * @Date 2018/4/16 15:16
     * @return
     * @exception
     *
     */
    public static Map<String, String> lightTableParser(String tableHtml){
        Map<String, String> map = new HashMap<>();
        Element lightDetailEle = Jsoup.parse(tableHtml).body();
        Element tableBodyEle = lightDetailEle.getElementsByTag("tbody").get(0);
        Elements trs = tableBodyEle.getElementsByTag("tr");
        if(trs != null && trs.size() != 0){
            Iterator<Element> iterator = trs.iterator();
            while (iterator.hasNext()){
                Element tr = iterator.next();
                String key = tr.getElementsByTag("th").get(0).text();
                String value = tr.getElementsByTag("td").get(0).text();
                map.put(key, value);
            }

        }
        return map;
    }
}
