package com.server.ConponentPack;

import com.server.ServerUnit.ServerModel;

import java.awt.*;

public class SteelWall implements Actor {
    public boolean[] shape = new boolean[4];
    public boolean wallDestroyed;
    public boolean bulletDestroyed;
    public ServerModel gameModel;
    public Image steelWall;
    public Rectangle generalBorder;
    private final int xPos;
    private final int yPos;
    private final Rectangle[] border = new Rectangle[4];

    public SteelWall(int a, int b, ServerModel gameModel) {
        this.gameModel = gameModel;
        steelWall = gameModel.textures[53];
        xPos = a;
        yPos = b;
        generalBorder = new Rectangle(xPos - 12, yPos - 12, 25, 25);
        border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
        border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
        border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
        border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
    }

    public SteelWall(int a, int b, int orientation, ServerModel gameModel) {
        xPos = a;
        yPos = b;
        this.gameModel = gameModel;
        steelWall = gameModel.textures[53];
        generalBorder = new Rectangle(xPos - 12, yPos - 12, 25, 25);
        if (orientation == 0) {
            border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
            border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
            shape[2] = true;
            shape[3] = true;
        }
        if (orientation == 1) {
            border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
            border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
            shape[0] = true;
            shape[1] = true;
        }
        if (orientation == 2) {
            border[0] = new Rectangle(xPos - 11, yPos - 11, 11, 11);
            border[2] = new Rectangle(xPos - 11, yPos + 1, 11, 11);
            shape[1] = true;
            shape[3] = true;
        }
        if (orientation == 3) {
            border[1] = new Rectangle(xPos + 1, yPos - 11, 11, 11);
            border[3] = new Rectangle(xPos + 1, yPos + 1, 11, 11);
            shape[0] = true;
            shape[2] = true;
        }
    }


    public void damageWall(Rectangle bullet, int bulletPower) {
        bulletDestroyed = false;
        if (bulletPower == 2) {
            for (int i = 0; i < 4; i++) {
                if (border[i] != null) {
                    if (bullet.intersects(border[i])) {
                        bulletDestroyed = true;
                        border[i] = null;
                        shape[i] = true;
                    }
                }
            }
        }
        if (bulletPower == 1) {
            for (int i = 0; i < 4; i++) {
                if (border[i] != null) {
                    if (bullet.intersects(border[i]))
                        bulletDestroyed = true;
                }
            }
        }

        //将变化写入输出行
        gameModel.outputLine += "s" + xPos + "," + yPos + ",";
        for (boolean b : shape) {
            if (b)
                gameModel.outputLine += "1";
            else
                gameModel.outputLine += "0";
        }
        gameModel.outputLine += ";";

    }

    public boolean wallDestroyed() {
        if (wallDestroyed)
            return true;
        return border[0] == null && border[1] == null && border[2] == null && border[3] == null;
    }

    public Rectangle getBorder() {
        return generalBorder;
    }

    public Rectangle[] getDetailedBorder() {
        return border;
    }

    public void draw(Graphics g) {
        if (wallDestroyed)
            return;

        g.drawImage(steelWall, xPos - 12, yPos - 12, null);
        g.setColor(new Color(128, 64, 0));
        if (shape[0])
            g.fillRect(xPos - 12, yPos - 12, 13, 13);
        if (shape[1])
            g.fillRect(xPos, yPos - 12, 13, 13);
        if (shape[2])
            g.fillRect(xPos - 12, yPos, 13, 13);
        if (shape[3])
            g.fillRect(xPos, yPos, 13, 13);
    }

    public String getType() {
        return "steelWall";
    }


    //未使用的方法
    public void move() {
    }
}