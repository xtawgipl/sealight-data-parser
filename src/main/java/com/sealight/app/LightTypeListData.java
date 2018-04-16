package com.sealight.app;

import java.util.List;
import java.util.Set;

/**
 * 灯类型数据
 *
 * @author zhangjj
 * @create 2017-12-10 18:05
 **/
public class LightTypeListData {
    /**
     * 前灯
     */
    private Set<String> forwardLightList;
    /**
     * 外灯
     */
    private Set<String> exteriorLightList;
    /**
     * 内灯
     */
    private Set<String> interiorLightList;



    public Set<String> getForwardLightList() {
        return forwardLightList;
    }

    public void setForwardLightList(Set<String> forwardLightList) {
        this.forwardLightList = forwardLightList;
    }

    public Set<String> getExteriorLightList() {
        return exteriorLightList;
    }

    public void setExteriorLightList(Set<String> exteriorLightList) {
        this.exteriorLightList = exteriorLightList;
    }

    public Set<String> getInteriorLightList() {
        return interiorLightList;
    }

    public void setInteriorLightList(Set<String> interiorLightList) {
        this.interiorLightList = interiorLightList;
    }
}
