package com.SourceUnit.ClientPack;

import java.awt.*;

/**
 * The type Client brick client wall.
 * 砖墙
 * @author chenhong
 */
public class ClientBrickWall extends ClientWall implements ClientGameComponent {

    /**
     * Instantiates a new Client brick client wall.
     * 构造函数
     * @param xPos        the x pos
     * @param yPos        the y pos
     * @param orientation the orientation
     */
    public ClientBrickWall(int xPos, int yPos, int orientation) {
        super(xPos, yPos, orientation, Toolkit.getDefaultToolkit().getImage("image\\71.jpg"));
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
     * 绘制出砖墙
     * @param g the g
     */
    @Override
    public void draw(Graphics g) {
        boolean wallDestroyed = true;
        for (boolean b : shape) {
            if (!b) {
                wallDestroyed = false;
                break;
            }
        }
        if (wallDestroyed) {
            return;
        }
        g.drawImage(getImage(), super.getX() - 12, super.getY() - 12, null);
        g.setColor(new Color(128, 64, 0));
        if (shape[0]) {
            g.fillRect(super.getX() - 12, super.getY() - 12, 7, 7);
        }
        if (shape[1]) {
            g.fillRect(super.getX() - 6, super.getY() - 12, 7, 7);
        }
        if (shape[2]) {
            g.fillRect(super.getX(), super.getY() - 12, 7, 7);
        }
        if (shape[3]) {
            g.fillRect(super.getX() + 6, super.getY() - 12, 7, 7);
        }
        if (shape[4]) {
            g.fillRect(super.getX() - 12, super.getY() - 6, 7, 7);
        }
        if (shape[5]) {
            g.fillRect(super.getX() - 6, super.getY() - 6, 7, 7);
        }
        if (shape[6]) {
            g.fillRect(super.getX(), super.getY() - 6, 7, 7);
        }
        if (shape[7]) {
            g.fillRect(super.getX() + 6, super.getY() - 6, 7, 7);
        }
        if (shape[8]) {
            g.fillRect(super.getX() - 12, super.getY(), 7, 7);
        }
        if (shape[9]) {
            g.fillRect(super.getX() - 6, super.getY(), 7, 7);
        }
        if (shape[10]) {
            g.fillRect(super.getX(), super.getY(), 7, 7);
        }
        if (shape[11]) {
            g.fillRect(super.getX() + 6, super.getY(), 7, 7);
        }
        if (shape[12]) {
            g.fillRect(super.getX() - 12, super.getY() + 6, 7, 7);
        }
        if (shape[13]) {
            g.fillRect(super.getX() - 6, super.getY() + 6, 7, 7);
        }
        if (shape[14]) {
            g.fillRect(super.getX(), super.getY() + 6, 7, 7);
        }
        if (shape[15]) {
            g.fillRect(super.getX() + 6, super.getY() + 6, 7, 7);
        }
    }

    /**
     * Gets type.
     * 获取物体的种类
     * @return the type 返回的是一个字符串，表示物体种类
     */
    public String getType() {
        return "wall";
    }
}