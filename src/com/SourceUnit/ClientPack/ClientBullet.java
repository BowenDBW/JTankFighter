package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;

import java.awt.*;

/**
 *  record类 是java14之后的产物，本项目理想状态是使用java8，故可以将其看作记录类，但不这么优化
 */
public class ClientBullet implements ClientGameComponent {

    private final int xPos;
    private final int yPos;
    private final int direction;

    public ClientBullet(int xPos, int yPos, int direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
    }

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

    @Override
    public int getX() {
        return xPos;
    }
    
    @Override
    public int getY() {
        return yPos;
    }

    public String getType() {
        return "bullet";
    }
}
