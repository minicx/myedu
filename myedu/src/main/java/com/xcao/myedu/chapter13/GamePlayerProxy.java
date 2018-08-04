package com.xcao.myedu.chapter13;

/**
 * @Author:caoxiang
 * @Description:
 * @Date: Create in 17:55 2018/8/4
 * @Modified By;
 */
public class GamePlayerProxy implements IPlayer {
    private IPlayer player;
    public GamePlayerProxy (IPlayer player) {
        this.player = player;
    }
    @Override
    public void login(String user, String passwd) {
        player.login(user, passwd);
    }

    @Override
    public void killBoss() {
        player.killBoss();
    }

    @Override
    public void upgrade() {
        player.upgrade();
    }
}