package com.gak.hospital.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class ResultUtils {

    public static JSONObject pageToJson(Page<?> page) {
        JSONObject object = getJson();
        object.put("totalElements", page.getTotalElements());
        object.put("data", page.getContent());
        return object;
    }

    /**
     * 实体转json分页数据
     */
    public static <T> JSONObject entityToPageJson(T t) {
        JSONObject object = getJson();
        List<T> list = new ArrayList<>();
        list.add(t);
        object.put("totalElements", 1);
        object.put("data", list);
        return object;
    }

    private static JSONObject getJson() {
        return new JSONObject();
    }

    private ResultUtils() {}
}
