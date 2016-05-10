package com.nazir;

import com.utils.UrlUtil;

public class Main {
    public static void main(String[] args) {
        final ApiTest apiTest = new ApiTest("localhost:8080", null, null, UrlUtil.getInstance().getUrlVersion(), null);
        apiTest.setDefaultCloseOperation(3);
        apiTest.setVisible(true);
    }
}
