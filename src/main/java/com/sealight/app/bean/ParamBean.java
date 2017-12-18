package com.sealight.app.bean;

import java.io.Serializable;

/**
 * @author zhangjj
 * @create 2017-12-16 15:50
 **/
public class ParamBean implements Serializable{

    private Integer makeId;

    private String type;

    private Integer yearId;


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

    @Override
    public String toString() {
        return "ParamBean{" +
                "makeId=" + makeId +
                ", type='" + type + '\'' +
                ", yearId=" + yearId +
                '}';
    }
}
