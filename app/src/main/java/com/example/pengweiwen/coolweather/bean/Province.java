package com.example.pengweiwen.coolweather.bean;

/**
 * 省级编号 provinceCode
 * 省名     provinceName
 * 对应数据库表中的id
 * Created by pengweiwen on 2017/5/5.
 */

public class Province {
    private String provinceCode;
    private String provinceName;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

}
