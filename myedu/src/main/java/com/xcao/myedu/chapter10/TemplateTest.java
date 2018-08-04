package com.xcao.myedu.chapter10;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:16 2018/8/4
 * @Modified By;
 */
public class TemplateTest {
    public static void main(String[] args) {
        AbstractTemplate con1 = new Concrete1();
        AbstractTemplate con2 = new Concrete2();
        con1.template();
        con2.template();
    }
}