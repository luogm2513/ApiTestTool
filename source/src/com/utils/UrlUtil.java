package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UrlUtil {

    final static String URL_FILE_ROOT = "/config/urls.txt";

    private static Map<String, UrlDTO> urlMap = new LinkedHashMap<String, UrlDTO>();

    private String urlVersion = "";

    private static UrlUtil instance = new UrlUtil();

    private UrlUtil() {
        loadurl();
    }

    public static UrlUtil getInstance() {
        return instance;
    }

    public String getUrlVersion() {
        return urlVersion;
    }

    public UrlDTO getUrlDTO(String url) {
        return urlMap.get(url);
    }

    public List<String> listUrl() {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, UrlDTO> entry : urlMap.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    // 获取一级分组名称列表
    private void loadurl() {
        try {
            JSONObject jsonObject = new JSONObject(getJsonFile());
            urlVersion = JsonUtils.getStrFromJO(jsonObject, "urlVersion", "");
            JSONArray ja = jsonObject.getJSONArray("urls");
            if (ja != null && ja.length() > 0) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String url = JsonUtils.getStrFromJO(jo, "url", "");
                    String restMethod = JsonUtils.getStrFromJO(jo, "restMethod", "");
                    String param = JsonUtils.getStrFromJO(jo, "param", "");
                    UrlDTO urlDTO = new UrlDTO();
                    urlDTO.setUrl(url);
                    urlDTO.setRestMethod(restMethod);
                    urlDTO.setParam(param);
                    urlMap.put(url, urlDTO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中输入流中加载url的json串
     * 
     */
    private String getJsonFile() {
        String json = null;
        InputStream in = null;
        BufferedReader br = null;
        try {
            String filePath = System.getProperty("user.dir") + URL_FILE_ROOT;
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                in = new FileInputStream(file);
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
                json = result.toString();
            }
        } catch (Exception e) {
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
        return json;
    }

    /**
     * 将Json字符串写入文件中
     * 
     */
    private void setJsonFile(String json) {
        try {
            String filePath = System.getProperty("user.dir") + URL_FILE_ROOT;
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存参数信息
     * 
     * @param url
     * @param params
     */
    public void saveParams(String url, String params) {
        urlMap.get(url).setParam(JsonUtils.getUglyJson(params));
        saveUrlText();
    }

    private void saveUrlText() {
        StringBuilder sb = new StringBuilder("{\"urlVersion\":" + getUrlVersion() + ",\"urls\":[");
        for (Map.Entry<String, UrlDTO> entry : urlMap.entrySet()) {
            sb.append("{\"url\":\"").append(entry.getKey()).append("\",\"restMethod\":\"")
                    .append(entry.getValue().getRestMethod()).append("\",\"param\":")
                    .append(JsonUtils.tojson(entry.getValue().getParam())).append("},");
        }
        String str = sb.toString();
        str = str.substring(0, str.length() - 1);
        setJsonFile(str + "]}");
    }
}
