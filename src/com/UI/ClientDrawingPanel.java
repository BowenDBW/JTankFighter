package com.UI;

import com.ProcessUnit.ClientPack.ClientLevel;
import com.ProcessUnit.ClientPack.ClientModel;
import com.ProcessUnit.ClientPack.ClientStatus;
import com.SourceUnit.ClientPack.ClientGameComponent;

import javax.swing.*;
import java.awt.*;

/**
 * The type Client drawing panel.
 * 主面板
 * @author chenhong  绘图面板类属于客户端程序
 */
public class ClientDrawingPanel extends JPanel {
    private Image offScreenImage;

    /**
     * The Message queue.
     */
    public static String[] messageQueue;
    private static int messageIndex;

    /**
     * The Drawing list.
     */
    public static ClientGameComponent[] drawingList;

    private int green, red, blue;
    private int p1Life, p2Life, p1Score, p2Score, enemyLeft, levelIndex;
    private final Image P1Image = Toolkit.getDefaultToolkit().getImage("image\\" + 55 + ".jpg");
    private final Image P2Image = Toolkit.getDefaultToolkit().getImage("image\\" + 73 + ".jpg");

    /**
     * Sets enemy left.
     * 设置敌人的剩余量
     * @param newEnemyLeft the new enemy left
     */
    public void setEnemyLeft(int newEnemyLeft) {
        enemyLeft = newEnemyLeft;
    }


    /**
     * Sets level index.
     * 设置关卡
     * @param newLevelIndex the new level index
     */
    public void setLevelIndex(int newLevelIndex) {
        levelIndex = newLevelIndex;
    }


    /**
     * Sets p 1 life.
     * 设置玩家生命值
     * @param newP1Life the new p 1 life
     */
    public void setP1Life(int newP1Life) {
        p1Life = newP1Life;
    }


    /**
     * Sets p 2 life.
     * 设置玩家生命值
     * @param newP2Life the new p 2 life
     */
    public void setP2Life(int newP2Life) {
        p2Life = newP2Life;
    }


    /**
     * Sets p 1 score.
     * 设置玩家分数
     * @param newP1Score the new p 1 score
     */
    public void setP1Score(int newP1Score) {
        p1Score = newP1Score;
    }

    /**
     * Sets p 2 score.
     * 设置玩家分数
     * @param newP2Score the new p 2 score
     */
    public void setP2Score(int newP2Score) {
        p2Score = newP2Score;
    }


    /**
     * 绘制各个组件
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        Graphics offScreenGraphics;
        if (offScreenImage == null) {

            offScreenImage = createImage(640, 550);
        }
        offScreenGraphics = offScreenImage.getGraphics();
        myPaint(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, this);
    }

    /**
     * My paint.
     * 绘制游戏信息，显示在面板上
     * @param g the g
     */
    public void myPaint(Graphics g) {
        super.paintComponent(g);

        if (ClientStatus.isGameStarted()) {
            //画游戏信息
            g.setColor(new Color(81, 111, 230));
            g.drawString("第  " + levelIndex + "  关", 527, 39);
            g.drawString("敌人数 =  " + enemyLeft, 527, 79);

            g.setColor(Color.yellow);
            g.drawImage(P1Image, 520, 380, null);
            g.drawString("x", 555, 395);
            g.drawString(p1Life  + "", 565, 396);
            String score = "000000000" + p1Score;
            g.drawString("P1" + " 得分:" + "", 515, 370);
            g.drawString(score.substring(score.length() - 7) + "", 566, 370);

            g.setColor(Color.green);
            g.drawImage(P2Image, 520, 460, null);
            g.drawString("x", 555, 475);
            g.drawString(p2Life + "", 565, 476);
            score = "000000000" + p2Score;
            g.drawString("P2" + " 得分:" + "", 515, 450);
            g.drawString(score.substring(score.length() - 7) + "", 566, 450);


            //绘制背景
            g.setColor(Color.blue);
            g.drawRect(10, 10, 501, 501);

            //绘制坦克等等
            if (drawingList != null) {
                for (ClientGameComponent actor : drawingList) {
                    if (actor != null) {
                        actor.draw(g);
                    }
                }
            }

            //绘制获胜场景
            if (ClientLevel.getWinningCount() > 150) {


                int temp = ClientLevel.getWinningCount() - 150;
                if (temp * 10 > 300) {
                    temp = 30;
                }
                if (ClientLevel.getWinningCount() > 470) {
                    temp = 500 - ClientLevel.getWinningCount();
                }
                g.setColor(Color.gray);
                g.fillRect(11, 11, 500, temp * 10);
                g.fillRect(11, 500 - temp * 10, 500, (1 + temp) * 10 + 2);

                if (ClientLevel.getWinningCount() > 190 && ClientLevel.getWinningCount() < 470) {

                    if (ClientLevel.getWinningCount() > 400) {

                        red += (int) ((128 - red) * 0.2);
                        green += (int) ((128 - green) * 0.2);
                    }
                    g.setColor(new Color(red, green, blue));
                    g.drawString("过 关 了  ！", 240, 250);
                }
            } else {

                green = 23;
                red = 34;
                blue = 128;
            }
        }

        //消息
        g.setColor(new Color(255, 255, 255));
        if (messageQueue != null) {

            for (int i = 0; i < 8; i++) {

                if (messageQueue[i] != null) {
                    g.drawString(messageQueue[i], 5, 12 + i * 16);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Add message.
     * 在屏幕上显示一条消息
     * @param message the message
     */
    public static void addMessage(String message) {
        if (messageIndex < 8) {
            messageQueue[messageIndex] = message;
            messageIndex++;
        } else {
            System.arraycopy
                    (messageQueue, 1, messageQueue, 0, 7);
            messageQueue[7] = message;
        }

        //调用视图来重新绘制屏幕，如果没有开始游戏
        if (!ClientStatus.isGameStarted()) {
            ClientModel.getView().getMainPanel().repaint();
        }
    }

    /**
     * Remove message.
     * 删除最早的消息在屏幕上
     */
    public static void removeMessage() {
        if (messageIndex == 0) {
            return;
        }

        messageIndex--;
        if (messageIndex >= 0) {
            System.arraycopy
                    (messageQueue, 1, messageQueue, 0, messageIndex);
        }
        messageQueue[messageIndex] = null;

        //调用视图来重新绘制屏幕如果没有开始游戏
        if (!ClientStatus.isGameStarted()) {
            ClientModel.getView().getMainPanel().repaint();
        }
    }

    /**
     * Add actor.
     * 添加一个游戏对象(如坦克、子弹等)图纸清单
     * @param actor the actor
     */
    public static void addActor(ClientGameComponent actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == null) {
                drawingList[i] = actor;
                break;
            }
        }
    }

    /**
     * Remove actor.
     * 删除一个游戏对象从图纸清单
     * @param actor the actor
     */
    public static void removeActor(ClientGameComponent actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == actor) {
                drawingList[i] = null;
                break;
            }
        }
    }
}