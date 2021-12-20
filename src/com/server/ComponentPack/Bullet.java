package com.server.ComponentPack;

import com.ProcessUnit.Instruction;
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
    private ServerModel gameModel;
    private boolean hitTarget;

    public ServerModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(ServerModel gameModel) {
        this.gameModel = gameModel;
    }

    public Bullet(int a, int b, int c, int d, int e, GameComponent owner, ServerModel gameModel) {
        this.owner = owner;
        this.gameModel = gameModel;
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
            ServerModel.removeActor(this);
            notifyOwner();
            makeBomb();
            writeToOutputLine();
            return;
        }
        //检查这颗子弹是否击中其他对象
        for (int i = 0; i < ServerModel.gameComponents.length; i++) {
            if (ServerModel.gameComponents[i] != null) {
                if (ServerModel.gameComponents[i] != this && ServerModel.gameComponents[i] != owner) {
                    if (border.intersects(ServerModel.gameComponents[i].getBorder())) {

                        if ("steelWall".equals(ServerModel.gameComponents[i].getType())) {
                            SteelWall temp = (SteelWall) ServerModel.gameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("wall".equals(ServerModel.gameComponents[i].getType())) {
                            Wall temp = (Wall) ServerModel.gameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower, direction);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("bullet".equals(ServerModel.gameComponents[i].getType())) {
                            Bullet temp = (Bullet) ServerModel.gameComponents[i];
                            if ("Player".equals(temp.owner.getType())) {
                                hitTarget = true;
                                ServerModel.removeActor(ServerModel.gameComponents[i]);
                                temp.notifyOwner();
                            }
                        } else if ("Player".equals(ServerModel.gameComponents[i].getType())) {
                            if ("enemy".equals(owner.getType())) {
                                Player temp = (Player) ServerModel.gameComponents[i];
                                temp.hurt();
                            }
                            hitTarget = true;
                        } else if ("enemy".equals(ServerModel.gameComponents[i].getType()) && "Player".equals(owner.getType())) {
                            Enemy temp = (Enemy) ServerModel.gameComponents[i];
                            Player tempe = (Player) owner;
                            if (temp.getHealth() == 0) {
                                tempe.scores += (temp.getsType() * 100);
                            }
                            temp.hurt();
                            hitTarget = true;
                        } else if ("base".equals(ServerModel.gameComponents[i].getType())) {
                            Base temp = (Base) ServerModel.gameComponents[i];
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
            ServerModel.removeActor(this);
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
        ServerModel.addActor(new Bomb(xPos, yPos, "small", gameModel));
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
