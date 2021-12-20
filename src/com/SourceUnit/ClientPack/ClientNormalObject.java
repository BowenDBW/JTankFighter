package com.SourceUnit.ClientPack;

import com.UI.ClientDrawingPanel;
import com.ProcessUnit.ClientPack.ClientLevel;

import java.awt.*;

/**
 * 这个类代表除了墙和钢墙外所有其他对象
 * @author 26317
 */
public class ClientNormalObject implements ClientGameComponent {
    private final String type;
    private Image image;
    private final int xPos;
    private final int yPos;

    public ClientNormalObject(int xPos, int yPos, String type, int imageIndex) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        if (imageIndex != -1) {
            image = ClientLevel.textures[imageIndex];
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

            ClientDrawingPanel.removeActor(this);
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