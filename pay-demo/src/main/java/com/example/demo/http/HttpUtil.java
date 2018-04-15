package com.example.demo.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

    /**
     * @param URL:请求地址
     * @param params：请求参数
     * @param pathFlat：1为http请求，2为https请求
     * @return 返回json格式字符串
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String post(String URL, Map<String, String> params, int pathFlat)
            throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
        StringBuffer result = new StringBuffer();
        InputStream in = null;
        BufferedReader bufferedReader = null;
        HttpClient httpClient = null;
        org.apache.http.client.methods.HttpPost httpPost = null;
        try {
            /**
             * 读取param中的数据，写入NameValuePair
             */
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null) {
                Iterator<Entry<String, String>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = (Entry<String, String>) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    nvps.add(new BasicNameValuePair(key, value));
                }
            }

            /**
             */
            if (pathFlat == 2) {
                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }
                };
                ctx.init(null, new TrustManager[]{tm}, null);
                SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("https", 443, ssf));
                ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
                httpClient = new DefaultHttpClient(mgr);
            } else if (pathFlat == 1) {
                httpClient = new DefaultHttpClient();
            }

            // HttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // 璁惧畾12绉掕秴鏃讹紝灞婃椂浼氬脊鍑篍xception
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            httpPost = new org.apache.http.client.methods.HttpPost(URL);
//            httpPost.addHeader("Access-Control-Allow-Origin", "*");
//            httpPost.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//            httpPost.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse response = httpClient.execute(httpPost);
            URL = URLDecoder.decode(URL, "UTF-8");
            /**
             * 获取服务器响应
             */
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (in != null) {
                in.close();
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return result.toString();
    }

    public static HttpClientContext httpClientContext = null;

    static {
        httpClientContext = HttpClientContext.create();  //创建上下文.用于共享sessionid
    }

    /**
     * @param map 需要提交的数据.
     * @param url 将要提交的地址.
     */
    public static String post(Map<String, String> map, String url) {
        StringBuffer result = new StringBuffer();
        InputStream in = null;
        BufferedReader bufferedReader = null;
        HttpClient client = (HttpClient) HttpClients.createDefault(); //获取链接对象.
        HttpPost post = new HttpPost(url); //创建表单.
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

        //遍历map 将其中的数据转化为表单数据
        for (Map.Entry<String, String> entry :
                map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            //对表单数据进行url编码
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            post.setEntity(urlEncodedFormEntity);
            //post.addHeader("Cookie");

            HttpResponse response = client.execute(post, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            System.out.println("gyqtest---------cookies.size" + cookies.size());
            for (Cookie cookie :
                    cookies) {
                System.out.println("gyqtest---------name=" + cookie.getName() + "====value=" + cookie.getValue());
            }

            /**
             * 获取服务器响应
             */
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
            bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String str = "{X1_Amount=0.01, X2_BillNo=50ace4aee7924bdf8a7767a3e88d458e, X3_MerNo=1256060676, X4_ReturnURL=http://localhost:8004/returnUrl," +
            " X5_NotifyURL=http://localhost:8004/notify, X6_MD5info=CFB7D17669C76C4C89E97118FDA92D74, X7_PaymentType=ICBC, X8_MerRemark=1, X9_ClientIp=, isApp=}";

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {

        //核心应用类
        HttpClient httpClient = new DefaultHttpClient();

        //设定表单需要提交的参数
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();

        //示例：提交用户名和密码
        qparams.add(new BasicNameValuePair("X1_Amount", "0.01"));
        qparams.add(new BasicNameValuePair("X2_BillNo", "50ace4aee7924bdf8a7767a3e88d458e"));
        qparams.add(new BasicNameValuePair("X3_MerNo", "1256060676"));
        qparams.add(new BasicNameValuePair("X4_ReturnURL", "http://localhost:8004/returnUrl"));
        qparams.add(new BasicNameValuePair("X5_NotifyURL", "http://localhost:8004/notify"));
        qparams.add(new BasicNameValuePair("X6_MD5info", "CFB7D17669C76C4C89E97118FDA92D74"));
        qparams.add(new BasicNameValuePair("X7_PaymentType", "ICBC"));
        qparams.add(new BasicNameValuePair("X8_MerRemark", "1"));
        qparams.add(new BasicNameValuePair("X9_ClientIp", ""));
        qparams.add(new BasicNameValuePair("isApp", ""));

        //设定需要访问的URL，第四个参数为表单提交路径
        URI uri = URIUtils.createURI("http", "bq.baiqianpay.com/webezf/web/?app_act=openapi/bq_pay/pay", -1, "/",
                //将参数加入URL中
                URLEncodedUtils.format(qparams, "UTF-8"), null);
        //Post提交
        HttpPost httpPost = new HttpPost(uri);

        //System.out.println(httpPost.getURI());

        //httpClient执行，返回response
        HttpResponse response = httpClient.execute(httpPost);

        //获取实体
        HttpEntity httpEntity= response.getEntity();

        //打印StatusLine
        System.out.println("StatusLine: " + response.getStatusLine());

        //读取内容
        String content = EntityUtils.toString(httpEntity, "UTF-8");
        //打印输出结果内容
        System.out.println(content);

    }
}
