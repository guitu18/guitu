package com.guitu18.common.config;

import com.guitu18.core.annonation.Configuration;
import lombok.Data;

/**
 * Web相关配置
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@Data
@Configuration("web")
public class WebConfig {

    /**
     * 服务端口号
     */
    private int serverPort = 8888;

}
