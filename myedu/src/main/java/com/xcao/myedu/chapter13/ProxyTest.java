package com.xcao.myedu.chapter13;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:57 2018/8/4
 * @Modified By;
 */
public class ProxyTest {
    public static void main(String[] args) {
        IPlayer player = new Player();
        IPlayer proxy = new GamePlayerProxy(player);
        proxy.killBoss();
    }
}