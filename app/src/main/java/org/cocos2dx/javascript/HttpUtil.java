package org.cocos2dx.javascript;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * HttpURLConnection网络请求工具类
 */
public class HttpUtil {
    /**
     * Get请求
     */
    public static void sendGetRequest(
            final String urlString, final HttpCallbackListener listener) {
        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    // 根据URL地址创建URL对象
                    url = new URL(urlString);
                    // 获取HttpURLConnection对象
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式，默认为GET
                    httpURLConnection.setRequestMethod("GET");
                    // 设置连接超时
                    httpURLConnection.setConnectTimeout(5000);
                    // 设置读取超时
                    httpURLConnection.setReadTimeout(8000);
                    /*// 如果需要设置apikey，如下设置：
                    httpURLConnection.setRequestProperty(
                    "apikey", "1b18****13f3****729210d6****8e29");*/

                    // 响应码为200表示成功，否则失败。
                    if (httpURLConnection.getResponseCode() != 200) {
                        Log.i("HttpUtil", "请求失败");
                    }
                    // 获取网络的输入流
                    InputStream is = httpURLConnection.getInputStream();
                    // 读取输入流中的数据
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    while ((len = bis.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                    }
                    bis.close();
                    is.close();
                    // 响应的数据
                    byte[] response = baos.toByteArray();

                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response);
                    }
                } catch (MalformedURLException e) {

                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } catch (IOException e) {

                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (httpURLConnection != null) {
                        // 释放资源
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * Post请求
     */
    public static void sendPostRequest(
            final String urlString, final HttpCallbackListener listener) {

        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(urlString);
                    httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(8000);

                    // 设置运行输入
                    httpURLConnection.setDoInput(true);
                    // 设置运行输出
                    httpURLConnection.setDoOutput(true);

                    // 请求的数据
                    String data = "num=" + URLEncoder.encode("10", "UTF-8") +
                            "&page=" + URLEncoder.encode("1", "UTF-8");

                    // 将请求的数据写入输出流中
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    bos.write(data.getBytes());
                    bos.flush();
                    bos.close();
                    os.close();

                    if (httpURLConnection.getResponseCode() == 200) {

                        InputStream is = httpURLConnection.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len = bis.read(bytes)) != -1) {
                            baos.write(bytes, 0, len);
                        }
                        is.close();
                        bis.close();
                        // 响应的数据
                        byte[] response = baos.toByteArray();

                        if (listener != null) {
                            // 回调onFinish()方法
                            listener.onFinish(response);
                        }
                    } else {
                        Log.e("HttpUtil", "请求失败");
                    }
                } catch (MalformedURLException e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (httpURLConnection != null) {
                        // 最后记得关闭连接
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}

