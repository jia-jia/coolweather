package com.example.pengweiwen.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络请求类（耗时操作，必须要在线程下进行访问）
 * Created by pengweiwen on 2017/5/5.
 */
public class HttpUtil {
    /*
    在这里，这里只是从服务器拿到数据，所以不用joson数据
     */
    public static void sendRequest(final String adress, final HttpCallbackListener httpCallbackListener) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(adress);
                    httpURLConnection = (HttpURLConnection) url.openConnection();//打开连接
                    httpURLConnection.setRequestMethod("GET");//设置请求方式为GET
                    httpURLConnection.setConnectTimeout(3000);//设置连接超时为8秒
                    httpURLConnection.setReadTimeout(3000);//设置读取超时也为8秒
                    InputStream inputStream = httpURLConnection.getInputStream();//获取输入流
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//包装流
                    StringBuilder builder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }

                    if (httpCallbackListener != null) {
                        httpCallbackListener.onFinish(builder.toString());//如果成功的话，把结果传给接口方法，从这里发出去
                    }
                } catch (MalformedURLException e) {
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onError(e);//访问失败时
                    }
                } catch (IOException e) {
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onError(e);//访问失败时
                    }
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();//断开连接
                    }
                }
            }
        }.start();
    }
    public interface HttpCallbackListener {

        void onFinish(String responseSuccess);

        void onError(Exception e);

    }
}
