package com.nazir;

import com.utils.PropertiesUtil;
import com.utils.UrlUtil;

public class Main {
    public static void main(String[] args) {
        final ApiTest apiTest = new ApiTest(
                PropertiesUtil.getInstance().getConfig("host") + ":" + PropertiesUtil.getInstance().getConfig("port"),
                null, null, UrlUtil.getInstance().getUrlVersion(), null);
        apiTest.setDefaultCloseOperation(3);
        apiTest.setVisible(true);
    }
}
