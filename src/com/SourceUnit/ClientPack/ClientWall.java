package com.SourceUnit.ClientPack;

import java.awt.*;

/**
 * <p>
 *
 * @author BowenDeng
 * @version 1.0
 * @date 2021/12/19 10:23
 **/
public abstract class ClientWall implements ClientGameComponent {

    private final Image wall;
    private final int xPos;
    private final int yPos;

    public boolean[] shape;

    public ClientWall(int xPos, int yPos, int orientation, Image wall) {
        this.wall = wall;
        this.xPos = xPos;
        this.yPos = yPos;
        shape = new boolean[16];

        if (orientation == 0) {
            for (int i = 8; i < 12; i++) {
                shape[i] = true;
            }
            for (int i = 12; i < 16; i++) {
                shape[i] = true;
            }
        }
        if (orientation == 1) {
            for (int i = 0; i < 4; i++) {
                shape[i] = true;
            }
            for (int i = 4; i < 8; i++) {
                shape[i] = true;
            }
        }
        if (orientation == 2) {
            for (int i = 3; i <= 15; i += 4) {
                shape[i] = true;
            }
            for (int i = 2; i <= 14; i += 4) {
                shape[i] = true;
            }
        }
        if (orientation == 3) {
            for (int i = 1; i <= 13; i += 4) {
                shape[i] = true;
            }
            for (int i = 0; i <= 12; i += 4) {
                shape[i] = true;
            }
        }
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }

    public Image getImage() {
        return wall;
    }

    @Override
    public abstract void draw(Graphics g) ;

}
