package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;

import java.awt.*;

/**
 * The type Client shield.
 * 保护盾
 * @author chenhong
 */
public class ClientShield implements ClientGameComponent {
    private final int xPos;
    private final int yPos;

    /**
     * Instantiates a new Client shield.
     * 构造函数初始化
     * @param xPos the x pos
     * @param yPos the y pos
     */
    public ClientShield(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * 绘制物体
     * @param g the g
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(xPos - 12, yPos - 12, 25, 25);
        g.drawRect(xPos - 11, yPos - 11, 23, 23);
        ClientDrawingPanel.removeActor(this);
    }

    /**
     * 获取坐标
     * @return xPos
     */
    @Override
    public int getX() {
        return xPos;
    }

    /**
     * 获取坐标
     * @return yPos
     */
    @Override
    public int getY() {
        return yPos;
    }

    /**
     * Gets type.
     * 获取类型
     * @return the type
     */
    public String getType() {
        return "shield";
    }
}