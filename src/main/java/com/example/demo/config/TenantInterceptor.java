package com.example.demo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private String getTenantIdFromRequestUri(HttpServletRequest request) {
        Object object = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        HashMap<String, String> pathVariables = (HashMap) object;
        String tenantId = null;
        if (Objects.nonNull(pathVariables)) {
            tenantId = pathVariables.get("tenantId");
        }
        return tenantId;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = getTenantIdFromRequestUri(request);
        TenantContext.setTenantId(tenantId);
        return true;
    }
}
