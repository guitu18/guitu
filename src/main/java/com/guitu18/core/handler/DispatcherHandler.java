package com.guitu18.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.guitu18.common.exception.MyException;
import com.guitu18.common.utils.CommonUtils;
import com.guitu18.common.utils.JsonResult;
import com.guitu18.core.beans.BeanManager;
import com.guitu18.core.http.Request;
import com.guitu18.core.interceptor.InterceptProcess;
import com.guitu18.core.mapping.HandlerMethodMapping;
import com.guitu18.core.thread.ThreadLocalHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * 前端控制器DispatcherHandler
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) throws Exception {
        log.info(fullHttpRequest.method().name() + " " + fullHttpRequest.uri() + " " + fullHttpRequest.protocolVersion().toString());
        // 包装Request
        Request request = new Request(fullHttpRequest);
        // 执行前置拦截器
        if (InterceptProcess.getInstance().processBefore()) {
            // 获取Handler
            Method method = getHandler();
            // 获取实例
            Object instance = BeanManager.getInstance().getBean(method.getDeclaringClass());
            // 从请求参数中获取Method需要的参数
            Object[] args = getMethodArgs(method);
            Object invoke;
            if (args != null) {
                invoke = method.invoke(instance, args);
            } else {
                invoke = method.invoke(instance);
            }
            // 执行后置拦截器
            InterceptProcess.getInstance().processAfter();
            response(context, HttpResponseStatus.OK, JSONObject.toJSONString(invoke));
        }
        response(context, HttpResponseStatus.INTERNAL_SERVER_ERROR, JsonResult.error("INTERNAL_SERVER_ERROR"));
    }

    /**
     * 根据请求路径获取处理器
     *
     * @return
     * @throws MyException
     */
    private Method getHandler() throws MyException {
        // 获取请求路径
        String mapping = ThreadLocalHolder.get().getUri();
        if (mapping.contains("?")) {
            mapping = mapping.substring(0, mapping.indexOf("?"));
        }
        return HandlerMethodMapping.getInstance().getMethod(mapping);
    }

    /**
     * 从请求参数中获取Method需要的参数
     *
     * @param method
     * @throws Exception
     */
    private Object[] getMethodArgs(Method method) throws Exception {
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameters.length == 0) {
            return null;
        }
        // 获取请求参数并注入到方法形参
        Object[] args = new Object[parameters.length];
        Request request = ThreadLocalHolder.get();
        for (int i = 0; i < parameters.length; i++) {
            List<String> list = request.getParameters().get(parameters[i].getName());
            Class<?> type = parameterTypes[i];
            // 暂仅支持Java基础类参数注入
            if (CommonUtils.isJavaClass(type)) {
                args[i] = list == null ? null : CommonUtils.getValueByType(type.toString(), list.get(0));
            }
        }
        return args;
    }


    /**
     * 响应请求
     *
     * @param context
     * @param status
     * @param result
     */
    private void response(ChannelHandlerContext context, HttpResponseStatus status, JsonResult result) {
        response(context, status, JSONObject.toJSONString(result));
    }

    /**
     * 响应请求
     *
     * @param context
     * @param status
     * @param result
     */
    private void response(ChannelHandlerContext context, HttpResponseStatus status, String result) {
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        // 将响应写到客户端
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof MyException) {
            log.error(((MyException) cause).getMsg());
            JsonResult error = JsonResult.error(404, ((MyException) cause).getMsg());
            response(ctx, HttpResponseStatus.NOT_FOUND, JSONObject.toJSONString(error));
            return;
        }
        cause.printStackTrace();
        response(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, JSONObject.toJSONString(JsonResult.error()));
    }

}
