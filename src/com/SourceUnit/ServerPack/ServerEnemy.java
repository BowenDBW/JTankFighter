package com.SourceUnit.ServerPack;

import com.CommunicateUnit.Instruction;
import com.UI.ServerDrawingPanel;
import com.ProcessUnit.ServerPack.ServerLevel;
import com.ProcessUnit.ServerPack.ServerModel;
import com.ProcessUnit.ServerPack.ServerStatus;

import java.awt.*;

/**
 * 敌方类
 */
public class ServerEnemy implements ServerGameComponent {

    private static int frozenTime;
    private static int frozenMoment;
    private final int size = 12;
    private final Rectangle map = new Rectangle(35, 35, 452, 452);
    private int numberOfBullet;
    private int coolDownTime;
    private int type;
    private final int speed;
    private int direction;
    private int interval;
    private int health;
    private int xPos, yPos, xvpos, yvpos;
    private final Rectangle border;
    private boolean flashing;
    private final double firePossibility;
    public Image[] textures;

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
     * 获取类型
     * @return type
     */
    public int getsType() {
        return type;
    }

    /**
     * 设置冰冻时间
     * @param frozenTime frozen time
     */
    public static void setFrozenTime(int frozenTime) {
        ServerEnemy.frozenTime = frozenTime;
    }

    /**
     * 设置冰冻时刻
     * @param frozenMoment frozen moment
     */
    public static void setFrozenMoment(int frozenMoment) {
        ServerEnemy.frozenMoment = frozenMoment;
    }

    /**
     * 获取子弹数量
     * @return the number
     */
    public int getNumberOfBullet() {
        return numberOfBullet;
    }

    /**
     * 设置子弹数量
     * @param numberOfBullet the number
     */
    public void setNumberOfBullet(int numberOfBullet) {
        this.numberOfBullet = numberOfBullet;
    }

    /**
     * 设置类型
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取健康值
     * @return health
     */
    public int getHealth() {
        return health;
    }

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
     * 属性初始化
     * @param type type
     * @param flashing flashing
     * @param xPos x pos
     * @param yPos y pos
     */
    public ServerEnemy(int type, boolean flashing, int xPos, int yPos) {
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.flashing = flashing;

        //设置全部敌人的共同属性
        interval = (int) (Math.random() * 200);
        direction = (int) (Math.random() * 4);
        numberOfBullet = 1;
        xvpos = xPos;
        yvpos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);

        //根据不同类型的敌人设置独特的属性如：容貌,速度,等等
        if (type == 1) {
            firePossibility = 0.95;
            speed = 2;
            textures = new Image[8];
            System.arraycopy(ServerModel.textures, 38, textures, 0, 8);
        } else if (type == 2) {
            firePossibility = 0.95;
            speed = 4;
            textures = new Image[8];
            System.arraycopy(ServerModel.textures, 2, textures, 0, 8);
        } else if (type == 3) {
            firePossibility = 0.9;
            speed = 2;
            textures = new Image[8];
            System.arraycopy(ServerModel.textures, 10, textures, 0, 8);
        } else {
            firePossibility = 0.95;
            health = 3;
            speed = 2;
            textures = new Image[20];
            System.arraycopy(ServerModel.textures, 18, textures, 0, 20);

        }
    }

    /**
     * 敌方坦克移动处理
     */
    @Override
    public void move() {
        if (ServerStatus.isGamePaused()) {
            writeToOutputLine();
            return;
        }

        if (frozenTime > ServerModel.getGameFlow() - frozenMoment) {
            writeToOutputLine();
            return;
        }


        //敌方坦克在一个周期内将会朝着相同的方向继续移动（如果不与其他对象相互影响）
        //在每个周期结束时，它将转向新的方向
        if (interval > 0) {
            interval--;
        }
        if (interval == 0) {
            interval = (int) (Math.random() * 200);
            int newDirection = (int) (Math.random() * 4);
            if (direction != newDirection) {
                if (direction / 2 != newDirection / 2) {
                    xPos = xvpos;
                    yPos = yvpos;
                    border.x = xPos - size;
                    border.y = yPos - size;
                }
                direction = newDirection;
            }

        }


        //完全随机决定是否要发射一颗子弹，敌方坦克不能开火
        //如果第一个不是摧毁的子弹
        if (coolDownTime > 0) {
            coolDownTime--;
        }
        int down = 1;
        int up = 0;
        int left = 2;
        if (Math.random() > firePossibility && coolDownTime == 0 && numberOfBullet > 0) {
            //获得子弹方向
            int c = direction;
            //获得子弹位置
            int a, b;
            if (direction == up) {
                a = xPos;
                b = yPos - size;
            } else if (direction == down) {
                a = xPos;
                b = yPos + size;
            } else if (direction == left) {
                a = xPos - size;
                b = yPos;
            } else {
                a = xPos + size;
                b = yPos;
            }
            //获得子弹速度
            int d;
            if (type == 3) {
                d = 12;
            } else {
                d = 7;
            }
            //添加子弹
            ServerDrawingPanel.addActor(new ServerBullet(a, b, c, d, 1, this));
            coolDownTime = 7;
            if (type == 3) {
                coolDownTime = 5;
            }
            numberOfBullet--;
        }

        //保存当前位置信息,如果确定了新举措无效后,然后改变

        int xPosTemp = xPos;
        int yPosTemp = yPos;
        Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25, 25);

        //定义地方坦克的下一个边界，假设它有效的根据方向来进行移动
        if (direction == up) {
            yPos -= speed;
        } else if (direction == down) {
            yPos += speed;
        } else if (direction == left) {
            xPos -= speed;
        } else {
            xPos += speed;
        }


        //更新边界
        border.y = yPos - size;
        border.x = xPos - size;

        //检查下一个边界是否会与地图边界相交，如果不相交则随机生成边界
        if (!border.intersects(map)) {
            direction = (int) (Math.random() * 4);
            interval = (int) (Math.random() * 250);
            xPos = xvpos;
            yPos = yvpos;
            border.x = xPos - size;
            border.y = yPos - size;
            writeToOutputLine();
            return;
        }

        //检查下一个边界是否与其他对象相交，例如玩家控制的坦克，墙等等
        for (int i = 0; i < ServerDrawingPanel.serverGameComponents.length; i++) {
            if (ServerDrawingPanel.serverGameComponents[i] != null) {
                if (this != ServerDrawingPanel.serverGameComponents[i]) {
                    if (border.intersects(ServerDrawingPanel.serverGameComponents[i].getBorder())) {
                        //静态对象，例如河流，墙等等
                        if ("steelWall".equals(ServerDrawingPanel.serverGameComponents[i].getType()) ||
                                "wall".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            if (!ServerDrawingPanel.serverGameComponents[i].wallDestroyed()) {
                                for (int j = 0; j < ServerDrawingPanel.serverGameComponents[i].getDetailedBorder().length; j++) {
                                    if (ServerDrawingPanel.serverGameComponents[i].getDetailedBorder()[j] != null) {
                                        if (ServerDrawingPanel.serverGameComponents[i].getDetailedBorder()[j].intersects(border)) {
                                            if (Math.random() > 0.90) {
                                                direction = (int) (Math.random() * 4);
                                            }
                                            xPos = xvpos;
                                            yPos = yvpos;
                                            border.x = xPos - size;
                                            border.y = yPos - size;
                                            writeToOutputLine();
                                            return;
                                        }
                                    }
                                }
                            }
                        } else if ("river".equals(ServerDrawingPanel.serverGameComponents[i].getType()) ||
                                "base".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            if (Math.random() > 0.90) {
                                direction = (int) (Math.random() * 4);
                            }
                            xPos = xvpos;
                            yPos = yvpos;
                            border.x = xPos - size;
                            border.y = yPos - size;
                            writeToOutputLine();
                            return;
                        }
                        //其他对象，其他的坦克
                        if ("Player".equals(ServerDrawingPanel.serverGameComponents[i].getType()) ||
                                "enemy".equals(ServerDrawingPanel.serverGameComponents[i].getType())) {
                            if (!borderTemp.intersects(ServerDrawingPanel.serverGameComponents[i].getBorder())) {
                                xPos = xPosTemp;
                                yPos = yPosTemp;
                                border.x = xPos - size;
                                border.y = yPos - size;
                                int newDirection = (int) (Math.random() * 4);
                                if (direction != newDirection) {
                                    if (direction / 2 != newDirection / 2) {
                                        xPos = xvpos;
                                        yPos = yvpos;
                                        border.x = xPos - size;
                                        border.y = yPos - size;
                                    }
                                    direction = newDirection;
                                }
                                writeToOutputLine();
                                return;
                            }
                        }
                    }
                }
            }
        }


        ///当坦克是90度倾斜时，找到坦克的虚拟位置，使用虚拟位置调整坦克的真实位置
        int a = (xPos - 10) / 25;
        int b = (xPos - 10) % 25;
        if (b < 7) {
            b = 0;
        }
        if (b > 18) {
            b = 25;
        }
        boolean flag = (b < 19 && b > 6) || xPos < 17 || xPos > 492;
        if (flag) {
            b = 13;
        }
        xvpos = a * 25 + b + 10;
        int c = (yPos - 10) / 25;
        int d = (yPos - 10) % 25;
        if (d < 7) {
            d = 0;
        }
        if (d > 18) {
            d = 25;
        }
        boolean flag1 = (d < 19 && d > 6) || yPos < 17 || yPos > 492;
        if (flag1) {
            d = 13;
        }
        yvpos = c * 25 + d + 10;
        writeToOutputLine();
    }

    /**
     * 记录变化
     */
    public void writeToOutputLine() {
        //将变化写入输出行
        Instruction.getFromSever().append("n").append(xPos).append(",").append(yPos).append(",");
        int textureIndex;
        if (flashing && ServerModel.getGameFlow() % 10 > 4) {
            if (type == 1) {
                textureIndex = 42 + direction;
            } else if (type == 2) {
                textureIndex = 6 + direction;
            } else if (type == 3) {
                textureIndex = 14 + direction;
            } else {
                textureIndex = 34 + direction;
            }
        } else {
            if (type == 1) {
                textureIndex = 38 + direction;
            } else if (type == 2) {
                textureIndex = 2 + direction;
            } else if (type == 3) {
                textureIndex = 10 + direction;
            } else {
                if (health == 3) {
                    textureIndex = 18 + direction;
                } else if (health == 2) {
                    textureIndex = 22 + direction;
                } else if (health == 1) {
                    textureIndex = 26 + direction;
                } else {
                    textureIndex = 30 + direction;
                }
            }
        }
        Instruction.getFromSever().append(textureIndex).append(";");
    }

    /**
     * 敌方坦克射击子弹判断
     */
    public void hurt() {
        if (flashing) {
            ServerDrawingPanel.addActor(new ServerPowerUp());
        }
        flashing = false;
        boolean death = false;
        if (type != 4) {
            death = true;
        } else {
            if (health == 0) {
                death = true;
            } else {
                if (health == 3) {
                    System.arraycopy(textures, 4, textures, 0, 4);
                } else if (health == 2) {
                    System.arraycopy(textures, 8, textures, 0, 4);
                } else if (health == 1) {
                    System.arraycopy(textures, 12, textures, 0, 4);
                }
                health--;
            }
        }

        if (death) {

            int noOfEnemy = ServerLevel.getNoOfEnemy();
            noOfEnemy--;
            ServerLevel.setNoOfEnemy(noOfEnemy);

            int deathCount = ServerLevel.getDeathCount();
            deathCount++;
            ServerLevel.setDeathCount(deathCount);
            ServerDrawingPanel.removeActor(this);
            ServerDrawingPanel.addActor(new ServerBomb(xPos, yPos, "big"));
        }
    }

    /**
     * 获取类型
     * @return enemy
     */
    @Override
    public String getType() {
        return "enemy";
    }

    /**
     * 绘制画面
     * @param g the draw
     */
    @Override
    public void draw(Graphics g) {
        if (flashing && ServerModel.getGameFlow() % 10 > 4) {
            g.drawImage(textures[textures.length - 4 + direction], xPos - size, yPos - size, null);
        } else {
            g.drawImage(textures[direction], xPos - size, yPos - size, null);
        }
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