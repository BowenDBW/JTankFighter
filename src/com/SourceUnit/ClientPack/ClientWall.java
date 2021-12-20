package com.SourceUnit.ClientPack;

import java.awt.*;

/**
 * 墙体类
 * @author chenhong
 */
public abstract class ClientWall implements ClientGameComponent {

    private final Image wall;
    private final int xPos;
    private final int yPos;

    /**
     * The Shape.
     */
    public boolean[] shape;

    /**
     * Instantiates a new Client wall.
     * 墙体的构造函数
     * @param xPos        the x pos
     * @param yPos        the y pos
     * @param orientation the orientation
     * @param wall        the wall
     */
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

    /**
     * 获取x坐标
     * @return xPos
     */
    @Override
    public int getX() {
        return xPos;
    }

    /**
     * 获取y坐标
     * @return yPos
     */
    @Override
    public int getY() {
        return yPos;
    }

    /**
     * Gets image.
     * 获取图片
     * @return the image
     */
    public Image getImage() {
        return wall;
    }

    /**
     * 抽象类，留给子类继承，用于绘制图像
     * @param g the g
     */
    @Override
    public abstract void draw(Graphics g) ;

}
