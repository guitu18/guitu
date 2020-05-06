package com.guitu18.core.beans;

import com.guitu18.core.annonation.Autowired;
import com.guitu18.core.bootstrap.NettyBootstrap;
import com.guitu18.core.interceptor.HandlerInterceptor;
import com.guitu18.core.reflect.ClassScanner;
import com.guitu18.core.reflect.ConfigurationParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContext，模仿Spring的IOC容器
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuituApplication {

    private final Logger log = Logger.getLogger(this.getClass());

    private static GuituApplication guituApplication;

    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    /**
     * 启动服务
     *
     * @param clazz 启动类Class对象
     * @param args  启动参数
     */
    public static void run(Class<?> clazz, String[] args) {
        GuituApplication.getInstance().init(clazz);
        NettyBootstrap.start(args);
    }

    /**
     * 获取ApplicationContext单例实例
     *
     * @return ApplicationContext
     */
    public static GuituApplication getInstance() {
        if (guituApplication == null) {
            synchronized (GuituApplication.class) {
                if (guituApplication == null) {
                    guituApplication = new GuituApplication();
                }
            }
        }
        return guituApplication;
    }

    /**
     * 初始化IOC容器
     *
     * @param clazz 启动类Class对象
     */
    public void init(Class<?> clazz) {
        log.info(">>>>>>>>>>>>> ApplicationContext.init()... >>>>>>>>>>>>>");
        for (Class<?> sClazz : ClassScanner.getClasses(clazz)) {
            try {
                // 扫描HandlerMapping映射
                ClassScanner.scanHandlerMapping(sClazz);
                // 初始化并保存实例映射
                BEAN_MAP.put(sClazz.getName(), sClazz.newInstance());
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
     */
    private void executeAutowired() throws Exception {
        for (Map.Entry<String, Object> entry : BEAN_MAP.entrySet()) {
            // 扫描配置类
            Class<?> clazz = entry.getValue().getClass();
            ConfigurationParser.getInstance().scanConfiguration(clazz);
            // 扫描Autowired注解
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Autowired.class) != null) {
                    Object obj = BEAN_MAP.get(field.getType().getName());
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
     * @param name Bean名称
     */
    public Object getBean(String name) throws Exception {
        return BEAN_MAP.get(name);
    }

    /**
     * 根据类型获取Bean
     *
     * @param clazz Bean的Class对象
     */
    public <T> T getBean(Class<T> clazz) throws Exception {
        return (T) getBean(clazz.getName());
    }


    /**
     * 获取拦截器，所有实现 {@link HandlerInterceptor} 接口的拦截器
     */
    public List<HandlerInterceptor> getHandlerInterceptor() {
        log.info(">>>>>>>>>>>>> ApplicationContext.getHandlerInterceptor()... >>>>>>>>>>>>>");
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        for (Map.Entry<String, Object> entry : BEAN_MAP.entrySet()) {
            if (entry.getValue() instanceof HandlerInterceptor) {
                interceptors.add((HandlerInterceptor) entry.getValue());
            }
        }
        return interceptors;
    }

}
