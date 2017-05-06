package com.example.pengweiwen.coolweather.bean;

/**
 * id 数据库表中的id
 * countryCode 县级编号
 * countryName  县级名字
 * cityId  与城市有关的外键
 * Created by pengweiwen on 2017/5/5.
 */

public class Country {

    private int id;
    private String countryCode;
    private String countryName;
    private int  cityId;

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
