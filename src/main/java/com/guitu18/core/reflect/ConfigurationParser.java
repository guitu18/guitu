package com.guitu18.core.reflect;

import com.guitu18.common.utils.CommonUtils;
import com.guitu18.core.annonation.Configuration;
import com.guitu18.core.beans.BeanManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置解析
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
public class ConfigurationParser {

    private static Logger log = Logger.getLogger(ConfigurationParser.class);

    private static ConfigurationParser configurationParser = null;

    private static Map<String, Object> properties = new HashMap<>();

    private ConfigurationParser() throws IOException {
        log.info(">>>>>>>>>> 正在加载配置文件 >>>>>>>>>>");
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        for (String name : properties.stringPropertyNames()) {
            ConfigurationParser.properties.put(name, properties.getProperty(name));
        }
    }

    public static ConfigurationParser getInstance() throws IOException {
        if (configurationParser == null) {
            synchronized (ConfigurationParser.class) {
                if (configurationParser == null) {
                    configurationParser = new ConfigurationParser();
                }
            }
        }
        return configurationParser;
    }


    /**
     * 扫描配置类
     *
     * @param clazz
     */
    public void scanConfiguration(Class<?> clazz) throws Exception {
        Configuration annotation = clazz.getAnnotation(Configuration.class);
        if (annotation != null) {
            String prefix = annotation.value() + ".";
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                Object value = CommonUtils.getValueByType(field.getType().toString(),
                        ConfigurationParser.properties.get(prefix + name));
                if (value != null) {
                    field.setAccessible(true);
                    field.set(BeanManager.getInstance().getBean(clazz.getName()), value);
                }
            }
        }
    }

}
