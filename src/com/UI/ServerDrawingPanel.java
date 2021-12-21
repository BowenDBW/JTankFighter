package com.UI;

import com.ProcessUnit.ServerPack.ServerLevel;
import com.ProcessUnit.ServerPack.ServerModel;
import com.ProcessUnit.ServerPack.ServerStatus;
import com.SourceUnit.ServerPack.ServerGameComponent;

import javax.swing.*;
import java.awt.*;

/**
 * The type Server drawing panel.
 * drawingPanel类属于服务器程序
 * @author chenhong
 */
public class ServerDrawingPanel extends JPanel {

    private Image offScreenImage;
    public static ServerGameComponent[] serverGameComponents;
    private static boolean gameStarted;
    private static int green, red, blue;

    private static String[] messageQueue;
    private static int messageIndex;

    /**
     * Instantiates a new Server drawing panel.
     * 空构造函数
     */
    public ServerDrawingPanel() {
    }

    /**
     * Sets game started.
     * 设置状态
     * @param newGameStarted the new game started
     */
    public void setGameStarted(boolean newGameStarted) {
        gameStarted = newGameStarted;
    }


    /**
     * 绘制
     * @param g 画笔
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
     * 绘制各部分
     * @param g the g
     */
    public void myPaint(Graphics g) {
        super.paintComponent(g);

        if (gameStarted) {
            //制作背景
            g.setColor(Color.blue);
            g.drawRect(10, 10, 501, 501);

            //制作坦克等等
            if (serverGameComponents != null) {
                for (ServerGameComponent serverGameComponent : serverGameComponents) {
                    if (serverGameComponent != null) {
                        serverGameComponent.draw(g);
                    }
                }
            }

            //制作级别关卡
            g.setColor(new Color(81, 111, 230));
            g.drawString("第  " + ServerLevel.getCurrentLevel() + "  关", 527, 39);
            g.drawString("敌人数 =  " + ServerLevel.getEnemyLeft(), 527, 79);

            //制作获胜场景
            if (ServerLevel.getWinningCount() > 150) {
                int temp = ServerLevel.getWinningCount() - 150;
                if (temp * 10 > 300) {
                    temp = 30;
                }
                if (ServerLevel.getWinningCount() > 470) {
                    temp = 500 - ServerLevel.getWinningCount();
                }
                g.setColor(Color.gray);
                g.fillRect(11, 11, 500, temp * 10);
                g.fillRect(11, 500 - temp * 10, 500, (1 + temp) * 10 + 2);

                if (ServerLevel.getWinningCount() > 190 && ServerLevel.getWinningCount() < 470) {
                    if (ServerLevel.getWinningCount() > 400) {
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
     * Remove message.
     * 删除屏幕上最早的信息
     */
    public static void removeMessage() {

        if (messageIndex == 0) {

            return;
        }

        messageIndex--;
        if (messageIndex >= 0) {

            System.arraycopy(messageQueue, 1, messageQueue, 0, messageIndex);
        }
        messageQueue[messageIndex] = null;

        //调用视图重绘屏幕如果比赛还没开始
        if (!ServerStatus.isGameStarted()) {

            ServerModel.getView().getMainPanel().repaint();
        }
    }

    /**
     * Add message.
     * 在屏幕上显示一行消息
     * @param message the message
     */
    public static void addMessage(String message) {
        if (messageIndex < 8) {
            messageQueue[messageIndex] = message;
            messageIndex++;
        } else {
            System.arraycopy(messageQueue, 1, messageQueue, 0, 7);
            messageQueue[7] = message;
        }

        //调用视图重绘屏幕如果游戏有没有开始
        if (!ServerStatus.isGameStarted()) {
            ServerModel.getView().getMainPanel().repaint();
        }
    }

    /**
     * Gets message index.
     * @return the message index
     */
    public static int getMessageIndex() {
        return messageIndex;
    }

    /**
     * Sets message queue.
     * @param messageQueue the message queue
     */
    public static void setMessageQueue(String[] messageQueue) {
        ServerDrawingPanel.messageQueue = messageQueue;
    }

    /**
     * Remove actor.
     * @param serverGameComponent the server game component
     */
    public static void removeActor(ServerGameComponent serverGameComponent) {
        for (int i = 0; i < serverGameComponents.length; i++) {
            if (serverGameComponents[i] == serverGameComponent) {
                serverGameComponents[i] = null;
                break;
            }
        }
    }

    /**
     * Add actor.
     * @param serverGameComponent the server game component
     */
    public static void addActor(ServerGameComponent serverGameComponent) {
        for (int i = 0; i < serverGameComponents.length; i++) {
            if (serverGameComponents[i] == null) {
                serverGameComponents[i] = serverGameComponent;
                break;
            }
        }
    }

    /**
     * Sets game components.
     * @param serverGameComponents the server game components
     */
    public static void setGameComponents(ServerGameComponent[] serverGameComponents) {
        ServerDrawingPanel.serverGameComponents = serverGameComponents;
    }
}