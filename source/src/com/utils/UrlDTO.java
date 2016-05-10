package com.utils;

public class UrlDTO implements Comparable<UrlDTO> {
    private String url;
    private String param;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public int compareTo(UrlDTO o) {
        if (o != null) {
            return this.url.compareTo(o.getUrl());
        }
        return 0;
    }

}
