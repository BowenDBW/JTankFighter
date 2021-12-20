package com.SourceUnit.ServerPack;

import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerStatus;

import java.awt.*;

/**
 * 子弹类
 */
public class ServerBullet implements ServerGameComponent {

    private final Rectangle map = new Rectangle(18, 18, 486, 486);
    private final Rectangle border;
    private final int direction;
    private final int Speed;
    private final int bulletPower;
    private int xPos, yPos;
    private final ServerGameComponent owner;
    private boolean hitTarget;

    /**
     * 初始化相关属性
     * @param a x pos
     * @param b y pos
     * @param c direction
     * @param d speed
     * @param e bullet power
     * @param owner owner
     */
    public ServerBullet(int a, int b, int c, int d, int e, ServerGameComponent owner) {
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

    /**
     * 绘制画面
     * @param g the draw
     */
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

    /**
     * 子弹射击处理
     */
    @Override
    public void move() {
        if (ServerStatus.isGamePaused()) {
            writeToOutputLine();
            return;
        }

        //检查这子弹是否撞击地图边界
        if (!border.intersects(map)) {
            ServerDrawingPanel.removeActor(this);
            notifyOwner();
            makeBomb();
            writeToOutputLine();
            return;
        }
        //检查这颗子弹是否击中其他对象
        for (int i = 0; i < ServerDrawingPanel.serverGameComponents.length; i++) {
            if (ServerDrawingPanel.serverGameComponents[i] != null) {
                if (ServerDrawingPanel.serverGameComponents[i] != this && ServerDrawingPanel.serverGameComponents[i] != owner) {
                    if (border.intersects(ServerDrawingPanel.serverGameComponents[i].getBorder())) {

                        if ("steelWall".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            ServerSteelWall temp = (ServerSteelWall) ServerDrawingPanel.serverGameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("wall".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            ServerWall temp = (ServerWall) ServerDrawingPanel.serverGameComponents[i];
                            if (!temp.isWallDestroyed()) {
                                temp.damageWall(border, bulletPower, direction);
                                if (temp.isBulletDestroyed()) {
                                    hitTarget = true;
                                }
                            }
                        } else if ("bullet".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            ServerBullet temp = (ServerBullet) ServerDrawingPanel.serverGameComponents[i];
                            if ("Player".equals(temp.owner.getType())) {
                                hitTarget = true;
                                ServerDrawingPanel.removeActor(ServerDrawingPanel.serverGameComponents[i]);
                                temp.notifyOwner();
                            }
                        } else if ("Player".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            if ("enemy".equals(owner.getType())) {
                                ServerPlayer temp = (ServerPlayer) ServerDrawingPanel.serverGameComponents[i];
                                temp.hurt();
                            }
                            hitTarget = true;
                        } else if ("enemy".equals(ServerDrawingPanel.serverGameComponents[i].getType()) && "Player".equals(owner.getType())) {
                            ServerEnemy temp = (ServerEnemy) ServerDrawingPanel.serverGameComponents[i];
                            ServerPlayer tempe = (ServerPlayer) owner;
                            if (temp.getHealth() == 0) {
                                tempe.scores += (temp.getsType() * 100);
                            }
                            temp.hurt();
                            hitTarget = true;
                        } else if ("base".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            ServerBase temp = (ServerBase) ServerDrawingPanel.serverGameComponents[i];
                            temp.doom();
                            hitTarget = true;
                            ServerStatus.setGameOver(true);
                        }
                    }
                }
            }
        }

        //如果子弹打到其他对象,从游戏系统中删除这个子弹对象
        if (hitTarget) {
            ServerDrawingPanel.removeActor(this);
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

    /**
     * 记录变化
     */
    public void writeToOutputLine() {

        Instruction.getFromSever().append("t").append(xPos).append(",")
                .append(yPos).append(",").append(direction).append(";");
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
     * @return bullet
     */
    @Override
    public String getType() {
        return "bullet";
    }

    /**
     * 角色判断
     */
    public void notifyOwner() {
        if (owner != null) {
            if ("Player".equals(owner.getType())) {

                ServerPlayer temp = (ServerPlayer) owner;
                int numberOfBullet = temp.getNumberOfBullet();
                numberOfBullet++;
                temp.setNumberOfBullet(numberOfBullet);

            } else if ("enemy".equals(owner.getType())) {
                ServerEnemy temp = (ServerEnemy) owner;
                int numberOfBullet = temp.getNumberOfBullet();
                numberOfBullet++;
                temp.setNumberOfBullet(numberOfBullet);
            }
        }
    }

    /**
     * 制造效果
     */
    public void makeBomb() {
        ServerDrawingPanel.addActor(new ServerBomb(xPos, yPos, "small"));
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
