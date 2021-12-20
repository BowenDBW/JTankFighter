package com.SourceUnit.ServerPack;
import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerModel;

import java.awt.*;

public class ServerBase implements ServerGameComponent {

    private final Rectangle border;
    private Image base = Toolkit.getDefaultToolkit().getImage("image\\1.jpg");
    private int xPos, yPos;
    private int steelWallTime;
    private boolean baseKilled;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setSteelWallTime(int steelWallTime) {
        this.steelWallTime = steelWallTime;
    }

    public ServerBase() {
        xPos = 260;
        yPos = 498;
        border = new Rectangle(xPos - 11, yPos - 11, 23, 23);

    }

    @Override
    public Rectangle getBorder() {

        return border;
    }

    public void doom() {
        base = ServerModel.textures[1];
        if (!baseKilled) {
            ServerDrawingPanel.addActor(new ServerBomb(xPos, yPos, "big"));
        }
        baseKilled = true;

        //记录变化到输出行
        Instruction.getFromSever().append("b").append(xPos).append(",").append(yPos).append(",").append("1;");
    }

    @Override
    public void move() {
        if (steelWallTime == 600) {
            ServerSteelWall temp = new ServerSteelWall(248, 498, 2);
            ServerDrawingPanel.serverGameComponents[0] = temp;
            writeToOutputLine("s", temp.shape, 248, 498);

            temp = new ServerSteelWall(273, 498, 3);
            ServerDrawingPanel.serverGameComponents[1] = temp;
            writeToOutputLine("s", temp.shape, 273, 498);

            temp = new ServerSteelWall(248, 473, 1);
            ServerDrawingPanel.serverGameComponents[2] = temp;
            writeToOutputLine("s", temp.shape, 248, 473);

            temp = new ServerSteelWall(273, 473, 1);
            ServerDrawingPanel.serverGameComponents[3] = temp;
            writeToOutputLine("s", temp.shape, 273, 473);
        }
        if (steelWallTime > 0) {
            steelWallTime--;
        }
        if (steelWallTime == 1) {
            ServerWall temp = new ServerWall(248, 498, 2);
            ServerDrawingPanel.serverGameComponents[0] = temp;
            writeToOutputLine("w", temp.shape, 248, 498);

            temp = new ServerWall(273, 498, 3);
            ServerDrawingPanel.serverGameComponents[1] = temp;
            writeToOutputLine("w", temp.shape, 273, 498);

            temp = new ServerWall(248, 473, 1);
            ServerDrawingPanel.serverGameComponents[2] = temp;
            writeToOutputLine("w", temp.shape, 248, 473);

            temp = new ServerWall(273, 473, 1);
            ServerDrawingPanel.serverGameComponents[3] = temp;
            writeToOutputLine("w", temp.shape, 273, 473);
        }
    }

    public void writeToOutputLine(String type, boolean[] shape, int xPos, int yPos) {
        //记录变化到输出行
        Instruction.getFromSever().append(type).append(xPos).append(",").append(yPos).append(",");
		for (boolean b : shape) {
			if (b) {
                Instruction.getFromSever().append("1");
            } else {
                Instruction.getFromSever().append("0");
            }
		}
        Instruction.getFromSever().append(";");
    }

    @Override
    public String getType() {
        return "base";
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(base, xPos - 12, yPos - 12, null);
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