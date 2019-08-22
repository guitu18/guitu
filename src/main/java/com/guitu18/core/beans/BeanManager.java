package com.guitu18.core.beans;

import com.guitu18.core.annonation.Autowired;
import com.guitu18.core.interceptor.HandlerInterceptor;
import com.guitu18.core.reflect.ClassScanner;
import com.guitu18.core.reflect.ConfigurationParser;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanManager
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class BeanManager {

    private Logger log = Logger.getLogger(this.getClass());

    private static BeanManager beanManager;

    private static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    private BeanManager() {
    }

    /**
     * 获取BeanManager单例实例
     *
     * @return
     */
    public static BeanManager getInstance() {
        if (beanManager == null) {
            synchronized (BeanManager.class) {
                if (beanManager == null) {
                    beanManager = new BeanManager();
                }
            }
        }
        return beanManager;
    }

    /**
     * 初始化IOC容器
     */
    public void init() {
        log.info(">>>>>>>>>>>>> BeanManager.init()... >>>>>>>>>>>>>");
        for (Class clazz : ClassScanner.getClasses()) {
            try {
                // 扫描HandlerMapping映射
                ClassScanner.scanHandlerMapping(clazz);
                // 初始化并保存实例映射
                beanMap.put(clazz.getName(), clazz.newInstance());
            } catch (Exception ignored) {
            }
        }
        try {
            executeAutowired();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行自动注入
     *
     * @throws Exception
     */
    private void executeAutowired() throws Exception {
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            // 扫描配置类
            Class<?> clazz = entry.getValue().getClass();
            ConfigurationParser.getInstance().scanConfiguration(clazz);
            // 扫描Autowired注解
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Autowired.class) != null) {
                    Object obj = beanMap.get(field.getType().getName());
                    if (obj != null) {
                        field.setAccessible(true);
                        field.set(entry.getValue(), obj);
                    }
                }
            }
        }
    }

    /**
     * 根据名称获取Bean
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception {
        return beanMap.get(name);
    }

    /**
     * 根据类型获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getBean(Class<T> clazz) throws Exception {
        return (T) getBean(clazz.getName());
    }


    /**
     * 获取拦截器
     *
     * @return
     */
    public List<HandlerInterceptor> getHandlerInterceptor() {
        log.info(">>>>>>>>>>>>> BeanManager.getHandlerInterceptor()... >>>>>>>>>>>>>");
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            if (entry.getValue() instanceof HandlerInterceptor) {
                interceptors.add((HandlerInterceptor) entry.getValue());
            }
        }
        return interceptors;
    }

}
