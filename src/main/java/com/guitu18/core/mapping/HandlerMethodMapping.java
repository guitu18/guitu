package com.guitu18.core.mapping;

import com.guitu18.common.exception.MyException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HandlerMethodMapping，模仿Spring中的处理器映射器 HandlerMethodMapping，用法一致
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class HandlerMethodMapping {

    private static HandlerMethodMapping handlerMethodMapping = null;

    private final Map<String, Method> methodMapping = new ConcurrentHashMap<>();

    private HandlerMethodMapping() {
    }

    /**
     * 获取单例实例
     *
     * @return HandlerMethodMapping
     */
    public static HandlerMethodMapping getInstance() {
        if (handlerMethodMapping == null) {
            synchronized (HandlerMethodMapping.class) {
                if (handlerMethodMapping == null) {
                    handlerMethodMapping = new HandlerMethodMapping();
                }
            }
        }
        return handlerMethodMapping;
    }

    /**
     * 获取处理器（Method）
     *
     * @param mapping 路径
     * @return Method
     */
    public Method getMethod(String mapping) throws MyException {
        mapping = mappingTrim(mapping);
        Method method = methodMapping.get(mapping.equals("") ? "/" : mapping);
        if (method == null) {
            throw new MyException("MethodMapping not found: " + mapping);
        }
        return method;
    }

    /**
     * 添加映射
     *
     * @param mapping 路径
     * @param method  方法
     */
    public void addMapping(String mapping, Method method) {
        methodMapping.put(mapping, method);
    }

    /**
     * 过滤mapping
     *
     * @param mapping 路径
     * @return String
     */
    public static String mappingTrim(String mapping) {
        mapping = mapping.trim();
        if ("".equals(mapping) || "/".equals(mapping)) {
            return mapping;
        }
        if (!mapping.startsWith("/")) {
            mapping = "/" + mapping;
        }
        if (mapping.endsWith("/")) {
            mapping = mapping.substring(0, mapping.length() - 1);
        }
        return mapping;
    }

}
