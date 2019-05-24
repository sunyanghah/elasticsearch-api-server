package cn.com.chinaunicom.util;

import cn.com.chinaunicom.dto.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by dell on 2018/8/6.
 * @author dell
 */
public class HttpUtil {

    public static List<String> uris;

    public static String username;

    public static String password;

    public static CloseableHttpClient getHttpClient(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();

            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslSocketFactory)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            return httpclient;
        }catch(Exception e){
            return new DefaultHttpClient();
        }
    }

    private static String getEsUrl() throws Exception{
        if (null != uris && uris.size() > 0){
            return uris.get(0)+"/";
        }else{
            throw new Exception("elasticsearch uris 配置错误");
        }
    }

    /**
     * get请求
     * @return
     */
    public static HttpResult doGet(String url) throws Exception{
        HttpGet httpGet = new HttpGet(getEsUrl()+url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        return getResponseToString(httpGet);
    }

    public static HttpResult doPut(String url,String params) throws Exception{
        HttpPut httpPut = new HttpPut(getEsUrl()+url);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        if (StringUtils.isNotBlank(params)) {
            StringEntity reqEntity = new StringEntity(params, charSet);
            httpPut.setEntity(reqEntity);
        }
        return getResponseToString(httpPut);
    }


    private static HttpResult getResponseToString(HttpUriRequest httpRequest) throws Exception{
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = getHttpClient();
        HttpResult httpResult = new HttpResult();
        try {
            response = httpClient.execute(httpRequest);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK || state == HttpStatus.SC_CREATED) {
                httpResult.setSuccess(true);
            }else{
                httpResult.setSuccess(false);
            }
            HttpEntity entity = response.getEntity();
            StringBuilder sb = new StringBuilder();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    sb = sb.append(text);
                }
            }
            EntityUtils.consume(entity);
            httpResult.setResponseString(sb.toString());
            return httpResult;
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * post请求（用于请求json格式的参数）
     * @param url
     * @param params
     * @return
     */
    public static HttpResult doPost(String url, String params) throws Exception {
        HttpPost httpPost = new HttpPost(getEsUrl()+url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        if (StringUtils.isNotBlank(params)) {
            StringEntity reqEntity = new StringEntity(params, charSet);
            httpPost.setEntity(reqEntity);
        }
        return getResponseToString(httpPost);
    }

    public static HttpResult doDelete(String url) throws Exception{
        HttpDelete httpDelete = new HttpDelete(getEsUrl()+url);
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-Type", "application/json");
        return getResponseToString(httpDelete);
    }

}
