package com.server.ConponentPack;

import com.server.ServerUnit.ServerModel;

import java.awt.*;

public class River implements Actor {
    public Image river;
    public ServerModel gameModel;
    private final int xPos;
    private final int yPos;
    private final Rectangle Border;

    public River(int a, int b, ServerModel gameModel) {
        this.gameModel = gameModel;
        river = gameModel.textures[71];
        xPos = a;
        yPos = b;
        Border = new Rectangle(xPos - 12, yPos - 12, 25, 25);
    }

    public Rectangle getBorder() {
        return Border;
    }

    public String getType() {
        return "river";
    }

    public void draw(Graphics g) {
        g.drawImage(river, xPos - 12, yPos - 12, null);
    }


    //未使用的方法
    public void move() {
    }

    public Rectangle[] getDetailedBorder() {
        return null;
    }

    public boolean wallDestroyed() {
        return false;
    }

}

