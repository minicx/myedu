package com.xcao.myedu.chapter7;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 16:33 2018/8/4
 * @Modified By;
 */
public class Single {

    private static Single single = new Single();

    private Single() {

    }

    public static Single getSingle() {
        return single;
    }
}