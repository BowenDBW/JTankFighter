package com.server.ComponentPack;

import com.server.ServerUnit.ServerModel;

import java.awt.*;

/**
 * @author chenhong
 */
public class River implements GameComponent {
    private final Image river;
    private ServerModel gameModel;
    private final int xPos;
    private final int yPos;
    private final Rectangle Border;

    public ServerModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(ServerModel gameModel) {
        this.gameModel = gameModel;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public River(int a, int b, ServerModel gameModel) {
        this.gameModel = gameModel;
        river = ServerModel.textures[71];
        xPos = a;
        yPos = b;
        Border = new Rectangle(xPos - 12, yPos - 12, 25, 25);
    }

    @Override
    public Rectangle getBorder() {
        return Border;
    }

    @Override
    public String getType() {
        return "river";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(river, xPos - 12, yPos - 12, null);
    }

    //未使用的方法
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

