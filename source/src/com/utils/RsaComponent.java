package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RsaComponent {
    private final String ANDROID = "android";
    private final String IPHONE = "iphone";

    private static Map<String, PublicKey> publicKeyMap = new HashMap<String, PublicKey>();

    @SuppressWarnings("serial")
    private final Map<String, String> publicKeyFileMap = new HashMap<String, String>() {
        {
            this.put(ANDROID, "classpath*:/com/resources/android_public.key");
            this.put(IPHONE, "classpath*:/com/resources/iphone_public.key");
        }
    };

    private static RsaComponent instance = new RsaComponent();

    private RsaComponent() {
        init();
    }

    public static RsaComponent getInstance() {
        return instance;
    }

    public PublicKey getPublicKey(String keyname) {
        return publicKeyMap.get(keyname);
    }

    private void init() {
        try {
            for (Map.Entry<String, String> entry : publicKeyFileMap.entrySet()) {
                String publicKeyStr = this.getFileKey(entry.getValue());
                if (StringUtils.isNotBlank(publicKeyStr)) {
                    PublicKey publicKey = RSAUtils.getPublicKey(publicKeyStr);
                    synchronized (publicKeyMap) {
                        publicKeyMap.put(entry.getKey(), publicKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中输入流中加载公钥
     * 
     * @param fileRoot 公钥路径
     * @throws Exception 加载公钥时产生的异常
     */
    private String getFileKey(String fileRoot) {
        String privateKey = null;
        InputStream in = null;
        BufferedReader br = null;
        try {
            if (fileRoot.indexOf("classpath*:") != -1) {
                in = this.getClass().getResourceAsStream(fileRoot.replace("classpath*:", ""));
            } else {
                File file = new File(fileRoot);
                in = new FileInputStream(file);
            }
            br = new BufferedReader(new InputStreamReader(in));

            String readLine = null;
            StringBuilder result = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    result.append(readLine);
                    result.append('\r');
                }
            }
            privateKey = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {

            }
        }
        return privateKey;
    }

}
