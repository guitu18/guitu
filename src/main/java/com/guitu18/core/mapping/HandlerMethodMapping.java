package com.guitu18.core.mapping;

import com.guitu18.common.exception.MyException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HandlerMethodMapping
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class HandlerMethodMapping {

    private static HandlerMethodMapping handlerMethodMapping = null;

    private Map<String, Method> methodMapping = new ConcurrentHashMap<>();

    private HandlerMethodMapping() {
    }

    /**
     * 获取单例实例
     *
     * @return
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
     * @param mapping
     * @return
     * @throws MyException
     */
    public Method getMethod(String mapping) throws MyException {
        Method method = methodMapping.get(mappingTrim(mapping));
        if (method == null) {
            throw new MyException("MethodMapping not fund: " + mapping);
        }
        return method;
    }

    /**
     * 添加映射
     *
     * @param mapping
     * @param method
     */
    public void addMapping(String mapping, Method method) {
        methodMapping.put(mapping, method);
    }

    /**
     * 过滤mapping
     *
     * @param mapping
     * @return
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
