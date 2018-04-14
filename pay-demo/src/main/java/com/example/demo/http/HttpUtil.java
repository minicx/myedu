package com.example.demo.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	 * 
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
				ctx.init(null, new TrustManager[] { tm }, null);
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
}
