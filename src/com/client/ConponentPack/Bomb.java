package com.client.ConponentPack;

import com.client.ClientUnit.ClientModel;

import java.awt.*;

public class Bomb implements Actor {

    private final int xPos;
    private final int yPos;
    private final ClientModel gameModel;
    private final int size;
    private int inner, middle, outer;

    public Bomb(int a, int b, int size, ClientModel gameModel) {
        this.size = size;
        this.gameModel = gameModel;
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

        gameModel.removeActor(this);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}