package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;

import java.awt.*;


/**
 * 爆炸效果
 * @author chenhong
 */
public class ClientBomb implements ClientGameComponent {

    private final int xPos;
    private final int yPos;
    private int inner, middle, outer;

    /**
     * Instantiates a new Client bomb.
     *
     * @param a    the a  x坐标
     * @param b    the b  y坐标
     * @param size        大小
     */
    public ClientBomb(int a, int b, int size) {
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

    /**
     * 绘制爆炸样式
     * @param g the g 画笔
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(xPos - outer, yPos - outer, 2 * outer, 2 * outer);
        g.setColor(Color.orange);
        g.fillOval(xPos - middle, yPos - middle, 2 * middle, 2 * middle);
        g.setColor(Color.yellow);
        g.fillOval(xPos - inner, yPos - inner, 2 * inner, 2 * inner);

        ClientDrawingPanel.removeActor(this);
    }

    /**
     * 获取x坐标
     * @return x坐标值
     */
    @Override
    public int getX() {
        return xPos;
    }

    /**
     * 获取y坐标
     * @return y坐标值
     */
    @Override
    public int getY() {
        return yPos;
    }
}