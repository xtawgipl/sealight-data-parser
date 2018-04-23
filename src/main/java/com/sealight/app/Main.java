package com.sealight.app;

import com.sealight.app.bean.Constants;
import com.sealight.app.bean.LightTypeListData;
import com.sealight.app.bean.ParamBean;
import com.sealight.app.util.DataParser;
import com.sealight.app.util.ExcelUtil;
import com.sealight.app.util.FileUtil;
import com.sealight.app.util.URLFetcher;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * 主类
 *
 * @author zhangjj
 * @create 2018-04-23 9:15
 **/
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String yearHtml = URLFetcher.pickData(URLFetcher.SYLVANIA_YEAR);
        Map<Integer, String> yearMap = DataParser.yearParser(yearHtml);

        final Map<Integer, List<ParamBean>> paramMap = new HashMap<>();

        List<ParamBean> list = ParamFactory.readDataFile(Constants.PARAMSLIST_2, List.class);

        final Map<Integer, String> allMakeMap = ParamFactory.readDataFile(Constants.ALLMAKEMAPFILE, Map.class);

        for(int i = 0; i < list.size(); ++i){
            if(i == 0){
                List<ParamBean> paramBeans = new ArrayList<>();
                paramBeans.add(list.get(i));
                paramMap.put(list.get(i).getMakeId(), paramBeans);
            }else if(paramMap.keySet().contains(list.get(i).getMakeId())){
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

            LightTypeListData lightTypeListData = new LightTypeListData();
            for(final ParamBean p : param.getValue()){

                if(p.getInteriorLightMap() != null && !p.getInteriorLightMap().isEmpty()){
//                            System.out.println(lightTypeListData.getInteriorLightList());
//                            System.out.println(p.getInteriorLightMap().keySet());
                    if(lightTypeListData.getInteriorLightList() == null){
                        lightTypeListData.setInteriorLightList(new HashSet<>());
                    }
                    lightTypeListData.getInteriorLightList().addAll(p.getInteriorLightMap().keySet());
                }

                if(p.getForwardLightMap() != null && !p.getForwardLightMap().isEmpty()){
                    if(lightTypeListData.getForwardLightList() == null){
                        lightTypeListData.setForwardLightList(new HashSet<>());
                    }
                    lightTypeListData.getForwardLightList().addAll(p.getForwardLightMap().keySet());
                }

                if(p.getExteriorLightMap() != null && !p.getExteriorLightMap().isEmpty()){
                    if(lightTypeListData.getExteriorLightList() == null){
                        lightTypeListData.setExteriorLightList(new HashSet<>());
                    }
                    lightTypeListData.getExteriorLightList().addAll(p.getExteriorLightMap().keySet());
                }

            }
            System.out.println(String.format("开始生成：%s, 行数 %s ", allMakeMap.get(param.getKey()), param.getValue().size()));
            ExcelUtil.excelExport(yearMap, allMakeMap.get(param.getKey()), lightTypeListData, param.getValue());

        }
    }
}
