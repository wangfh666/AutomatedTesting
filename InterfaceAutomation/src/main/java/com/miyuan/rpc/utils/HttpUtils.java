package com.miyuan.rpc.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.pool.sizeof.SizeOf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * @author hust
 * @ClassName: HttpUtils
 * @Description:
 * @date 2016年11月17日 上午10:08
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 发送json
     * @param sendUrl
     * @param data
     * @return
     */
    public static String sendJson(String sendUrl, String data) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sendUrl);
//        StringEntity myEntity = new StringEntity(data, ContentType.APPLICATION_JSON);// 构造请求数据
        StringEntity myEntity = new StringEntity(data, "UTF-8");// 构造请求数据
        myEntity.setContentEncoding("UTF-8");
        myEntity.setContentType("application/json");

        post.setEntity(myEntity);// 设置请求体
        String responseContent = null; // 响应内容
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            	System.out.println("响应内容：" + responseContent);
            	
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(response, client);
        }
        return responseContent;
    }

    /**
     * post 发送key-value
     * @param sendUrl
     * @param keyVal
     * @throws IOException
     */
    public static void sendNameValuePair(String sendUrl, Map<String,String> keyVal) throws IOException {
        List<NameValuePair> formparams = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = keyVal.entrySet();
        for (Map.Entry<String, String> data : entrySet) {
            formparams.add(new BasicNameValuePair(data.getKey(), data.getValue()));
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sendUrl);
        // 连接配置。
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(50000)
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000)
                .build();

        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, PassportUtils.defaultCharset);
        post.setEntity(reqEntity);
        post.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                String message = EntityUtils.toString(resEntity, "utf-8");
                logger.info(message);
            } else {
                logger.error("[{}] 请求失败!", sendUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(response, client);
        }
    }

    /**
     * 关闭
     * @param response
     * @param client
     */
    public static void close(CloseableHttpResponse response, CloseableHttpClient client) {
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
