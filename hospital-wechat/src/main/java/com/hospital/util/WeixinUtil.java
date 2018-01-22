package com.hospital.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hospital.wechat.service.AccessTokenMgr;
import com.hospital.wechat.service.MyX509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

/**
 * Created by QQQ on 2017/12/10.
 */
public class WeixinUtil {

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET/POST）
     * @param param         提交的数据
     * @return 回复的结果的JSON对象
     */
    public static JSONObject HttpsRequest(String requestUrl, String requestMethod,
                                          String param) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {

            //创建SSLContext对象
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            //从上述SSLContext对象中取得SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            //设置请求方式(GET/POST)
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            //当有数据需要提交时
            if (param != null) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                //设置编码
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            //释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSON.parseObject(buffer.toString());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
    }


    /**
     * 发起http请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET/POST）
     * @param param         提交的数据
     * @return 回复的结果的JSON对象
     */
    public static String HttpRequestString(String requestUrl, String requestMethod,
                                           String param) {
        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod("POST");

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            //当有数据需要提交时
            if (param != null) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                //设置编码
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            //释放资源
            inputStream.close();
            inputStream = null;

            httpUrlConn.disconnect();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            //return jsonObject;
            return "";
        }
    }

    /**
     * 发起http请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET/POST）
     * @param param         提交的数据
     * @return 回复的结果的JSON对象
     */
    public static JSONObject HttpRequest(String requestUrl, String requestMethod,
                                         String param) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod("POST");

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            //当有数据需要提交时
            if (param != null) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                //设置编码
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            //释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSON.parseObject(buffer.toString());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
    }

    private static final String uploadUrl = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    public static JSONObject UploadFile(String type, File file, AccessTokenMgr mgr) {
        JSONObject jsonObject = null;
        String url = uploadUrl.replace("ACCESS_TOKEN", mgr.getAccessToken()).replace("TYPE", type);
        //定义数据分隔符
        String boundary = "----------sunlight";
        try {
            URL uploadUrl = new URL(url);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");
            //设置请求头Content-type
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data);boundary=" + boundary);
            //获取媒体文件上传的输出流
            OutputStream outputStream = uploadConn.getOutputStream();
            //从请求头中获取内容类型
            String contentType = "Content-Type: " + getContentType();
            //请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes());
            outputStream.write(String.format("Content-Type: %s\r\n\r\n", contentType).getBytes());
            //获取媒体文件的输入流（读取文件）
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024 * 8];
            int size = 0;
            while ((size = in.read(buf)) != -1) {
                // 将媒体文件写到输出流（往微信服务器写数据）
                outputStream.write(buf, 0, size);
            }
            //请求提结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();
            in.close();
            //将返回的输入流转换成字符串
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            //释放资源
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();
            jsonObject = JSON.parseObject(buffer.toString());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取文件的上传类型，图片格式为image/png,image/jpeg等。非图片为application /octet-stream
    private static String getContentType() throws Exception {
        return "application/octet-stream";
    }
}