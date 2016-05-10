package com.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

public class HttpClientUtils {
    public static String sendPost(String url, String jsonContent, String userAgent, String version,
            String authentication) {
        try {
            if (StringUtils.isBlank(authentication)) {
                authentication = "";
            }

            String signed = RSAUtils.encrypt(Md5Utils.getMD5(jsonContent + "_" + authentication), RsaComponent
                    .getInstance().getPublicKey(userAgent));

            HttpClient httpclient = new HttpClient();
            PostMethod post = new PostMethod(url);
            post.setRequestHeader("version", version);
            post.setRequestHeader("authentication", authentication);
            post.setRequestHeader("User-Agent", userAgent);
            post.setRequestHeader("sign", signed);
            RequestEntity entity = new StringRequestEntity(jsonContent, "application/json", "utf-8");
            post.setRequestEntity(entity);
            httpclient.executeMethod(post);

            InputStream in = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String str = "";
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
