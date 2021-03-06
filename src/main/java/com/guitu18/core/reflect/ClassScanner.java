package com.guitu18.core.reflect;

import com.guitu18.common.config.WebConfig;
import com.guitu18.common.exception.MyException;
import com.guitu18.core.annonation.Autowired;
import com.guitu18.core.annonation.RequestMapping;
import com.guitu18.core.mapping.HandlerMethodMapping;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassScanner
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class ClassScanner {

    @Autowired
    private static WebConfig webConfig;

    private static final Logger log = Logger.getLogger(ClassScanner.class);

    private static Set<Class<?>> classes = null;

    /**
     * 获取Class集合
     *
     * @param clazz 启动类Class对象
     * @return Set<Class>
     */
    public static Set<Class<?>> getClasses(Class<?> clazz) {
        if (classes == null) {
            synchronized (ClassScanner.class) {
                if (classes == null) {
                    classes = new HashSet<>();
                    scanner(clazz.getPackage().getName(), classes);
                }
            }
        }
        return classes;
    }

    /**
     * 扫描Class
     */
    public static void scanner(String packageName, Set<Class<?>> classSet) {
        String packageDirName = packageName.replaceAll("\\.", "/");
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                // 判断是否是文件资源
                if ("file".equalsIgnoreCase(url.getProtocol())) {
                    scanClassInPackages(classSet, packageName, url.getFile(), true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 递归扫描Class
     */
    private static void scanClassInPackages(Set<Class<?>> classSet, String packageName, String packageDirName, boolean recursive) {
        File dir = new File(packageDirName);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) {
                throw new MyException("Class file not found.");
            }
            for (File file : files) {
                if (file.isFile()) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classSet.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                    } catch (ClassNotFoundException e) {
                        log.error("ClassNotFoundException", e);
                    }
                }
                // 递归扫描
                else if (file.isDirectory() && recursive) {
                    scanClassInPackages(classSet, packageName + "." + file.getName(), file.getPath(), recursive);
                }
            }
        }
    }

    /**
     * 扫描RequestMapping
     *
     * @param clazz 扫描的类的Class对象
     */
    public static void scanHandlerMapping(Class<?> clazz) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null) {
                    String mapping1 = HandlerMethodMapping.mappingTrim(annotation.value());
                    String mapping2 = HandlerMethodMapping.mappingTrim(methodAnnotation.value());
                    String mapping = (mapping1 + mapping2).equals("") ? "/" : mapping1 + mapping2;
                    HandlerMethodMapping.getInstance().addMapping(mapping, method);
                }
            }
        }
    }


}
