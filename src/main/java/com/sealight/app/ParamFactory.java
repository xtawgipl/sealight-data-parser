package com.sealight.app;

import com.sealight.app.bean.ParamBean;
import com.sealight.app.util.FileUtil;
import com.sealight.app.util.SerUtil;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangjj
 * @create 2018-04-15 21:19
 **/
public class ParamFactory {


    public final static String ALLMAKEMAPFILE = "D:/data/cache/allMakeMap.dat";

    public final static String ALLMAKEFORYEARMAP = "D:/data/cache/allMakeForYearMap.dat";

    public final static String PARAMSLIST = "D:/data/cache/paramsList.dat";


    public final static ExecutorService executorService = Executors.newFixedThreadPool(50);



    public static void writeDataFile(String path, Object data){
        SerUtil.writeObject(path , data);
    }

    public static <T> T readDataFile(String path, Class<T> clazz){
        return SerUtil.readObject(path, clazz);
    }


    /**
     * 生成的有制造商及 制造商对应的年份
     * @param
     * @author zhangjj
     * @Date
     * @return
     * @exception
     *
     */
    private static void factory1(){
        Map<Integer, String> allMakeMap = new HashMap<>();//所有的制造商id
        Map<Integer, List<Integer>> allMakeForYearMap = new HashMap<>();//key制造商id，value年份集合

        Map<Integer, Set<Integer>> yearMakeMap = new HashMap<>();//key年份id,value制造商集合

        String yearHtml = URLFetcher.pickData(URLFetcher.SYLVANIA_YEAR);
        Map<Integer, String> yearMap = DataParser.yearParser(yearHtml);
        for(Map.Entry<Integer, String> entry : yearMap.entrySet()){
            String makeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_MAKE, entry.getKey()));
//            System.out.println("makeHtml --> " + makeHtml);
            Map<Integer, String> makeMap = DataParser.makeParser(makeHtml);
            yearMakeMap.put(entry.getKey(), makeMap.keySet());
//            System.out.println(entry.getValue() + " 年的制造商：");
            for(Map.Entry<Integer, String> makeEntry : makeMap.entrySet()){
//                System.out.println("             " + makeEntry.getKey() + " -- > " + makeEntry.getValue());
                allMakeMap.put(makeEntry.getKey(), makeEntry.getValue());
            }
        }

        Set<Map.Entry<Integer, Set<Integer>>> entries = yearMakeMap.entrySet();
        List<Integer> yearList;
        for(Integer makeId : allMakeMap.keySet()){
            yearList = new ArrayList<>();
            for(Map.Entry<Integer, Set<Integer>> entry : entries){
                if(entry.getValue().contains(makeId)){
                    yearList.add(entry.getKey());
                }
            }
            allMakeForYearMap.put(makeId, yearList);
        }

        writeDataFile(ALLMAKEMAPFILE, allMakeMap);
        writeDataFile(ALLMAKEFORYEARMAP, allMakeForYearMap);

    }

    /**
     * 生成制造商、年份、型号
     * @param
     * @author zhangjj
     * @Date
     * @return
     * @exception
     *
     */
    private static void factory2(){
        final List<ParamBean> paramsList = new ArrayList<>();

        Map<Integer, List<Integer>> allMakeForYearMap = readDataFile(ALLMAKEFORYEARMAP, Map.class);

        Map<Integer, String> allMakeMap = readDataFile(ALLMAKEMAPFILE, Map.class);

        final CountDownLatch latch = new CountDownLatch(allMakeForYearMap.size());
        for(final Map.Entry<Integer, List<Integer>> allMakeForYearEntry : allMakeForYearMap.entrySet()){
            if(FileUtil.xlsExist(allMakeMap.get(allMakeForYearEntry.getKey()))){
//                System.out.println(allMakeMap.get(allMakeForYearEntry.getKey()) + " 制造商表格已经生成!");
                latch.countDown();
                continue;
            }

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    for(Integer yearId : allMakeForYearEntry.getValue()){
                        try {
                            String typeHtml = URLFetcher.pickData(String.format(URLFetcher.SYLVANIA_TYPE, allMakeForYearEntry.getKey(), yearId), URLFetcher.TRY_NUM);
                            Map<Integer, String> typeMap = DataParser.typeParser(typeHtml);
                            for(Map.Entry<Integer, String> typeEntry : typeMap.entrySet()) {
                                ParamBean paramBean = new ParamBean(allMakeForYearEntry.getKey(), typeEntry.getValue(), yearId);
//                                String temp = "paramsList.add(new ParamBean("+allMakeForYearEntry.getKey()+", \""+typeEntry.getValue()+"\", "+yearId+"));";
//                                System.out.println(temp);

                                paramsList.add(paramBean);
                            }
                        }catch (Throwable e){
                            System.err.println("异常，直接返回执行完线程!");
                            e.printStackTrace();
                            return ;
                        }
                    }
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();


        try {
            latch.await();
            System.out.println("---------------");
            System.out.println("---------------");
            System.out.println("---------------");
            System.out.println("---------------");
            Collections.sort(paramsList, new Comparator<ParamBean>() {
                @Override
                public int compare(ParamBean o1, ParamBean o2) {
                    int res = o1.getMakeId().compareTo(o2.getMakeId());
                    if(res == 0){
                        res = o1.getType().compareTo(o2.getType());
                    }
                    if(res == 0){
                        res = o1.getYearId().compareTo(o2.getYearId());
                    }
                    return res;
                }
            });
            writeDataFile(PARAMSLIST, paramsList);
            System.out.println("finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*factory1();

        Map<Integer, List<Integer>> allMakeForYearMap = readDataFile(ALLMAKEFORYEARMAP, Map.class);

        System.out.println(allMakeForYearMap);

        Map<Integer, String> allMakeMap = readDataFile(ALLMAKEMAPFILE, Map.class);

        System.out.println(allMakeMap);*/

        factory2();
    }


}
