package com.server.ComponentPack;

import com.ProcessUnit.Instruction;
import com.server.ServerUnit.DrawingPanel;
import com.server.ServerUnit.ServerCommunication;
import com.server.ServerUnit.ServerModel;
import com.server.ServerUnit.Status;

import java.awt.*;

public class Bullet implements GameComponent {

    private final Rectangle map = new Rectangle(18, 18, 486, 486);
    private final Rectangle border;
    private final int direction;
    private final int Speed;
    private final int bulletPower;
    private int xPos, yPos;
    private final GameComponent owner;
    private boolean hitTarget;

    public Bullet(int a, int b, int c, int d, int e, GameComponent owner) {
        this.owner = owner;
        xPos = a;
        yPos = b;
        direction = c;
        if (direction == 0 || direction == 1) {
            border = new Rectangle(a - 2, b - 5, 5, 13);
        } else {
            border = new Rectangle(a - 5, b - 2, 13, 5);
        }

        Speed = d;
        bulletPower = e;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.lightGray);
        if (direction == 0 || direction == 1) {
            g.fillRect(border.x + 1, border.y + 1, 3, 9);
        }
        if (direction == 2 || direction == 3) {
            g.fillRect(border.x + 1, border.y + 1, 9, 3);
        }
    }

    @Override
    public void move() {
        if (Status.isGamePaused()) {
            writeToOutputLine();
            return;
        }


        //检查这子弹是否撞击地图边界
        if (!border.intersects(map)) {
            DrawingPanel.removeActor(this);
            notifyOwner();
            makeBomb();
            writeToOutputLine();
            return;
        }
        //检查这颗子弹是否击中其他对象
        for (int i = 0; i < DrawingPanel.gameComponents.length; i++) {
            if (DrawingPanel.gameComponents[i] != null) {
                if (DrawingPanel.gameComponents[i] != this && DrawingPanel.gameComponents[i] != owner) {
                    if (border.intersects(DrawingPanel.gameComponents[i].getBorder())) {

                        if ("steelWall".equals(DrawingPanel.gameComponents[i].getType())) {
                            SteelWall temp = (SteelWall) DrawingPanel.gameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("wall".equals(DrawingPanel.gameComponents[i].getType())) {
                            Wall temp = (Wall) DrawingPanel.gameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower, direction);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("bullet".equals(DrawingPanel.gameComponents[i].getType())) {
                            Bullet temp = (Bullet) DrawingPanel.gameComponents[i];
                            if ("Player".equals(temp.owner.getType())) {
                                hitTarget = true;
                                DrawingPanel.removeActor(DrawingPanel.gameComponents[i]);
                                temp.notifyOwner();
                            }
                        } else if ("Player".equals(DrawingPanel.gameComponents[i].getType())) {
                            if ("enemy".equals(owner.getType())) {
                                Player temp = (Player) DrawingPanel.gameComponents[i];
                                temp.hurt();
                            }
                            hitTarget = true;
                        } else if ("enemy".equals(DrawingPanel.gameComponents[i].getType()) && "Player".equals(owner.getType())) {
                            Enemy temp = (Enemy) DrawingPanel.gameComponents[i];
                            Player tempe = (Player) owner;
                            if (temp.getHealth() == 0) {
                                tempe.scores += (temp.getsType() * 100);
                            }
                            temp.hurt();
                            hitTarget = true;
                        } else if ("base".equals(DrawingPanel.gameComponents[i].getType())) {
                            Base temp = (Base) DrawingPanel.gameComponents[i];
                            temp.doom();
                            hitTarget = true;
                            Status.setGameOver(true);
                        }
                    }
                }
            }
        }

        //如果子弹打到其他对象,从游戏系统中删除这个子弹对象
        if (hitTarget) {
            DrawingPanel.removeActor(this);
            notifyOwner();
            makeBomb();
            writeToOutputLine();
            return;
        }

        if (direction == 0) {
            border.y -= Speed;
            yPos -= Speed;
        }
        if (direction == 1) {
            border.y += Speed;
            yPos += Speed;
        }
        if (direction == 2) {
            border.x -= Speed;
            xPos -= Speed;
        }
        if (direction == 3) {
            border.x += Speed;
            xPos += Speed;
        }
        writeToOutputLine();
    }

    public void writeToOutputLine() {

        Instruction.getFromSever().append("t").append(xPos).append(",")
                .append(yPos).append(",").append(direction).append(";");
    }

    @Override
    public Rectangle getBorder() {
        return border;
    }

    @Override
    public String getType() {
        return "bullet";
    }

    public void notifyOwner() {
        if (owner != null) {
            if ("Player".equals(owner.getType())) {

                Player temp = (Player) owner;
                int numberOfBullet = temp.getNumberOfBullet();
                numberOfBullet++;
                temp.setNumberOfBullet(numberOfBullet);

            } else if ("enemy".equals(owner.getType())) {
                Enemy temp = (Enemy) owner;
                int numberOfBullet = temp.getNumberOfBullet();
                numberOfBullet++;
                temp.setNumberOfBullet(numberOfBullet);
            }
        }
    }

    public void makeBomb() {
        DrawingPanel.addActor(new Bomb(xPos, yPos, "small"));
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
