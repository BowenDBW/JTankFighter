package com.client.ClientUnit;

import com.client.ConponentPack.GameComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author chenhong
 * 绘图面板类属于客户端程序
 */
public class DrawingPanel extends JPanel {
    private Image offScreenImage;

    public String[] messageQueue;
    public GameComponent[] drawingList;

    private boolean gameStarted;
    private int green, red, blue;
    private int p1Life, p2Life, p1Score, p2Score, enemyLeft, levelIndex;
    private final Image p1Image = Toolkit.getDefaultToolkit().getImage("image\\" + 55 + ".jpg");;
    private final Image p2Image = Toolkit.getDefaultToolkit().getImage("image\\" + 73 + ".jpg");



    public void setEnemyLeft(int newEnemyLeft) {
        enemyLeft = newEnemyLeft;
    }


    public void setLevelIndex(int newLevelIndex) {
        levelIndex = newLevelIndex;
    }


    public void setP1Life(int newP1Life) {
        p1Life = newP1Life;
    }


    public void setP2Life(int newP2Life) {
        p2Life = newP2Life;
    }


    public void setP1Score(int newP1Score) {
        p1Score = newP1Score;
    }

    public void setP2Score(int newP2Score) {
        p2Score = newP2Score;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

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

    public void myPaint(Graphics g) {
        super.paintComponent(g);

        if (gameStarted) {
            //画游戏信息
            g.setColor(new Color(81, 111, 230));
            g.drawString("第  " + levelIndex + "  关", 527, 39);
            g.drawString("敌人数 =  " + enemyLeft, 527, 79);

            g.setColor(Color.yellow);
            g.drawImage(p1Image, 520, 380, null);
            g.drawString("x", 555, 395);
            g.drawString(p1Life  + "", 565, 396);
            String score = "000000000" + p1Score;
            g.drawString("P1" + " 得分:" + "", 515, 370);
            g.drawString(score.substring(score.length() - 7) + "", 566, 370);

            g.setColor(Color.green);
            g.drawImage(p2Image, 520, 460, null);
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
                for (GameComponent actor : drawingList) {
                    if (actor != null) {
                        actor.draw(g);
                    }
                }
            }

            //绘制获胜场景
            if (Level.getWinningCount() > 150) {


                int temp = Level.getWinningCount() - 150;
                if (temp * 10 > 300) {
                    temp = 30;
                }
                if (Level.getWinningCount() > 470) {
                    temp = 500 - Level.getWinningCount();
                }
                g.setColor(Color.gray);
                g.fillRect(11, 11, 500, temp * 10);
                g.fillRect(11, 500 - temp * 10, 500, (1 + temp) * 10 + 2);

                if (Level.getWinningCount() > 190 && Level.getWinningCount() < 470) {

                    if (Level.getWinningCount() > 400) {

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
}