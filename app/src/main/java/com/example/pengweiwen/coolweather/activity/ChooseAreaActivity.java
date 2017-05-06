package com.example.pengweiwen.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengweiwen.coolweather.R;
import com.example.pengweiwen.coolweather.bean.City;
import com.example.pengweiwen.coolweather.bean.Country;
import com.example.pengweiwen.coolweather.bean.Province;
import com.example.pengweiwen.coolweather.db.CoolWeatherDB;
import com.example.pengweiwen.coolweather.util.AnalyticalData;
import com.example.pengweiwen.coolweather.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengweiwen on 2017/5/5.
 */

public class ChooseAreaActivity extends Activity {
    private TextView tv_choose_activity_title;
    private ListView listView;
    private CoolWeatherDB coolWeather;
    private List<Province> provincelist;
    private List<Country> countrieslist;
    private Province provinced;//被选中过后
    private List<City> citieslist;
    private List<String> datalist = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;//加载框
    private City selectedCity;
    /*
    当前选中的级别
     */
    private int currentLevel;
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(this);
//        if (pres.getBoolean("city_selected",false)){
//            Intent intent = new Intent(this,WeatherActivity.class);
//            startActivity(intent);
//
//            return;
//        }

        setContentView(R.layout.choose_area);
        initView();//初始化控件
        //得到CoolWeatherDB的实例
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        coolWeather=CoolWeatherDB.getInstance(this);//间接的就把数据库给创建了
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                  provinced =   provincelist.get(position);
                   queryCity();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity = citieslist.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTRY){
                    String countyCode =countrieslist.get(position).getCountryCode();
                    Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                    intent.putExtra("county_code",countyCode);
                    startActivity(intent);


                }
            }
        });
        queryProvince();//加载省级数据
    }
/*
查询省份信息
 */
    private void queryProvince() {
        provincelist = coolWeather.loadProvinces();//查询省份信息，返回一个集合Province对象
        if (provincelist.size()>0){
            //第二次进来的时候要清空集合，重新加载
            datalist.clear();
          for (Province province:provincelist){
              datalist.add(province.getProvinceName());
          }
           adapter.notifyDataSetChanged();
            listView.setSelection(0);
            tv_choose_activity_title.setText("中国");
            //改变当前选中的状态
            currentLevel = LEVEL_PROVINCE;
         //当数据库没有数据的时候，再去从服务器中获取
        }else{
            queryServer(null,"province");
        }
    }
    /*
    查询市级信息
     */
    private void queryCity(){
        citieslist =   coolWeather.loadCity(provinced.getId());
        if (citieslist.size()>0){
              datalist.clear();
            for (City city:citieslist){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            tv_choose_activity_title.setText(provinced.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryServer(provinced.getProvinceCode(),"city");
        }
    }
    /*
    查询县级信息
     */
    private void queryCounties(){
        countrieslist =    coolWeather.loadCountry(selectedCity.getId());
        if (countrieslist.size()>0){
            datalist.clear();
            for (Country country:countrieslist){
                datalist.add(country.getCountryName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            tv_choose_activity_title.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTRY;
        }else{
         queryServer(selectedCity.getCityCode(),"country");
        }
    }
/*
查询服务器
code 表明传过来的编码
 */
    private void queryServer(final String code, final String type) {

        String adress;
        //在这里进行判断，如果编码为空的话，就说明是查询省份信息，如果不为空的话，就说明是查询市级或者县级的信息
        if (!TextUtils.isEmpty(code)){
            adress = "http://www.weather.com.cn/data/list3/city"+code+".xml";//这是查询市级或者县级的信息
        }else{
            adress = "http://www.weather.com.cn/data/list3/city.xml";//查询省份信息，
        }
        showProgressDialog();//一般状态下，都会有一个加载框
        HttpUtil.sendRequest(adress, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String responseSuccess) {
                boolean result = false;
                if ("province".equals(type)){

                    result = AnalyticalData.handleProvincesResponse(coolWeather,responseSuccess);
                }else if ("city".equals(type)){

                    result = AnalyticalData.handleCityResponse(coolWeather,responseSuccess,provinced.getId());
                }else if ("country".equals(type)){

                    result = AnalyticalData.handleCountryResponse(coolWeather,responseSuccess,selectedCity.getId());
                }
                if (result){
                    //通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvince();
                            }else if ("city".equals(type)){
                                queryCity();
                            }else if ("country".equals(type)){
                                queryCounties();
                            }
                        }
                    });

                }
            }

            @Override
            public void onError(Exception e) {

                //通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
//显示加载框
    private void showProgressDialog() {

         if (progressDialog==null){
             progressDialog = new ProgressDialog(this);
             progressDialog.setMessage("正在加载中...");
             progressDialog.setCanceledOnTouchOutside(false);
         }
            progressDialog.show();


    }
//销毁加载框
    private void closeProgressDialog(){

        if (progressDialog!=null){
            progressDialog.dismiss();
        }

    }
    private void initView(){

        tv_choose_activity_title = (TextView) findViewById(R.id.tv_choose_activity_title);
        listView = (ListView) findViewById(R.id.listView);

}

    @Override
    public void onBackPressed() {
        if (currentLevel==LEVEL_COUNTRY){
            queryCity();
        }else if (currentLevel == LEVEL_CITY){
           queryProvince();
        }else {
            finish();
        }
    }
}
