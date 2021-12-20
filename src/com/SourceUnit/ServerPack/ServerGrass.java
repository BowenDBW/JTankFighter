package com.SourceUnit.ServerPack;

import java.awt.*;

/**
 * 草地类
 */
public class ServerGrass implements ServerGameComponent {
    private final Rectangle border;
    private final int xPos;
    private final int yPos;

    /**
     * 获取x坐标
     * @return x pos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * 获取y坐标
     * @return y pos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * 属性初始化
     * @param a x pos
     * @param b y pos
     */
    public ServerGrass(int a, int b) {
        xPos = a;
        yPos = b;
        border = new Rectangle(0, 0, 0, 0);
    }

    /**
     * 绘制画面
     * @param g the darw
     */
    @Override
    public void draw(Graphics g) {

            g.setColor(new Color(0, 225, 0));
            for (int i = yPos - 11; i <= yPos + 12; i += 5) {
                g.drawLine(xPos - 12, i, xPos + 12, i);
            }
            for (int i = xPos - 11; i <= xPos + 12; i += 5) {
                g.drawLine(i, yPos - 12, i, yPos + 12);
            }
            g.setColor(new Color(0, 128, 0));
            for (int i = yPos - 10; i <= yPos + 12; i += 5) {
                g.drawLine(xPos - 12, i, xPos + 12, i);
            }
            for (int i = xPos - 10; i <= xPos + 12; i += 5) {
                g.drawLine(i, yPos - 12, i, yPos + 12);
            }
    }

    /**
     * 获取类型
     * @return grass
     */
    @Override
    public String getType() {
        return "grass";
    }

    /**
     * 获取边界
     * @return border
     */
    @Override
    public Rectangle getBorder() {
        return border;
    }

    /**
     * 位置处理
     */
    @Override
    public void move() {
    }

    /**
     * 获取边界
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