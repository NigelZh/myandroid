package com.tempus.weijiujiao.Utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.tempus.weijiujiao.HTTP.onProgressListner;

import android.util.Log;

/**
 * HTTP (POST/GET ) 操作封装类
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class HttpUtils {
	private static final int TIME_OUT = 1000 * 6;

	/**
	 * HTTP POST
	 * 
	 * @param url
	 *            接口地址
	 * @param postData
	 *            json字符串
	 * @return 结果字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String httpPost(String url, String postData)
			throws ClientProtocolException, IOException {
		Log.d("HttpUtils httpPost url=", url);
		Log.d("HttpUtils httpPost postData=", postData);
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("", URLEncoder.encode(postData,HTTP.UTF_8)));
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIME_OUT);
		HttpResponse httpResponse = client.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result= StringUtils.clearMystring(URLDecoder.decode(EntityUtils.toString(httpResponse.getEntity()), HTTP.UTF_8));
			//String result=URLDecoder.decode(EntityUtils.toString(httpResponse.getEntity()), HTTP.UTF_8);
			Log.d("HttpUtils httpPost getdata=", result);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 上传图片到服务器
	 * 
	 * @param requestUrl
	 *            接口地址
	 * @param bitmapByteArray
	 *            图片的二进制数组
	 * @param postData
	 *            json参数
	 * @return 结果 JSON
	 * @throws IOException
	 *             请求异常
	 */
	// public static String uploadFile(String requestUrl, byte[]
	// bitmapByteArray,
	// String postData) throws IOException {
	// String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	// String PREFIX = "--", LINE_END = "\r\n";
	// String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	// URL url = new URL(requestUrl);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setReadTimeout(10 * 1000);
	// conn.setConnectTimeout(10 * 1000);
	// conn.setDoInput(true);
	// conn.setDoOutput(true);
	// conn.setUseCaches(false);
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("Charset", HTTP.UTF_8);
	// conn.setRequestProperty("connection", "keep-alive");
	// conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
	// + BOUNDARY);
	// if (bitmapByteArray != null && bitmapByteArray.length > 0) {
	// DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	// StringBuffer sb = new StringBuffer();
	// sb.append(PREFIX);
	// sb.append(BOUNDARY);
	// sb.append(LINE_END);
	// sb.append("Content-Disposition: form-data; name=\"image\"; param=\""+
	// postData + "\"" + LINE_END);
	// sb.append("Content-Type: application/octet-stream; charset="+ HTTP.UTF_8
	// + LINE_END);
	// sb.append(LINE_END);
	// dos.write(sb.toString().getBytes());
	// int len = 1024;
	// int off = 0;
	// while (off < bitmapByteArray.length) {
	// if(off+len>bitmapByteArray.length){
	// len=bitmapByteArray.length-off;
	// }
	// dos.write(bitmapByteArray, off, len);
	// off += len;
	// }
	// dos.write(LINE_END.getBytes());
	// dos.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
	// dos.flush();
	// if (conn.getResponseCode() == 200) {
	// InputStream input = conn.getInputStream();
	// StringBuffer sb1 = new StringBuffer();
	// int ss;
	// while ((ss = input.read()) != -1) {
	// sb1.append((char) ss);
	// }
	// return sb1.toString();
	// } else {
	// return null;
	// }
	// }
	// return null;
	// }
	public static String uploadFile(String requestUrl, byte[] bitmapByteArray,
			String postData) throws IOException {
		Log.d("HttpUtils httpPost uploadimage url=", requestUrl);
		Log.d("HttpUtils httpPost uploadimage postData=", postData);
		String start = "-start", end = "end-", selibret = "-mid-";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10 * 1000);
		conn.setConnectTimeout(10 * 1000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Charset", HTTP.UTF_8);
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);
		if (bitmapByteArray != null && bitmapByteArray.length > 0) {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = new StringBuffer();
			sb.append(start);
			sb.append(postData);
			sb.append(selibret);
			dos.write(sb.toString().getBytes());
			int len = 1024;
			int off = 0;
			while (off < bitmapByteArray.length) {
				if (off + len > bitmapByteArray.length) {
					len = bitmapByteArray.length - off;
				}
				dos.write(bitmapByteArray, off, len);
				off += len;
			}
			dos.write(end.getBytes());
			dos.write("\r\n".getBytes());
			dos.flush();
			if (conn.getResponseCode() == 200) {
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				return 
						StringUtils.clearMystring(URLDecoder.decode(sb1.toString(), HTTP.UTF_8));
			} else {
				return null;
			}
		}
		return null;
	}

	public static void downLoadFile(File saveDir, String fileURL,
			onProgressListner listener) {
		String fileName = StringUtils.getFileNameFormUrl(fileURL);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		File savefile = new File(saveDir + "/" + fileName);
		HttpURLConnection conn;
		try {
			URL url = new URL(fileURL);
			if (savefile.exists()) {
				savefile.delete();
			}
			conn = (HttpURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();
			int isLength = conn.getContentLength();
			FileOutputStream fos;
			fos = new FileOutputStream(savefile);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				byte[] buffer = new byte[1024];
				int byteread = 0;
				int readSum = 0;
				float prgress = 0;
				while ((byteread = is.read(buffer)) != -1) {
					fos.write(buffer, 0, byteread);
					readSum += byteread;
					float newProgress = (float) readSum / (float) isLength
							* 100;
					if (newProgress - prgress > 5) {
						prgress = newProgress;
						if (listener != null) {
							listener.onProgress(Math.round(newProgress));
						}
					}
				}

			} else {
				if (listener != null) {
					listener.onFailed("connect to server failed");
				}

			}
			fos.close();
			is.close();
			if (listener != null) {
				listener.onDone(savefile);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (listener != null) {
				listener.onFailed(e.toString());
			}
			e.printStackTrace();
			savefile.delete();
		}
	}
}
