package com.client.ClientUnit;

import com.client.ConponentPack.Actor;

import java.awt.*;

public class Shield implements Actor {
    private final int xPos;
    private final int yPos;
    private final ClientModel gameModel;

    public Shield(int xPos, int yPos, ClientModel gameModel) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.gameModel = gameModel;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(xPos - 12, yPos - 12, 25, 25);
        g.drawRect(xPos - 11, yPos - 11, 23, 23);
        gameModel.removeActor(this);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public String getType() {
        return "shield";
    }
}