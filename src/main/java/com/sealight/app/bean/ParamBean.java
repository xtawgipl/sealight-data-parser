package com.sealight.app.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhangjj
 * @create 2017-12-16 15:50
 **/
public class ParamBean implements Serializable{

    private Integer makeId;

    private String type;

    private Integer yearId;


    /**
     * 前灯
     */
    private Map<String, String> forwardLightMap;
    /**
     * 外灯
     */
    private Map<String, String> exteriorLightMap;
    /**
     * 内灯
     */
    private Map<String, String> interiorLightMap;


    public ParamBean(Integer makeId, String type, Integer yearId) {
        this.makeId = makeId;
        this.type = type;
        this.yearId = yearId;
    }

    public Integer getMakeId() {
        return makeId;
    }

    public void setMakeId(Integer makeId) {
        this.makeId = makeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getYearId() {
        return yearId;
    }

    public void setYearId(Integer yearId) {
        this.yearId = yearId;
    }

    public Map<String, String> getForwardLightMap() {
        return forwardLightMap;
    }

    public void setForwardLightMap(Map<String, String> forwardLightMap) {
        this.forwardLightMap = forwardLightMap;
    }

    public Map<String, String> getExteriorLightMap() {
        return exteriorLightMap;
    }

    public void setExteriorLightMap(Map<String, String> exteriorLightMap) {
        this.exteriorLightMap = exteriorLightMap;
    }

    public Map<String, String> getInteriorLightMap() {
        return interiorLightMap;
    }

    public void setInteriorLightMap(Map<String, String> interiorLightMap) {
        this.interiorLightMap = interiorLightMap;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return "ParamBean{" +
                "makeId=" + makeId +
                ", type='" + type + '\'' +
                ", yearId=" + yearId +
                '}';
    }
}
