package com.client.ConponentPack;

import com.client.ClientUnit.ClientModel;

import java.awt.*;

/**
 * 这个类代表除了墙和钢墙外所有其他对象
 * @author 26317
 */
public class NormalObject implements GameComponent {
    private final String type;
    private Image image;
    private final int xPos;
    private final int yPos;

    public NormalObject(int xPos, int yPos, String type, int imageIndex) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        if (imageIndex != -1) {
            image = ClientModel.textures[imageIndex];
        }
    }

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

            ClientModel.removeActor(this);
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

    public String getType() {
        return type;
    }
}