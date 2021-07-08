package com.gak.hospital.utils;

public class RoleUtils {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String TECHNICAL = "TECHNICAL";
    public static final String PHARMACIST = "PHARMACIST";

    public static final String ROLE_USER = "ROLE_" + USER;
    public static final String ROLE_ADMIN = "ROLE_" + ADMIN;
    public static final String ROLE_DOCTOR = "ROLE_" + DOCTOR;
    public static final String ROLE_TECHNICAL = "ROLE_" + TECHNICAL;
    public static final String ROLE_PHARMACIST = "ROLE_" + PHARMACIST;

    /**
     * 获取权限数组
     * @param roles roles
     * @return String[]
     */
    public static String[] getRoles(String roles) {
        return roles.split(",");
    }

    /**
     * 判断是否存在权限
     * @param role role
     * @param roles roles
     * @return boolean
     */
    public static boolean existRole(String role, String roles) {
        for (String item: getRoles(roles)) {
            if (item.equals(role)) {
                return true;
            }
        }
        return false;
    }

    private RoleUtils() {}
}
