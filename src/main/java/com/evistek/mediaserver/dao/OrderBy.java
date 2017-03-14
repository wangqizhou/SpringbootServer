package com.evistek.mediaserver.dao;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/30.
 */
public class OrderBy {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private String key;
    private String order;

    public OrderBy(String key) {
        this.key = key;
        this.order = ASC;
    }

    public OrderBy(String key, String order) {
        this.key = key;
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
