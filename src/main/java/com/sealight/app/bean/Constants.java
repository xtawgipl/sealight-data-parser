package com.sealight.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类
 *
 * @author zhangjj
 * @create 2017-12-11 21:42
 **/
public class Constants {

    /** 表格存放目录*/
    public static final String FILE_PATH = "D:/data/";

    /** 表格 标识 */
    public static final String FILE_SIGN = "sealightData_";



    /** 表头*/
    public static List<String> titles = new ArrayList<>();

    /** 所有灯 类型*/
    public static List<String> alLightList = new ArrayList<>();

    static{
//        titles.add("制造商");
        titles.add("型号");
        titles.add("年份");

        /**前灯 */
        alLightList.add("Headlight High Beam and Low Beam");
        alLightList.add("Headlight High Beam");
        alLightList.add("Headlight Low Beam");
        alLightList.add("Fog Light Bulb Front");

        /**外灯 */
        alLightList.add("Back up Light");
        alLightList.add("Brake Light");
        alLightList.add("Turn Signal (Front)");
        alLightList.add("License Plate Light");
        alLightList.add("Parking Light");
        alLightList.add("Turn Signal (Rear)");
        alLightList.add("Tail Light");
        alLightList.add("Side Marker Light Bulb Front");
        alLightList.add("Side Marker Light Bulb Rear");
        alLightList.add("Stepwell Light Bulb");
        alLightList.add("Center High Mount Stop Light Bulb");
        alLightList.add("Daytime Running Light Bulb");

        /**内灯 */
        alLightList.add("Dome Light");
        alLightList.add("Glove Box Light");
        alLightList.add("Map Light");
        alLightList.add("Trunk or Cargo Area Light");
        alLightList.add("Floor Console Compartment Light Bulb");
        alLightList.add("Interior Door Light Bulb");
        alLightList.add("Vanity Mirror Light");
        alLightList.add("Door Mirror Illumination Light Bulb");

        titles.addAll(alLightList);
    }
}
