package com.SourceUnit.ServerPack;
import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerModel;

import java.awt.*;

/**
 * 基地类
 */
public class ServerBase implements ServerGameComponent {

    private final Rectangle border;
    private Image base = Toolkit.getDefaultToolkit().getImage("image\\1.jpg");
    private int xPos, yPos;
    private int steelWallTime;
    private boolean baseKilled;

    /**
     * 获取x坐标
     * @return xpos
     */
    public int getxPos() {

        return xPos;

    }

    /**
     * 设置x坐标
     * @param xPos x pos
     */
    public void setxPos(int xPos) {

        this.xPos = xPos;

    }

    /**
     * 获取y坐标
     * @return ypos
     */
    public int getyPos() {

        return yPos;

    }

    /**
     * 设置y坐标
     * @param yPos y pos
     */
    public void setyPos(int yPos) {

        this.yPos = yPos;

    }

    /**
     * 设置铁墙保护基地时间
     * @param steelWallTime steel wall time
     */
    public void setSteelWallTime(int steelWallTime) {

        this.steelWallTime = steelWallTime;

    }

    /**
     * 初始化坐标、边界
     */
    public ServerBase() {

        xPos = 260;
        yPos = 498;
        border = new Rectangle(xPos - 11, yPos - 11, 23, 23);

    }

    /**
     * 获取边界
     * @return border
     */
    @Override
    public Rectangle getBorder() {

        return border;

    }

    /**
     * 判断基地是否被摧毁
     */
    public void doom() {

        base = ServerModel.textures[1];

        if (!baseKilled) {
            ServerDrawingPanel.addActor(new ServerBomb(xPos, yPos, "big"));
        }

        baseKilled = true;
        //记录变化到输出行
        Instruction.getFromSever().append("b").append(xPos).append(",").append(yPos).append(",").append("1;");
    }

    /**
     * 铁墙保护道具
     */
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

    /**
     * 记录变化
     * @param type type
     * @param shape shape
     * @param xPos x pos
     * @param yPos y pos
     */
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

    /**
     * 获取类型
     * @return base
     */
    @Override
    public String getType() {
        return "base";
    }

    /**
     * 绘制画面
     * @param g base
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(base, xPos - 12, yPos - 12, null);
    }

    /**
     * 未使用方法
     * @return null
     */
    @Override
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    /**
     * 击毁墙处理
     * @return false
     */
    @Override
    public boolean wallDestroyed() {
        return false;
    }

}