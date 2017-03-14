package com.evistek.mediaserver.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/4.
 */
public class HttpErrorMessage {
    public static String build(HttpServletResponse resp, int code, String message) {
        resp.setStatus(code);
        Map<String, Object> map = new HashMap<>();
        map.put("error", message);
        return JSON.toJSONString(map);
    }
}
