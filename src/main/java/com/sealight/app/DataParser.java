package com.sealight.app;

import com.sealight.app.bean.Constants;
import com.sealight.app.bean.DataBean;
import com.sealight.app.bean.ParamBean;
import com.sealight.app.util.ExcelUtil;
import com.sealight.app.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final static ExecutorService executorService = Executors.newFixedThreadPool(50);

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
            Element tr = trs.get(0);
            String key = tr.getElementsByTag("th").get(0).text();
            String value = tr.getElementsByTag("td").get(0).text();
            map.put(key, value);
        }
        return map;
    }


    /*public static void main(String[] args) {




        String yearHtml = URLFetcher.pickData(URLFetcher.SYLVANIA_YEAR);
        Map<Integer, String> yearMap = DataParser.yearParser(yearHtml);
        for(Map.Entry<Integer, String> entry : yearMap.entrySet()){
            String makeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_MAKE, entry.getKey()));
//            System.out.println("makeHtml --> " + makeHtml);
            Map<Integer, String> makeMap = makeParser(makeHtml);
            System.out.println(entry.getValue() + " 年的制造商：");
            for(Map.Entry<Integer, String> makeEntry : makeMap.entrySet()){
                System.out.println("             " + makeEntry.getKey() + " -- > " + makeEntry.getValue());
                String typeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_TYPE, makeEntry.getKey(), entry.getKey()));
//                System.out.println(typeHtml);
                Map<Integer, String> typeMap = typeParser(typeHtml);
                for(Map.Entry<Integer, String> typeEntry : typeMap.entrySet()){
                    System.out.println("                                       " + typeEntry.getKey() + " -- > " + typeEntry.getValue());
                    String lightTypeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_LIGHT_TYPE, entry.getValue(), makeEntry.getValue(), typeEntry.getValue()));
//                    System.out.println(lightTypeHtml);
                    Map<String, Map<String, String>> lightTypeMap = lightTypeParser(lightTypeHtml);
                    Map<String, String> forwardMap = lightTypeMap.get("forward");
                    if(forwardMap != null){
                        for(Map.Entry<String, String> forwardLightEntry : forwardMap.entrySet()){
                            String lightDetailHtml = URLFetcher.pickData(forwardLightEntry.getKey());
//                            System.out.println(lightDetailHtml);
                            List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                            for(String detailUrl : detailUrlList){
                                String detailHtml = URLFetcher.pickData(detailUrl);
                                String detailName = lightDetailHtml(detailHtml);
                                System.out.println("                                                " + detailName);
                            }
                        }
                    }

                }
            }
        }
    }*/



    public static void main(String[] args) throws FileNotFoundException {

        final Map<Integer, List<ParamBean>> paramMap = new HashMap<>();

        List<ParamBean> list = ParamFactory.readDataFile(Constants.PARAMSLIST, List.class);

        final Map<Integer, String> allMakeMap = ParamFactory.readDataFile(Constants.ALLMAKEMAPFILE, Map.class);

        final LightTypeListData lightTypeListData = ParamFactory.readDataFile(Constants.LIGHTTYPELISTDATA, LightTypeListData.class);

        for(int i = 0; i < list.size(); ++i){
            if(i == 0){
                List<ParamBean> paramBeans = new ArrayList<>();
                paramBeans.add(list.get(i));
                paramMap.put(list.get(i).getMakeId(), paramBeans);
            }else if(list.get(i).getMakeId().equals(list.get(i - 1).getMakeId())){
                List<ParamBean> paramBeans = paramMap.get(list.get(i).getMakeId());
                paramBeans.add(list.get(i));
                paramMap.put(list.get(i).getMakeId(), paramBeans);
            }else{
                List<ParamBean> paramBeans = new ArrayList<>();
                paramBeans.add(list.get(i));
                paramMap.put(list.get(i).getMakeId(), paramBeans);
            }
        }
        System.out.println("---");
        for(final Map.Entry<Integer, List<ParamBean>> param : paramMap.entrySet()) {
            if(FileUtil.xlsExist(allMakeMap.get(param.getKey()))){
                System.out.println(allMakeMap.get(param.getKey()) + " 制造商表格已经生成!");
                continue;
            }
            final List<DataBean> dataList = new ArrayList<>();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for(final ParamBean p : param.getValue()){


                        String lightTypeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_LIGHT_TYPE,
                                YearMapData.yearMap.get(p.getYearId()),
                                allMakeMap.get(p.getMakeId()),
                                p.getType()), URLFetcher.TRY_NUM);
                        Map<String, Map<String, String>> lightTypeMap = lightTypeParser(lightTypeHtml);

                        Map<String, List<String>> lightMap = new HashMap<>();
                        int max = 0;

                        Map<String, String> forwardMap = lightTypeMap.get("forward");
                        if(forwardMap != null && !forwardMap.isEmpty()){
                            for(String forwardLight : lightTypeListData.getForwardLightList()){
                                if(forwardMap.get(forwardLight) == null){
                                    continue;
                                }

                                List<String> lightList = new ArrayList<>();

                                String lightDetailHtml = URLFetcher.pickData(forwardMap.get(forwardLight), URLFetcher.TRY_NUM);
                                List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                for(String detailUrl : detailUrlList){
                                    String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                    String detailName = lightDetailHtml(detailHtml);
                                    System.out.println(allMakeMap.get(p.getMakeId()) + "  :  "
                                            + YearMapData.yearMap.get(p.getYearId()) + "  :  "
                                            + p.getType() + "  :  "
                                            + detailName);
                                    lightList.add(detailName);
                                    break;//一个系统的灯只要一个
                                }
                                lightMap.put(forwardLight, lightList);
                                max = max > lightList.size() ? max : lightList.size();
                            }
                        }

                        Map<String, String> exteriorMap = lightTypeMap.get("exterior");
                        if(exteriorMap != null && !exteriorMap.isEmpty()){
                            for(String exteriorLight : lightTypeListData.getExteriorLightList()){
                                if(exteriorMap.get(exteriorLight) == null){
                                    continue;
                                }

                                List<String> lightList = new ArrayList<>();

                                String lightDetailHtml = URLFetcher.pickData(exteriorMap.get(exteriorLight), URLFetcher.TRY_NUM);
                                List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                for(String detailUrl : detailUrlList){
                                    String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                    String detailName = lightDetailHtml(detailHtml);
                                    System.out.println(allMakeMap.get(p.getMakeId()) + "  :  "
                                            + YearMapData.yearMap.get(p.getYearId()) + "  :  "
                                            + p.getType() + "  :  "
                                            + detailName);
                                    lightList.add(detailName);
                                    break;//一个系统的灯只要一个
                                }
                                lightMap.put(exteriorLight, lightList);
                                max = max > lightList.size() ? max : lightList.size();
                            }
                        }

                        Map<String, String> interiorMap = lightTypeMap.get("interior");
                        if(interiorMap != null && !interiorMap.isEmpty()){
                            for(String interiorLight : lightTypeListData.getInteriorLightList()){
                                if(interiorMap.get(interiorLight) == null){
                                    continue;
                                }

                                List<String> lightList = new ArrayList<>();

                                String lightDetailHtml = URLFetcher.pickData(interiorMap.get(interiorLight), URLFetcher.TRY_NUM);
                                List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                for(String detailUrl : detailUrlList){
                                    String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                    String detailName = lightDetailHtml(detailHtml);
                                    System.out.println(allMakeMap.get(p.getMakeId()) + "  :  "
                                            + YearMapData.yearMap.get(p.getYearId()) + "  :  "
                                            + p.getType() + "  :  "
                                            + detailName);
                                    lightList.add(detailName);
                                    break;//一个系统的灯只要一个
                                }
                                lightMap.put(interiorLight, lightList);
                                max = max > lightList.size() ? max : lightList.size();
                            }
                        }

                        for(int i = 0; i < max; ++i){
                            DataBean dataBean = new DataBean();
                            dataBean.setMake(allMakeMap.get(p.getMakeId()));
                            dataBean.setYear(YearMapData.yearMap.get(p.getYearId()));
                            dataBean.setType(p.getType());
                            Map<String, String> map = new HashMap<>();
                            for(String light : Constants.alLightList){
                                List<String> list = lightMap.get(light);
                                if(list != null && list.size() - 1 >= i){
                                    map.put(light, list.get(i));
                                }else{
                                    map.put(light, "");
                                }
                            }
                            dataBean.setLightMap(map);
                            dataList.add(dataBean);
                        }
                    }
                    ExcelUtil.excelExport(allMakeMap.get(param.getKey()), Constants.titles, dataList);
                }
            });
        }

        executorService.shutdown();

    }

}
