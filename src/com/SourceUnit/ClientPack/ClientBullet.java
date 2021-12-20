package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;

import java.awt.*;

/**
 * 子弹类
 * @author chenhong
 */
public class ClientBullet implements ClientGameComponent {

    private final int xPos;
    private final int yPos;
    private final int direction;

    /**
     * Instantiates a new Client bullet.
     *  子弹类的构造函数
     * @param xPos      the x pos 坐标
     * @param yPos      the y pos 坐标
     * @param direction the direction 子弹的朝向
     */
    public ClientBullet(int xPos, int yPos, int direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
    }

    /**
     * 绘制子弹
     * @param g the g
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.lightGray);
        if (direction == 0 || direction == 1) {
            g.fillRect(xPos - 1, yPos - 4, 3, 9);
        }
        if (direction == 2 || direction == 3) {
            g.fillRect(xPos - 4, yPos - 1, 9, 3);
        }
        ClientDrawingPanel.removeActor(this);
    }

    /**
     * 获取x坐标
     * @return xpos x坐标
     */
    @Override
    public int getX() {
        return xPos;
    }


    /**
     * 获取y坐标
     * @return ypos y坐标
     */
    @Override
    public int getY() {
        return yPos;
    }

    /**
     * Gets type.
     * 获取物体类型
     * @return the type
     */
    public String getType() {
        return "bullet";
    }
}

