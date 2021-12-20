package com.SourceUnit.ServerPack;

import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerModel;

import java.awt.*;

/**
 * @author chenhong
 */
public class ServerPowerUp implements ServerGameComponent {
    private int function;
    private final Rectangle border;
    private int displayTime;
    public Image[] textures;

    private final int xPos;
    private final int yPos;

    public int getFunction() {
        return function;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public ServerPowerUp() {

        //加载图像
        textures = new Image[7];
        System.arraycopy(ServerModel.textures, 46, textures, 0, 7);

        xPos = 24 + (int) (Math.random() * 475);
        yPos = 24 + (int) (Math.random() * 475);
        int a = (int) (Math.random() * 17);
        if (0 <= a && a < 3) {
            function = 0;
        }
        if (3 <= a && a < 6) {
            function = 1;
        }
        if (6 <= a && a < 9) {
            function = 2;
        }
        if (9 <= a && a < 12) {
            function = 3;
        }
        if (12 <= a && a < 15) {
            function = 4;
        }
        if (a == 15) {
            function = 5;
        }
        if (a == 16) {
            function = 6;
        }
        displayTime = 100 + (int) (Math.random() * 630);
        border = new Rectangle(xPos - 12, yPos - 12, 25, 25);
    }

    @Override
    public Rectangle getBorder() {
        return border;
    }

    @Override
    public String getType() {
        return "powerUp";
    }

    @Override
    public void move() {
        displayTime--;
        if (displayTime == 0) {
            ServerDrawingPanel.removeActor(this);
        }

        //将变化写入输出行
        Instruction.getFromSever().append("n").append(xPos).append(",").append(yPos).append(",")
                .append(46 + function).append(";");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(textures[function], xPos - 12, yPos - 12, null);
    }

    //未使用的方法
    @Override
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    @Override
    public boolean wallDestroyed() {
        return false;
    }

}