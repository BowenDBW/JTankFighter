package com.client.ConponentPack;

import com.client.ClientUnit.ClientModel;
import com.client.ConponentPack.Actor;

import java.awt.*;

public class Shield implements Actor {
    private final int xPos;
    private final int yPos;

    public Shield(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(xPos - 12, yPos - 12, 25, 25);
        g.drawRect(xPos - 11, yPos - 11, 23, 23);
        ClientModel.removeActor(this);
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
        return "shield";
    }
}