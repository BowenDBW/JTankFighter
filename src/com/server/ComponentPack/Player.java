package com.server.ComponentPack;

import com.server.ServerUnit.Level;
import com.server.ServerUnit.ServerModel;
import com.server.ServerUnit.Status;

import java.awt.*;

public class Player implements GameComponent {
    private final int UP = 0;
    private final int size = 12;
    private final Rectangle map = new Rectangle(35, 35, 452, 452);
    public int scores;
    private String type;
    private int life;
    private int speed;
    private int direction;
    private int invulnerableTime;
    private int frozen;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean fire;
    private int numberOfBullet;
    private int coolDownTime;
    private int status;
    private int health;
    private int xPos, yPos, xVPos, yVPos;
    private Rectangle border;
    private final Image standardImage;
    public Image[] textures;
    private final ServerModel gameModel;

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public int getNumberOfBullet() {
        return numberOfBullet;
    }

    public void setNumberOfBullet(int numberOfBullet) {
        this.numberOfBullet = numberOfBullet;
    }

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

    public void setType(String type) {
        this.type = type;
    }

    public int getLife() {
        return life;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public Player(String type, ServerModel gameModel) {
        this.type = type;
        life = 3;
        direction = UP;
        status = 1;
        health = 1;
        numberOfBullet = 1;
        invulnerableTime = 150;
        this.gameModel = gameModel;

        textures = new Image[4];
        if ("1P".equals(type)) {
            //玩家1 游戏开启时位置
            xPos = 198;
            yPos = 498;
            //玩家1 的图像
            System.arraycopy(gameModel.textures, 54, textures, 0, 4);
        } else {
            //玩家2 游戏开启时位置
            xPos = 323;
            yPos = 498;
            //玩家2的图像
            System.arraycopy(gameModel.textures, 72, textures, 0, 4);
        }
        standardImage = textures[0];

        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);

    }

    @Override
    public void move() {
        if (Status.isGamePaused()) {
            writeToOutputLine();
            return;
        }

        if (coolDownTime > 0) {
            coolDownTime--;
        }
        if (invulnerableTime > 0) {
            invulnerableTime--;
        }

        if (frozen == 1) {
            writeToOutputLine();
            return;
        }

        //如果玩家点击“开火”键，并且满足条件，则创建一个子弹目标（即发射子弹）
        int DOWN = 1;
        int LEFT = 2;
        if (fire && coolDownTime == 0 && numberOfBullet > 0) {
            //子弹方向
            int c = direction;
            //子弹位置
            int a, b;
            if (direction == UP) {
                a = xPos;
                b = yPos - size;
            } else if (direction == DOWN) {
                a = xPos;
                b = yPos + size;
            } else if (direction == LEFT) {
                a = xPos - size;
                b = yPos;
            } else {
                a = xPos + size;
                b = yPos;
            }
            //子弹速度
            int d;
            if (status == 1) {
                numberOfBullet = 1;
                d = 7;
            } else {
                d = 12;
            }
            //子弹能力
            int e;
            if (status == 4) {
                e = 2;
            } else {
                e = 1;
            }
            //添加子弹
            gameModel.addActor(new Bullet(a, b, c, d, e, this, gameModel));
            //coolDownTime是你要等到你可以发射第二颗子弹时间（与魔兽争霸3相同）
            if (status > 2) {
                coolDownTime = 5;
            } else {
                coolDownTime = 8;
            }
            //减少子弹的可用数，子弹发射时numberOfBullet会增加
            //由玩家的坦克击中目标（例如，墙壁，敌人坦克等）；
            numberOfBullet--;
        }


        //保存当前位置信息，如果新的移动确定后无效，则更改
        //以前的位置
        int xPosTemp = xPos;
        int yPosTemp = yPos;
        Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25, 25);

        //根据玩家坦克的移动定义玩家坦克的下一个边界，假设它的下一个移动是有效的；
        boolean notMoving = false;
        int RIGHT = 3;
        if (moveUp) {
            if (direction != UP && direction != DOWN) {
                xPos = xVPos;
            }
            yPos -= speed;
            direction = UP;
        } else if (moveDown) {
            if (direction != UP && direction != DOWN) {
                xPos = xVPos;
            }
            yPos += speed;
            direction = DOWN;
        } else if (moveLeft) {
            if (direction != LEFT && direction != RIGHT) {
                yPos = yVPos;
            }
            xPos -= speed;
            direction = LEFT;
        } else if (moveRight) {
            if (direction != LEFT && direction != RIGHT) {
                yPos = yVPos;
            }
            xPos += speed;
            direction = RIGHT;
        } else {
            notMoving = true;
        }
        if (notMoving) {
            if (speed > 0) {
                speed--;
            }
        } else {
            if (speed < 3) {
                speed++;
            }
        }

        //更新边界
        border.y = yPos - size;
        border.x = xPos - size;

        //检查下一个边界是否与地图边界相交，如果不移动到任何地方
        if (!border.intersects(map)) {
            xPos = xVPos;
            yPos = yVPos;
            border.x = xPos - size;
            border.y = yPos - size;
            writeToOutputLine();
            return;
        }


        //检查下个边界是否与其他对象相交，如玩家控制的坦克，墙等等
        for (int i = 0; i < gameModel.gameComponents.length; i++) {
            if (gameModel.gameComponents[i] != null) {
                if (this != gameModel.gameComponents[i]) {
                    if (border.intersects(gameModel.gameComponents[i].getBorder())) {
                        if ("powerUp".equals(gameModel.gameComponents[i].getType())) {
                            scores += 50;
                            PowerUp temp = (PowerUp) gameModel.gameComponents[i];
                            int function = temp.getFunction();
                            if (function == 0) {  //普通星星，增加速度
                                upgrade();
                            } else if (function == 1) {  //钢墙保护基地
                                Base tempe = (Base) gameModel.gameComponents[4];
                                tempe.setSteelWallTime(600);
                            } else if (function == 2) {   // 杀死所有的敌方坦克
                                for (int j = 0; j < gameModel.gameComponents.length; j++) {
                                    if (gameModel.gameComponents[j] != null) {
                                        if ("enemy".equals(gameModel.gameComponents[j].getType())) {
                                            Enemy tempe = (Enemy) gameModel.gameComponents[j];
                                            gameModel.addActor(new Bomb(tempe.getxPos(), tempe.getyPos(), "big", gameModel));
                                            gameModel.removeActor(gameModel.gameComponents[j]);
                                        }
                                    }
                                }
                                Level.setNoOfEnemy(0);;
                                Level.setDeathCount(20 - Level.getEnemyLeft());
                            } else if (function == 3) {   //防护盾，刀枪不入
                                invulnerableTime = 300 + (int) (Math.random() * 400);
                            } else if (function == 4) {  //冻结所有敌人
                                Enemy.setFrozenTime(300 + (int) (Math.random() * 400));
                                Enemy.setFrozenMoment(ServerModel.getGameFlow());
                            } else if (function == 5) { //超级星星
                                if (status < 3) {
                                    numberOfBullet++;
                                }
                                status = 4;
                                health = 2;
                                if ("1P".equals(type)) {
                                    System.arraycopy(gameModel.textures, 66, textures, 0, 4);
                                } else {
                                    System.arraycopy(gameModel.textures, 84, textures, 0, 4);
                                }
                            } else if (function == 6) {  // 增加生命
                                life++;
                            }

                            gameModel.removeActor(gameModel.gameComponents[i]);

                        }
                        //静态对象，如墙壁，河流
                        else if ("steelWall".equals(gameModel.gameComponents[i].getType()) || "wall".equals(gameModel.gameComponents[i].getType())) {
                            if (!gameModel.gameComponents[i].wallDestroyed()) {
                                for (int j = 0; j < gameModel.gameComponents[i].getDetailedBorder().length; j++) {
                                    if (gameModel.gameComponents[i].getDetailedBorder()[j] != null) {
                                        if (gameModel.gameComponents[i].getDetailedBorder()[j].intersects(border)) {
                                            xPos = xVPos;
                                            yPos = yVPos;
                                            border.x = xPos - size;
                                            border.y = yPos - size;
                                            writeToOutputLine();
                                            return;
                                        }
                                    }
                                }
                            }
                        } else if ("river".equals(gameModel.gameComponents[i].getType()) || "base".equals(gameModel.gameComponents[i].getType())) {
                            xPos = xVPos;
                            yPos = yVPos;
                            border.x = xPos - size;
                            border.y = yPos - size;
                            writeToOutputLine();
                            return;
                        }
                        //移动对象，例如敌人坦克
                        else if ("enemy".equals(gameModel.gameComponents[i].getType()) || "Player".equals(gameModel.gameComponents[i].getType())) {
                            if (!borderTemp.intersects(gameModel.gameComponents[i].getBorder()) || "enemy".equals(gameModel.gameComponents[i].getType())) {
                                xPos = xPosTemp;
                                yPos = yPosTemp;
                                border.x = xPos - size;
                                border.y = yPos - size;
                                writeToOutputLine();
                                return;
                            }
                        }
                    }
                }
            }
        }

        //找到坦克的虚拟位置，当90度转弯时，虚拟位置用来调整坦克的真实位置。
        int a = (xPos - 10) / 25;
        int b = (xPos - 10) % 25;
        if (b < 7) {
            b = 0;
        }
        if (b > 18) {
            b = 25;
        }
        if ((b < 19 && b > 6) || xPos < 17 || xPos > 492) {
            b = 13;
        }
        xVPos = a * 25 + b + 10;
        int c = (yPos - 10) / 25;
        int d = (yPos - 10) % 25;
        if (d < 7) {
            d = 0;
        }
        if (d > 18) {
            d = 25;
        }
        if ((d < 19 && d > 6) || yPos < 17 || yPos > 492) {
            d = 13;
        }
        yVPos = c * 25 + d + 10;

        writeToOutputLine();
    }

    public void writeToOutputLine() {
        //将变化写入输出行
        gameModel.outputLine += "n" + xPos + "," + yPos + ",";
        int textureIndex;
        if ("1P".equals(type)) {
            if (status == 1) {
                textureIndex = 54 + direction;
            } else if (status == 2) {
                textureIndex = 58 + direction;
            } else if (status == 3) {
                textureIndex = 62 + direction;
            } else {
                textureIndex = 66 + direction;
            }
        } else {
            if (status == 1) {
                textureIndex = 72 + direction;
            } else if (status == 2) {
                textureIndex = 76 + direction;
            } else if (status == 3) {
                textureIndex = 80 + direction;
            } else {
                textureIndex = 84 + direction;
            }
        }


        gameModel.outputLine += "" + textureIndex + ";";

        if (invulnerableTime > 0) {
            gameModel.outputLine += "i" + xPos + "," + yPos + ";";
        }
    }

    @Override
    public void draw(Graphics g) {
        //绘制玩家坦克
        g.drawImage(textures[direction], xPos - size, yPos - size, null);
        if (invulnerableTime > 0) {
            g.setColor(Color.red);
            g.drawRect(xPos - 12, yPos - 12, 25, 25);
            g.drawRect(xPos - 11, yPos - 11, 23, 23);
        }

        //关于玩家的信息，如分数，生命等
        if ("1P".equals(type)) {
            g.setColor(Color.yellow);
            g.drawImage(standardImage, 520, 380, null);
            g.drawString("x", 555, 395);
            g.drawString(life + "", 565, 396);
            String SCORE = "000000000" + scores;
            g.drawString(type + " 得分:" + "", 515, 370);
            g.drawString(SCORE.substring(SCORE.length() - 7) + "", 566, 370);
        }
        if ("2P".equals(type)) {
            g.setColor(Color.green);
            g.drawImage(standardImage, 520, 460, null);
            g.drawString("x", 555, 475);
            g.drawString(life + "", 565, 476);
            String SCORE = "000000000" + scores;
            g.drawString(type + " 得分:" + "", 515, 450);
            g.drawString(SCORE.substring(SCORE.length() - 7) + "", 566, 450);
        }


    }

    @Override
    public Rectangle getBorder() {
        return border;
    }

    @Override
    public String getType() {
        return "Player";
    }

    public void hurt() {
        if (invulnerableTime != 0) {
            return;
        }

        //如果坦克只有1级的健康状态，被击中，那么玩家坦克失去一个生命，如果玩家坦克是最后一次生命，被击中，则game over
        //只有吃掉超级星星时，玩家才会有2级的生命健康状态
        if (health == 1) {
            gameModel.addActor(new Bomb(xPos, yPos, "big", gameModel));
            life--;
            if (life == 0) {
                xPos = 100000;
                yPos = 100000;           //this will make the player never come back to the main screen, thus looks like "dead"
                border = new Rectangle(xPos - size, yPos - size, 25, 25);
                xVPos = xPos;
                yVPos = yPos;
            } else {
                direction = UP;
                status = 1;
                health = 1;
                numberOfBullet = 1;
                invulnerableTime = 150;
                if ("1P".equals(type)) {
                    xPos = 198;
                    yPos = 498;
                    border = new Rectangle(xPos - size, yPos - size, 25, 25);
                    xVPos = xPos;
                    yVPos = yPos;
                    System.arraycopy(gameModel.textures, 54, textures, 0, 4);
                } else {
                    xPos = 323;
                    yPos = 498;
                    border = new Rectangle(xPos - size, yPos - size, 25, 25);
                    xVPos = xPos;
                    yVPos = yPos;
                    System.arraycopy(gameModel.textures, 72, textures, 0, 4);
                }
            }
        } else {
            health--;
            status = 3;
            if ("1P".equals(type)) {
                System.arraycopy(gameModel.textures, 62, textures, 0, 4);
            } else {
                System.arraycopy(gameModel.textures, 80, textures, 0, 4);
            }
        }
    }

    public void upgrade() {
        //当玩家坦克吃掉正常的星星时，他的子弹将会升级
        if ("1P".equals(type)) {
            if (status == 1) {
                status = 2;
                System.arraycopy(gameModel.textures, 58, textures, 0, 4);
            } else if (status == 2) {
                status = 3;
                numberOfBullet++;
                System.arraycopy(gameModel.textures, 62, textures, 0, 4);
            } else if (status == 3) {
                status = 4;
                System.arraycopy(gameModel.textures, 66, textures, 0, 4);
            }
        } else {
            if (status == 1) {
                status = 2;
                System.arraycopy(gameModel.textures, 76, textures, 0, 4);
            } else if (status == 2) {
                status = 3;
                numberOfBullet++;
                System.arraycopy(gameModel.textures, 80, textures, 0, 4);
            } else if (status == 3) {
                status = 4;
                System.arraycopy(gameModel.textures, 84, textures, 0, 4);
            }
        }
    }

    public void reset() {
        direction = UP;
        invulnerableTime = 150;
        if ("1P".equals(type)) {
            xPos = 198;
        } else {
            xPos = 323;
        }
        yPos = 498;

        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);
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
