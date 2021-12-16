package com.server.ConponentPack;

import com.server.ServerUnit.Level;
import com.server.ServerUnit.ServerModel;

import java.awt.*;

public class Enemy implements Actor {
    private static int frozenTime;
    private static int frozenMoment;
    private final int UP = 0;
    private final int DOWN = 1;
    private final int LEFT = 2;
    private final int size = 12;
    private final Rectangle map = new Rectangle(35, 35, 452, 452);
    private int numberOfBullet;
    private int coolDownTime;
    private int type;
    private int speed;
    private int direction;
    private int interval;
    private int health;
    private int xPos, yPos, xVPos, yVPos;
    private Rectangle border;
    private boolean flashing;
    private double firePossibility;
    public Image[] textures;
    private ServerModel gameModel;


    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    
    public int getsType() {
        return type;
    }

    public static void setFrozenTime(int frozenTime) {
        Enemy.frozenTime = frozenTime;
    }

    public static void setFrozenMoment(int frozenMoment) {
        Enemy.frozenMoment = frozenMoment;
    }


    public int getNumberOfBullet() {
        return numberOfBullet;
    }

    public void setNumberOfBullet(int numberOfBullet) {
        this.numberOfBullet = numberOfBullet;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }


    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public Enemy(int type, boolean flashing, int xPos, int yPos, ServerModel gameModel) {
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.flashing = flashing;
        this.gameModel = gameModel;

        //设置全部敌人的共同属性
        interval = (int) (Math.random() * 200);
        direction = (int) (Math.random() * 4);
        numberOfBullet = 1;
        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);


        //根据不同类型的敌人设置独特的属性如：容貌,速度,等等
        if (type == 1) {
            firePossibility = 0.95;
            speed = 2;
            textures = new Image[8];
            System.arraycopy(gameModel.textures, 38, textures, 0, 8);
        } else if (type == 2) {
            firePossibility = 0.95;
            speed = 4;
            textures = new Image[8];
            System.arraycopy(gameModel.textures, 2, textures, 0, 8);
        } else if (type == 3) {
            firePossibility = 0.9;
            speed = 2;
            textures = new Image[8];
            System.arraycopy(gameModel.textures, 10, textures, 0, 8);
        } else {
            firePossibility = 0.95;
            health = 3;
            speed = 2;
            textures = new Image[20];
            System.arraycopy(gameModel.textures, 18, textures, 0, 20);

        }

    }

    @Override
    public void move() {
        if (gameModel.isGamePaused()) {
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
                    xPos = xVPos;
                    yPos = yVPos;
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
        if (Math.random() > firePossibility && coolDownTime == 0 && numberOfBullet > 0) {
            //获得子弹方向
            int c = direction;
            //获得子弹位置
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
            //获得子弹速度
            int d;
            if (type == 3) {
                d = 12;
            } else {
                d = 7;
            }
            //添加子弹
            gameModel.addActor(new Bullet(a, b, c, d, 1, this, gameModel));
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
        if (direction == UP) {
            yPos -= speed;
        } else if (direction == DOWN) {
            yPos += speed;
        } else if (direction == LEFT) {
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
            xPos = xVPos;
            yPos = yVPos;
            border.x = xPos - size;
            border.y = yPos - size;
            writeToOutputLine();
            return;
        }

        //检查下一个边界是否与其他对象相交，例如玩家控制的坦克，墙等等
        for (int i = 0; i < gameModel.actors.length; i++) {
            if (gameModel.actors[i] != null) {
                if (this != gameModel.actors[i]) {
                    if (border.intersects(gameModel.actors[i].getBorder())) {
                        //静态对象，例如河流，墙等等
                        if ("steelWall".equals(gameModel.actors[i].getType()) ||
                                "wall".equals(gameModel.actors[i].getType())) {
                            if (!gameModel.actors[i].wallDestroyed()) {
                                for (int j = 0; j < gameModel.actors[i].getDetailedBorder().length; j++) {
                                    if (gameModel.actors[i].getDetailedBorder()[j] != null) {
                                        if (gameModel.actors[i].getDetailedBorder()[j].intersects(border)) {
                                            if (Math.random() > 0.90) {
                                                direction = (int) (Math.random() * 4);
                                            }
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
                        } else if ("river".equals(gameModel.actors[i].getType()) ||
                                "base".equals(gameModel.actors[i].getType())) {
                            if (Math.random() > 0.90) {
                                direction = (int) (Math.random() * 4);
                            }
                            xPos = xVPos;
                            yPos = yVPos;
                            border.x = xPos - size;
                            border.y = yPos - size;
                            writeToOutputLine();
                            return;
                        }
                        //其他对象，其他的坦克
                        if ("Player".equals(gameModel.actors[i].getType()) ||
                                "enemy".equals(gameModel.actors[i].getType())) {
                            if (!borderTemp.intersects(gameModel.actors[i].getBorder())) {
                                xPos = xPosTemp;
                                yPos = yPosTemp;
                                border.x = xPos - size;
                                border.y = yPos - size;
                                int newDirection = (int) (Math.random() * 4);
                                if (direction != newDirection) {
                                    if (direction / 2 != newDirection / 2) {
                                        xPos = xVPos;
                                        yPos = yVPos;
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
        gameModel.outputLine += "" + textureIndex + ";";

    }

    //如果敌方坦克打出一颗子弹，判断会发生什么
    public void hurt() {
        if (flashing) {
            gameModel.addActor(new PowerUp(gameModel));
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

            int noOfEnemy = Level.getNoOfEnemy();
            noOfEnemy--;
            Level.setNoOfEnemy(noOfEnemy);

            int deathCount = Level.getDeathCount();
            deathCount++;
            Level.setDeathCount(deathCount);
            gameModel.removeActor(this);
            gameModel.addActor(new Bomb(xPos, yPos, "big", gameModel));
        }
    }

    @Override
    public String getType() {
        return "enemy";
    }

    @Override
    public void draw(Graphics g) {
        if (flashing && ServerModel.getGameFlow() % 10 > 4) {
            g.drawImage(textures[textures.length - 4 + direction], xPos - size, yPos - size, null);
        } else {
            g.drawImage(textures[direction], xPos - size, yPos - size, null);
        }
    }

    @Override
    public Rectangle getBorder() {
        return border;
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