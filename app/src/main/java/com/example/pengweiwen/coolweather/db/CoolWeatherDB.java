package com.example.pengweiwen.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pengweiwen.coolweather.bean.City;
import com.example.pengweiwen.coolweather.bean.Country;
import com.example.pengweiwen.coolweather.bean.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类将一些数据库常用的操作封装起来
 * Created by pengweiwen on 2017/5/5.
 */

public class CoolWeatherDB {
    /*
    数据库名称
     */
    public static final String DB_NAME = "cool_weather";
    /*
    数据库版本
     */
    public static final int VERSION = 1;
    /*
    声明CoolWeatherDB类
     */
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    /*
    将构造方法私有化
     */
    private CoolWeatherDB(Context context) {
        WeatherDatabase weatherDatabase = new WeatherDatabase(context, DB_NAME, null, VERSION);//创建数据库
        db = weatherDatabase.getWritableDatabase();//设置为可写操作
    }

    /*
    获取CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {

        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /*
    将Province实例存储到数据库中
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }

    }

    /*
    从数据库中读取全国省份信息
     */
    public List<Province> loadProvinces() {

        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /*
    将City实例存储到数据库中
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

/*
从数据库中读取所有省份中的所有城市信息,保存到bean中
 */

    public List<City> loadCity(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?",new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    City city = new City();
                    city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    city.setProvinceId(provinceId);
                    city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                    city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                    list.add(city);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }

        }
        return list;
    }
/*
将县城的实例保存到数据库中去
 */
    public void saveCountry(Country country) {
        if (country != null) {
            ContentValues values = new ContentValues();
            values.put("country_name", country.getCountryName());
            values.put("country_code", country.getCountryCode());
            values.put("city_id", country.getCityId());
            db.insert("Country", null, values);
        }
    }
/*
从数据库中读取所有的数据到bean中
 */
    public List<Country> loadCountry(int cityId) {

        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCityId(cityId);
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                list.add(country);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}



