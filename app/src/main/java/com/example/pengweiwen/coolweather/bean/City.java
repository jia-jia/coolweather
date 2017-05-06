package com.example.pengweiwen.coolweather.bean;

/**
 * 城市bean类
 * id  数据库表中的id 自增长
 * cityName 城市名
 * cityCode 城市编码
 * cityId 与cityId相关联的外键
 * Created by pengweiwen on 2017/5/5.
 */

public class City {

    private int id;
    private String cityName;
    private String cityCode;
    private int  provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}

