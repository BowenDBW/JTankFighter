package com.SourceUnit.ServerPack;

import com.ProcessUnit.ServerPack.ServerModel;

import java.awt.*;

/**
 * 河道类
 */
public class ServerRiver implements ServerGameComponent {
    private final Image river;
    private final int xPos;
    private final int yPos;
    private final Rectangle Border;

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    /**
     * 初始化属性
     * @param a x pos
     * @param b y pos
     */
    public ServerRiver(int a, int b) {
        river = ServerModel.textures[71];
        xPos = a;
        yPos = b;
        Border = new Rectangle(xPos - 12, yPos - 12, 25, 25);
    }

    /**
     * 获取边界
     * @return border
     */
    @Override
    public Rectangle getBorder() {
        return Border;
    }

    /**
     * 获取类型
     * @return river
     */
    @Override
    public String getType() {
        return "river";
    }

    /**
     * 绘制
     * @param g draw
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(river, xPos - 12, yPos - 12, null);
    }

    /**
     * 未使用方法
     */
    @Override
    public void move() {
    }

    /**
     * 获取细致边界
     * @return null
     */
    @Override
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    /**
     * 击毁墙处理
     * @return false
     */
    @Override
    public boolean wallDestroyed() {
        return false;
    }

}

