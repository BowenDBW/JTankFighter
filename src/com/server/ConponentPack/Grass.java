package com.server.ConponentPack;

import java.awt.*;

/**
 * @author chenhong
 */
public class Grass implements Actor {
    private Rectangle border;
    private final int xPos;
    private final int yPos;

    public void setBorder(Rectangle border) {
        this.border = border;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public Grass(int a, int b) {
        xPos = a;
        yPos = b;
        border = new Rectangle(0, 0, 0, 0);
    }

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

    @Override
    public String getType() {
        return "grass";
    }

    @Override
    public Rectangle getBorder() {
        return border;
    }

    @Override
    public void move() {
    }

    @Override
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    @Override
    public boolean wallDestroyed() {
        return false;
    }


}