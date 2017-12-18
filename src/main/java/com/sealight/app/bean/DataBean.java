package com.sealight.app.bean;

import java.util.Map;

/**
 * 组装excel的bean
 *
 * @author zhangjj
 * @create 2017-12-11 21:10
 **/
public class DataBean {

    /** 制造商，用作sheel名 */
    private String make;

    /** 汽车生产 年份 */
    private String year;

    /** 汽车年份 */
    private String type;

    /** 灯 */
    private Map<String, String> lightMap;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getLightMap() {
        return lightMap;
    }

    public void setLightMap(Map<String, String> lightMap) {
        this.lightMap = lightMap;
    }
}
