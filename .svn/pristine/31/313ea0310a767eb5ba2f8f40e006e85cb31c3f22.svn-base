package reflect;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 关于Method.invoke()调用有参方法的传参问题
 *
 * @author zhangkuan
 * @date 2019/8/21
 */
public class MethodInvokeTest {

    public void hello(String hello) {
        System.out.println("MethodInvokeTest.hello");
    }

    public void hello(Integer age) {
        System.out.println("MethodInvokeTest.hello");
    }

    public void score(int[] score) {
        System.out.println("MethodInvokeTest.score");
    }

    public void score(Integer[] score) {
        System.out.println("MethodInvokeTest.score");
    }

    public void like(String code, String read) {
        System.out.println("MethodInvokeTest.like");
    }

    public void like(String[] like) {
        System.out.println("MethodInvokeTest.like");
    }

    public void math(Integer score, Integer number) {
        System.out.println("MethodInvokeTest.score");
    }

    public void math(Integer score, Integer[] number) {
        System.out.println("MethodInvokeTest.score");
    }

    @Test
    public void test1() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("hello", String.class);
        String[] args = {"hello world"};
        method.invoke(clazz.newInstance(), "hello world");
        method.invoke(clazz.newInstance(), (Object) args);
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test2() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("hello", Integer.class);
        Integer[] args = {26};
        method.invoke(clazz.newInstance(), 26);
        method.invoke(clazz.newInstance(), (Object) args);
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test3() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("score", int[].class);
        int[] args = {1, 2, 3};
        method.invoke(clazz.newInstance(), args);
        method.invoke(clazz.newInstance(), (Object) args);
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test4() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("score", Integer[].class);
        Integer[] args = {1, 2, 3};
        /**
         * int[]可以不用进行Object强转，Integer[]必须强转
         * 否则报参数类型转换异常，String数组也一样需要转换
         */
        method.invoke(clazz.newInstance(), (Object) args);
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test5() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("like", String[].class);
        String[] args = {"Java", "science"};
        /**
         * String[]必须强转，用来告诉JVM这是一个数组参数，而不是多个单独的参数
         */
        method.invoke(clazz.newInstance(), (Object) args);
        /**
         * 或者使用下面的方式告诉JVM这个String[]是一个单独的参数
         */
        method.invoke(clazz.newInstance(), new Object[]{new String[]{"Java", "science"}});
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test6() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("like", String.class, String.class);
        Object[] args = {"Java", "science"};
        method.invoke(clazz.newInstance(), args);
        method.invoke(clazz.newInstance(), "Java", "science");
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test7() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("math", Integer.class, Integer.class);
        Object[] args = {1, 2};
        method.invoke(clazz.newInstance(), args);
        method.invoke(clazz.newInstance(), 1, 2);
        System.out.println("args = " + Arrays.toString(args));
    }

    @Test
    public void test8() throws Exception {
        Class<MethodInvokeTest> clazz = MethodInvokeTest.class;
        Method method = clazz.getMethod("math", Integer.class, Integer[].class);
        Object[] args = {1, new int[]{1, 2, 3}};
        /**
         * 混合参数必须单独传递
         */
        method.invoke(clazz.newInstance(), 1, new Integer[]{1, 2, 3});
        System.out.println("args = " + Arrays.toString(args));
    }

}
