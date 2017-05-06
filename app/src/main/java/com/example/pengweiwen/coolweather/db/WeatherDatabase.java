package com.example.pengweiwen.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建城市，省份，县级数据库
 * Created by pengweiwen on 2017/5/5.
 */

public class WeatherDatabase extends SQLiteOpenHelper {

    /*
    创建省份数据库（表）
     */
    private static final String CREATE_PROVINCE = "create table Province(" +
            "id integer primary key autoincrement," +
            "province_name text," +
            "province_code text)";
    /*
    创建城市数据库表
     */
    private static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "city_code text," +
            "province_id integer)";
    /*
    创建县级数据库表
     */
    private static final String CREATE_COUNTRY = "create table Country(" +
            "id integer primary key autoincrement," +
            "country_name text," +
            "country_code text," +
            "city_id integer)";

    public WeatherDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PROVINCE);//创建Province表
        db.execSQL(CREATE_CITY);//创建City表
        db.execSQL(CREATE_COUNTRY);//创建Country表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
