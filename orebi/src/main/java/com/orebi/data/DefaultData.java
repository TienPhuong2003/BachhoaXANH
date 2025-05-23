package com.orebi.data;

import java.util.Arrays;
import java.util.List;

public class DefaultData {

    // Dữ liệu mặc định được khai báo là private static
    private static final List<String> DEFAULT_PERMISSIONS = Arrays.asList("VIEW", "CREATE", "UPDATE", "DELETE", "EXECUTE");
    private static final List<String> DEFAULT_RESOURCES = Arrays.asList(
            "CART", "CATEGORY", "DISCOUNTCODE", "DISCOUNTPRODUCT", "LINEITEM", "ORDER",
            "ORDERDETAIL", "ORDERSTATUS", "PAYMENTMETHOD", "PERMISSION", "PERMISSIONRESOURCE",
            "PRODUCT", "PRODUCTDETAIL", "PRODUCTIMAGE", "RESOURCE", "ROLE", "ROLEPERMISSION",
            "SUBCATEGORY", "USER", "USERPERMISSION"
    );
    private static final List<String> ADMIN_PERMISSIONS = Arrays.asList("VIEW", "CREATE", "UPDATE", "DELETE", "EXECUTE");
    private static final List<String> USER_PERMISSIONS = Arrays.asList("VIEW","EXECUTE");

    // Phương thức public chỉ trả về bản sao của dữ liệu
    public static List<String> getDefaultPermissions() {
        return List.copyOf(DEFAULT_PERMISSIONS);
    }

    public static List<String> getDefaultResources() {
        return List.copyOf(DEFAULT_RESOURCES);
    }

    public static List<String> getAdminPermissions() {
        return List.copyOf(ADMIN_PERMISSIONS);
    }

    public static List<String> getUserPermissions() {
        return List.copyOf(USER_PERMISSIONS);
    }
}
