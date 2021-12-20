package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;
import com.ProcessUnit.ClientPack.ClientLevel;

import java.awt.*;

/**
 * 这个类代表除了墙和钢墙外所有其他对象
 *
 * @author chenhong
 */
public class ClientNormalObject implements ClientGameComponent {
    private final String type;
    private Image image;
    private final int xPos;
    private final int yPos;

    /**
     * Instantiates a new Client normal object.
     * 构造函数
     * @param xPos       the x pos
     * @param yPos       the y pos
     * @param type       the type
     * @param imageIndex the image index
     */
    public ClientNormalObject(int xPos, int yPos, String type, int imageIndex) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        if (imageIndex != -1) {
            image = ClientLevel.textures[imageIndex];
        }
    }

    /**
     * 绘制普通物体
     * @param g the g
     */
    @Override
    public  void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, xPos - 12, yPos - 12, null);
        } else {
            g.setColor(new Color(0, 225, 0));
            for (int i = yPos - 11; i <= yPos + 12; i += 5) {
                g.drawLine(xPos - 12, i, xPos + 12, i);
            }
            for (int i = xPos - 11; i <= xPos + 12; i += 5) {
                g.drawLine(i, yPos - 12, i, yPos + 12);
            }
            g.setColor(new Color(0, 128, 0));
            for (int i = yPos - 10; i <= yPos + 12; i += 5) {
                g.drawLine(xPos - 12, i, xPos + 12, i);
            }
            for (int i = xPos - 10; i <= xPos + 12; i += 5) {
                g.drawLine(i, yPos - 12, i, yPos + 12);
            }
        }

        if (!"river".equals(type) && !"grass".equals(type) && !"base".equals(type)) {

            ClientDrawingPanel.removeActor(this);
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
     * Gets type.
     * 获取物体的类型
     * @return the type
     */
    public String getType() {
        return type;
    }
}