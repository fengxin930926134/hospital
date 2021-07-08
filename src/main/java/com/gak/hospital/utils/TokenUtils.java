package com.gak.hospital.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenUtils {

    // 有效性 单位毫秒 过期时间1小时
    private static final long TOKEN_VALIDITY = 1000L * 60 * 60;

    public static JSONObject createToken(UserDetails userDetails) {
        JSONObject tokenJson = new JSONObject();
        long expires = System.currentTimeMillis() + TOKEN_VALIDITY;
        tokenJson.put("token", computeSignature(userDetails, expires));
        tokenJson.put("expires", expires);
        return tokenJson;
    }

    // 验证token
    public static boolean validateToken(String authToken, UserDetails userDetails) {
        //check token
        String username = getUserNameFromToken(authToken);
        Long expires = getExpiresFromToken(authToken);
        if (username != null && expires != null) {
            return userDetails.getUsername().equals(username) && System.currentTimeMillis() < expires;
        }
        return false;
    }

    // 从token中识别用户
    public static String getUserNameFromToken(String authToken) {
        // ……
        int index = authToken.lastIndexOf("|");
        if (index != -1) {
            return authToken.substring(0, index);
        }
        return null;
    }

    // 从token中识别过期时间
    public static Long getExpiresFromToken(String authToken) {
        // ……
        int index = authToken.lastIndexOf("|");
        if (index != -1) {
            return Long.valueOf(authToken.substring(index + 1));
        }
        return null;
    }

    private static String computeSignature(UserDetails userDetails, long expires) {
        // 一些特有的信息组装 ,并结合某种加密活摘要算法 例如 something+"|"+something2+MD5(s)
        return userDetails.getUsername() + "|" + expires;
    }
}
