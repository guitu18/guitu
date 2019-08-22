package com.guitu18.common.utils;

import com.guitu18.common.exception.MyException;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * CommonUtils
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
public class CommonUtils {

    /**
     * 是否是集合实现类
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是Java基础类，代码参考EasyPOI
     *
     * @param clazz
     * @return
     */
    public static boolean isJavaClass(Class<?> clazz) {
        boolean isBaseClass = false;
        if (clazz.isArray()) {
            isBaseClass = false;
        } else if (clazz.isPrimitive() || clazz.getPackage() == null
                || "java.lang".equals(clazz.getPackage().getName())
                || "java.math".equals(clazz.getPackage().getName())
                || "java.sql".equals(clazz.getPackage().getName())
                || "java.util".equals(clazz.getPackage().getName())) {
            isBaseClass = true;
        }
        return isBaseClass;
    }

    /**
     * 根据类型获取值，代码参考EasyPOI
     *
     * @param classFullName
     * @param obj
     * @return
     */
    public static Object getValueByType(String classFullName, Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            if ("class java.util.Date".equals(classFullName)) {
                return obj;
            }
            if ("class java.lang.Boolean".equals(classFullName) || "boolean".equals(classFullName)) {
                return Boolean.valueOf(String.valueOf(obj));
            }
            if ("class java.lang.Double".equals(classFullName) || "double".equals(classFullName)) {
                return Double.valueOf(String.valueOf(obj));
            }
            if ("class java.lang.Long".equals(classFullName) || "long".equals(classFullName)) {
                try {
                    return Long.valueOf(String.valueOf(obj));
                } catch (Exception e) {
                    //格式错误的时候,就用double,然后获取Int值
                    return Double.valueOf(String.valueOf(obj)).longValue();
                }
            }
            if ("class java.lang.Float".equals(classFullName) || "float".equals(classFullName)) {
                return Float.valueOf(String.valueOf(obj));
            }
            if ("class java.lang.Integer".equals(classFullName) || "int".equals(classFullName)) {
                try {
                    return Integer.valueOf(String.valueOf(obj));
                } catch (Exception e) {
                    //格式错误的时候,就用double,然后获取Int值
                    return Double.valueOf(String.valueOf(obj)).intValue();
                }
            }
            if ("class java.math.BigDecimal".equals(classFullName)) {
                return new BigDecimal(String.valueOf(obj));
            }
            if ("class java.lang.String".equals(classFullName)) {
                return String.valueOf(obj);
            }
            return obj;
        } catch (Exception e) {
            throw new MyException("参数转换异常");
        }
    }

}
