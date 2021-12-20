package com.SourceUnit.ClientPack;

import java.awt.*;

/**
 * The interface Client game component.
 * 接口，规定每个物体需要实现的方法
 * @author chenhong
 */
public interface ClientGameComponent {

    /**
     * Draw.
     *  绘制
     * @param g the g
     */
    void draw(Graphics g);

    /**
     * Gets x.
     * 获取坐标
     * @return the x
     */
    int getX();

    /**
     * Gets y.
     * 获取坐标
     * @return the y
     */
    int getY();
}
