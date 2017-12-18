package com.sealight.app;

import com.sealight.app.bean.Constants;
import com.sealight.app.bean.DataBean;
import com.sealight.app.util.ExcelUtil;
import com.sealight.app.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class DataParserBackup {

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

    private static List<String> getDetailUrlList(String detailHtml){
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



    public static void main(String[] args) {

        Map<Integer, List<Integer>> allMakeForYearMap = MakeMapData.allMakeForYearMap;
        for(final Map.Entry<Integer, List<Integer>> allMakeForYearEntry : allMakeForYearMap.entrySet()){

            if(FileUtil.xlsExist(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()))){
                System.out.println(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()) + " 制造商表格已经生成!");
                continue;
            }

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<DataBean> dataList = new ArrayList<>();
                    for(Integer yearId : allMakeForYearEntry.getValue()){
                      try {

                          String typeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_TYPE, allMakeForYearEntry.getKey(), yearId), URLFetcher.TRY_NUM);
                          Map<Integer, String> typeMap = typeParser(typeHtml);
                          for(Map.Entry<Integer, String> typeEntry : typeMap.entrySet()) {
                              String lightTypeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_LIGHT_TYPE,
                                      YearMapData.yearMap.get(yearId),
                                      MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()),
                                      typeEntry.getValue()), URLFetcher.TRY_NUM);
                              Map<String, Map<String, String>> lightTypeMap = lightTypeParser(lightTypeHtml);

                              Map<String, List<String>> lightMap = new HashMap<>();
                              int max = 0;

                              Map<String, String> forwardMap = lightTypeMap.get("forward");
                              if(forwardMap != null && !forwardMap.isEmpty()){
                                  for(String forwardLight : LightTypeListData.forwardLightList){
                                      if(forwardMap.get(forwardLight) == null){
                                          continue;
                                      }

                                      List<String> lightList = new ArrayList<>();

                                      String lightDetailHtml = URLFetcher.pickData(forwardMap.get(forwardLight), URLFetcher.TRY_NUM);
                                      List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                      for(String detailUrl : detailUrlList){
                                          String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                          String detailName = lightDetailHtml(detailHtml);
                                          System.out.println(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()) + "  :  "
                                                  + YearMapData.yearMap.get(yearId) + "  :  "
                                                  + typeEntry.getValue() + "  :  "
                                                  + detailName);
                                          lightList.add(detailName);
                                      }
                                      lightMap.put(forwardLight, lightList);
                                      max = max > lightList.size() ? max : lightList.size();
                                  }
                              }

                              Map<String, String> exteriorMap = lightTypeMap.get("exterior");
                              if(exteriorMap != null && !exteriorMap.isEmpty()){
                                  for(String exteriorLight : LightTypeListData.exteriorLightList){
                                      if(exteriorMap.get(exteriorLight) == null){
                                          continue;
                                      }

                                      List<String> lightList = new ArrayList<>();

                                      String lightDetailHtml = URLFetcher.pickData(exteriorMap.get(exteriorLight), URLFetcher.TRY_NUM);
                                      List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                      for(String detailUrl : detailUrlList){
                                          String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                          String detailName = lightDetailHtml(detailHtml);
                                          System.out.println(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()) + "  :  "
                                                  + YearMapData.yearMap.get(yearId) + "  :  "
                                                  + typeEntry.getValue() + "  :  "
                                                  + detailName);
                                          lightList.add(detailName);
                                      }
                                      lightMap.put(exteriorLight, lightList);
                                      max = max > lightList.size() ? max : lightList.size();
                                  }
                              }

                              Map<String, String> interiorMap = lightTypeMap.get("interior");
                              if(interiorMap != null && !interiorMap.isEmpty()){
                                  for(String interiorLight : LightTypeListData.interiorLightList){
                                      if(interiorMap.get(interiorLight) == null){
                                          continue;
                                      }

                                      List<String> lightList = new ArrayList<>();

                                      String lightDetailHtml = URLFetcher.pickData(interiorMap.get(interiorLight), URLFetcher.TRY_NUM);
                                      List<String> detailUrlList = getDetailUrlList(lightDetailHtml);
                                      for(String detailUrl : detailUrlList){
                                          String detailHtml = URLFetcher.pickData(detailUrl, URLFetcher.TRY_NUM);
                                          String detailName = lightDetailHtml(detailHtml);
                                          System.out.println(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()) + "  :  "
                                                  + YearMapData.yearMap.get(yearId) + "  :  "
                                                  + typeEntry.getValue() + "  :  "
                                                  + detailName);
                                          lightList.add(detailName);
                                      }
                                      lightMap.put(interiorLight, lightList);
                                      max = max > lightList.size() ? max : lightList.size();
                                  }
                              }

                              for(int i = 0; i < max; ++i){
                                  DataBean dataBean = new DataBean();
                                  dataBean.setMake(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()));
                                  dataBean.setYear(YearMapData.yearMap.get(yearId));
                                  dataBean.setType(typeEntry.getValue());
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
                      }catch (Throwable e){
                          System.err.println("异常，直接返回执行完线程!");
                          e.printStackTrace();
                          return ;
                      }
                    }
                    ExcelUtil.excelExport(MakeMapData.allMakeMap.get(allMakeForYearEntry.getKey()), Constants.titles, dataList);
                }
            });
        }
        executorService.shutdown();
    }

}
