package com.SourceUnit.ServerPack;

import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerStatus;

import java.awt.*;

/**
 * 爆炸效果
 * @author chenhong
 */
public class ServerBomb implements ServerGameComponent {

    private final Rectangle border = new Rectangle(0, 0, 0, 0);
    private final String size;
    private int inner, middle, outer, jumpDistance;
    private int xPos, yPos;
    private int animationTime;

    /**
     * 获取x坐标
     * @return x pos
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
     * @return y pos
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
     * 类初始化
     * @param a x
     * @param b y
     * @param size size
     */
    public ServerBomb(int a, int b, String size) {
        this.size = size;
        if ("big".equals(size)) {
            inner = 6;
            middle = 9;
            outer = 14;
            jumpDistance = 8;
            animationTime = 6;
        } else if ("small".equals(size)) {
            inner = 2;
            middle = 4;
            outer = 7;
            jumpDistance = 4;
            animationTime = 4;
        }

        xPos = a;
        yPos = b;
    }

    /**
     * 绘制画面
     * @param g the draw
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(xPos - outer, yPos - outer, 2 * outer, 2 * outer);
        g.setColor(Color.orange);
        g.fillOval(xPos - middle, yPos - middle, 2 * middle, 2 * middle);
        g.setColor(Color.yellow);
        g.fillOval(xPos - inner, yPos - inner, 2 * inner, 2 * inner);
    }

    /**
     * 暂停处理
     */
    @Override
    public void move() {
        if (ServerStatus.isGamePaused()) {
            Instruction.getFromSever().append("o").append(xPos).append(",").append(yPos).append(",")
                    .append(size).append(";");
            return;
        }

        animationTime--;
        if (animationTime < 0) {
            ServerDrawingPanel.removeActor(this);
            return;
        }
        xPos = xPos + (int) (Math.random() * jumpDistance) - (int) (Math.random() * jumpDistance);
        yPos = yPos + (int) (Math.random() * jumpDistance) - (int) (Math.random() * jumpDistance);

        //将变化写入输出行
        Instruction.getFromSever().append("o").append(xPos)
                .append(",").append(yPos).append(",").append(size).append(";");

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
     * 获取类型
     * @return bomb
     */
    @Override
    public String getType() {
        return "bomb";
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
