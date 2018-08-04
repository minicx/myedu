package com.xcao.myedu.chapter10;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:13 2018/8/4
 * @Modified By;
 */
public abstract class AbstractTemplate {
    protected abstract void doSomething();

    protected abstract void doAnything();

    public void template(){
        this.doSomething();
        this.doAnything();
    }
}