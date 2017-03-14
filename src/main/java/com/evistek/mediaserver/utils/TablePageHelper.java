package com.evistek.mediaserver.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * Bootstrap table need total and rows to do pagination
 *
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/10.
 */
public class TablePageHelper {
    public static String toJSONString(int total, Object data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", data);
        return JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
    }
}
