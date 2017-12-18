package com.sealight.app;

import java.util.ArrayList;
import java.util.List;

/**
 * 灯类型数据
 *
 * @author zhangjj
 * @create 2017-12-10 18:05
 **/
public class LightTypeListData {
    public static List<String> forwardLightList = new ArrayList<>();
    public static List<String> exteriorLightList = new ArrayList<>();
    public static List<String> interiorLightList = new ArrayList<>();
    static{
        forwardLightList.add("Headlight High Beam and Low Beam");
        forwardLightList.add("Headlight High Beam");
        forwardLightList.add("Headlight Low Beam");
        forwardLightList.add("Fog Light Bulb Front");


        exteriorLightList.add("Back up Light");
        exteriorLightList.add("Brake Light");
        exteriorLightList.add("Turn Signal (Front)");
        exteriorLightList.add("License Plate Light");
        exteriorLightList.add("Parking Light");
        exteriorLightList.add("Turn Signal (Rear)");
        exteriorLightList.add("Tail Light");
        exteriorLightList.add("Side Marker Light Bulb Front");
        exteriorLightList.add("Side Marker Light Bulb Rear");
        exteriorLightList.add("Stepwell Light Bulb");
        exteriorLightList.add("Center High Mount Stop Light Bulb");
        exteriorLightList.add("Daytime Running Light Bulb");


        interiorLightList.add("Dome Light");
        interiorLightList.add("Glove Box Light");
        interiorLightList.add("Map Light");
        interiorLightList.add("Trunk or Cargo Area Light");
        interiorLightList.add("Floor Console Compartment Light Bulb");
        interiorLightList.add("Interior Door Light Bulb");
        interiorLightList.add("Vanity Mirror Light");
        interiorLightList.add("Door Mirror Illumination Light Bulb");
    }

}
