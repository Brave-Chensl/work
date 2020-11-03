package com.chensl.reptile.utils;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpUtils {

    //创建连接池管理器
    private static PoolingHttpClientConnectionManager cm;

    public HttpUtils(){
        cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    //配置请求信息
    private static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)      //创建连接的最长时间，单位毫秒
                .setConnectionRequestTimeout(10000)  //设置获取链接的最长时间，单位毫秒
                .setSocketTimeout(10000)     //设置数据传输的最长时间，单位毫秒
                .build();
        return config;
    }


    /**
     * 根据请求地址获取页面数据
     * @param url   请求路径
     * @param parameter   请求参数
     * @param header   请求头
     * @return  //页面数据
     * @throws URISyntaxException
     */
    public static String doGetHtml(String url, Map<String, String> parameter, Map<String, String> header) throws URISyntaxException {
        //创建HTTPClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //设置请求地址
        //创建URLBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        //设置参数
        if (!parameter.isEmpty()) {
            for (String key : parameter.keySet()) {
                uriBuilder.setParameter(key, parameter.get(key));
            }
        }

        //创建HTTPGet对象，设置url访问地址
        //uriBuilder.build()得到请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        //设置请求头信息
        if (!header.isEmpty()) {
            for (String key : header.keySet()) {
                httpGet.addHeader(key, header.get(key));
            }
        }

        //设置请求信息
        httpGet.setConfig(getConfig());

        //System.out.println("发起请求的信息：" + httpGet);

        //使用HTTPClient发起请求，获取response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                //判断响应体Entity是否不为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
