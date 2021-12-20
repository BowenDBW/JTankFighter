package com.client.ConponentPack;

import com.client.ClientUnit.DrawingPanel;

import java.awt.*;

/**
 * @author chenhong
 */
public class Bomb implements GameComponent {

    private final int xPos;
    private final int yPos;
    private int inner, middle, outer;

    public Bomb(int a, int b, int size) {
        xPos = a;
        yPos = b;

        if (size == 0) {
            inner = 6;
            middle = 9;
            outer = 14;
        } else if (size == 1) {
            inner = 2;
            middle = 4;
            outer = 7;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(xPos - outer, yPos - outer,
                2 * outer, 2 * outer);
        g.setColor(Color.orange);
        g.fillOval(xPos - middle, yPos - middle,
                2 * middle, 2 * middle);
        g.setColor(Color.yellow);
        g.fillOval(xPos - inner, yPos - inner,
                2 * inner, 2 * inner);

        DrawingPanel.removeActor(this);
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }
}