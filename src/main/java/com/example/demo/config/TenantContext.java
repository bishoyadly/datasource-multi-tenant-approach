package com.example.demo.config;


import lombok.Getter;
import lombok.Setter;


public class TenantContext {
    private static ThreadLocal<String> tenantId = new ThreadLocal<>();

    public static String getTenantId() {
        return tenantId.get();
    }

    public static void setTenantId(String id) {
        tenantId.set(id);
    }
}
