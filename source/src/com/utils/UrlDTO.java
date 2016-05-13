package com.utils;

public class UrlDTO implements Comparable<UrlDTO> {
    private String url;
    private String param;
    private String restMethod;

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

    public String getRestMethod() {
        return restMethod;
    }

    public void setRestMethod(String restMethod) {
        this.restMethod = restMethod;
    }

    @Override
    public int compareTo(UrlDTO o) {
        if (o != null) {
            return this.url.compareTo(o.getUrl());
        }
        return 0;
    }

}
