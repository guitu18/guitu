package com.guitu18.core.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guitu18.common.utils.Constants;
import com.guitu18.core.thread.ThreadLocalHolder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Request，对FullHttpRequest进行包装
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
@Data
public class Request {

    private String httpVersion;

    private String uri;

    private String method;

    private Map<String, String> headers;

    private Map<String, Cookie> cookies;

    private Map<String, List<String>> parameters;

    /**
     * 包装Request信息
     *
     * @param fullHttpRequest
     */
    public Request(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        this.httpVersion = fullHttpRequest.protocolVersion().toString();
        this.uri = fullHttpRequest.uri();
        this.method = fullHttpRequest.method().name();
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.parameters = new HashMap<>();
        // 解析请求头
        parseHeaders(fullHttpRequest);
        // 解析请求参数
        parseParameter(fullHttpRequest);
        // 添加为本地线程变量
        ThreadLocalHolder.set(this);
    }


    /**
     * 解析Header
     *
     * @param request
     */
    private void parseHeaders(FullHttpRequest request) {
        for (Map.Entry<String, String> header : request.headers()) {
            String key = header.getKey();
            String value = header.getValue();
            headers.put(key, value);
            // 解析Cookie
            if (Constants.ContentType.COOKIE.equals(key)) {
                Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(value);
                for (Cookie cookie : cookies) {
                    this.cookies.put(cookie.name(), cookie);
                }
            }
        }
    }

    /**
     * 解析请求参数
     *
     * @param fullHttpRequest
     * @return
     * @throws UnsupportedEncodingException
     */
    private void parseParameter(FullHttpRequest fullHttpRequest) throws UnsupportedEncodingException {
        // GET请求
        if (Constants.RequestMethod.GET.equalsIgnoreCase(this.method)) {
            // 获取URL中的参数
            String decode = URLDecoder.decode(this.uri, "utf-8");
            QueryStringDecoder query = new QueryStringDecoder(decode);
            this.parameters = query.parameters();
        }
        // POST请求
        else if (Constants.RequestMethod.POST.equalsIgnoreCase(this.method)) {
            String contentType = this.headers.get(Constants.ContentType.CONTENT_TYPE);
            // 解析from表单数据 Content-Type = x-www-form-urlencoded
            if (Constants.ContentType.FORM_URLENCODED.equalsIgnoreCase(contentType)) {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
                List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
                for (InterfaceHttpData data : postData) {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        MemoryAttribute attribute = (MemoryAttribute) data;
                        this.parameters.put(attribute.getName(), Collections.singletonList(attribute.getValue()));
                    }
                }
            }
            // 解析json数据 Content-Type = application/json
            else if (Constants.ContentType.APPLICATION_JSON.equalsIgnoreCase(contentType)) {
                ByteBuf content = fullHttpRequest.content();
                byte[] reqContent = new byte[content.readableBytes()];
                content.readBytes(reqContent);
                JSONObject json = JSONObject.parseObject(new String(reqContent, "UTF-8"));
                for (String key : json.keySet()) {
                    // 根据不同类型做不同处理，JSONObject直接做字符串处理
                    if (json.get(key) instanceof JSONObject) {
                        this.parameters.put(key, Collections.singletonList(json.get(key).toString()));
                    }
                    // JSONArray转为List<String>
                    else if (json.get(key) instanceof JSONArray) {
                        this.parameters.put(key, json.getJSONArray(key).toJavaList(String.class));
                    }
                    // 转为String
                    else {
                        this.parameters.put(key, Collections.singletonList(json.get(key).toString()));
                    }
                }
            }
        }
    }

}
