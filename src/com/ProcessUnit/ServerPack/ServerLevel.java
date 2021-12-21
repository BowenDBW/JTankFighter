package com.ProcessUnit.ServerPack;

import com.ProcessUnit.level.Level;
import com.SourceUnit.ServerPack.*;
import com.UI.ServerDrawingPanel;


/**
 * @author chenhong
 */

public class ServerLevel extends Level {
    private static int currentLevel = 0;
    private static int enemySpawnTime = 150;
    private static int enemyLeft = 20;
    private static int deathCount = 0;
    private static int maxNoEnemy = 3;
    private static int NoOfEnemy = 0;
    public static int[] enemySequence;

    /**
     *  制作获胜场景所需的变量
     */
    private static int winningCount;

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static int getEnemyLeft() {
        return enemyLeft;
    }

    public static int getDeathCount() {
        return deathCount;
    }

    public static void setDeathCount(int deathCount) {
        ServerLevel.deathCount = deathCount;
    }

    public static int getNoOfEnemy() {
        return NoOfEnemy;
    }

    public static void setNoOfEnemy(int noOfEnemy) {
        NoOfEnemy = noOfEnemy;
    }

    public static int getWinningCount() {
        return winningCount;
    }

    public static void setWinningCount(int winningCount) {
        ServerLevel.winningCount = winningCount;
    }

    public static void loadLevel() {
        //增加关卡数量
        currentLevel++;

        //每次加载一个新的关卡将增加难度
        int a1 = 30;
        if (enemySpawnTime > a1) {
            enemySpawnTime -= 10;
        }
        if (maxNoEnemy < 10 && (currentLevel % 2 == 0)) {
            maxNoEnemy++;
        }

        //从上个关卡清除所有东西
        for (int i = 0; i < 400; i++) {
            ServerDrawingPanel.serverGameComponents[i] = null;
        }

        //启动时各关卡共享
        enemyLeft = 20;

        //加载基地，每个关卡都一样
        ServerDrawingPanel.serverGameComponents[0] = new ServerWall(248, 498);
        ServerDrawingPanel.serverGameComponents[1] = new ServerWall(273, 498, 3);
        ServerDrawingPanel.serverGameComponents[2] = new ServerWall(248, 473, 1);
        ServerDrawingPanel.serverGameComponents[3] = new ServerWall(273, 473, 1);
        ServerDrawingPanel.serverGameComponents[4] = new ServerBase();

        //加载一个关卡
        int choiceLevel = 1 + (currentLevel - 1) % 8;
        switch (choiceLevel) {
            case 1:{
                enemySequence = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2};
                String[] level = readArray("maps\\level1.txt");
                loadLevel(level);
                break;
            }
            case 2: {
                enemySequence = new int[]{1, 1, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 3, 3, 3, 3};
                String[] level = readArray("maps\\level2.txt");
                loadLevel(level);
                break;
            }
            case 3:{
                enemySequence = new int[]{1, 1, 1, 2, 2, 2, 4, 4, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 4, 4};
                String[] level = readArray("maps\\level3.txt");
                loadLevel(level);
                break;
            }
            case 4:{
                enemySequence = new int[]{3, 3, 3, 3, 2, 2, 2, 3, 3, 1, 1, 1, 3, 3, 3, 1, 1, 4, 4, 4};
                String[] level = readArray("maps\\level4.txt");
                loadLevel(level);
                break;
            }
            case 5:{
                enemySequence = new int[]{2, 2, 2, 3, 3, 3, 2, 2, 2, 4, 4, 4, 3, 3, 3, 3, 3, 2, 2, 2};
                String[] level = readArray("maps\\level5.txt");
                loadLevel(level);
                break;
            }
            case 6:{
                enemySequence = new int[]{4, 4, 4, 4, 2, 2, 2, 4, 4, 1, 1, 1, 3, 3, 3, 1, 1, 4, 4, 4};
                String[] level = readArray("maps\\level6.txt");
                loadLevel(level);
                break;
            }
            case 7:{
                enemySequence = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4};
                String[] level = readArray("maps\\level7.txt");
                loadLevel(level);
                break;
            }
            case 8:{
                enemySequence = new int[]{3, 4, 4, 2, 3, 4, 4, 2, 3, 4, 4, 2, 3, 4, 4, 2, 3, 4, 4, 2};
                String[] level = readArray("maps\\level8.txt");
                loadLevel(level);
                break;
            }
            default:break;
        }
            ServerDrawingPanel.addActor(ServerModel.getP1());
            ServerDrawingPanel.addActor(ServerModel.getP2());
    }

    public static void loadLevel(String[] level) {
        for (int i = 0; i < level.length; i++) {
            String type = level[i];
            switch (type){
                case "wall":{
                    ServerDrawingPanel.addActor(new ServerWall(23 + (i % 20) * 25, 23 + (i / 20) * 25));
                    break;
                }
                case "wall0":{
                    ServerDrawingPanel.addActor(new ServerWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "wall1":{
                    ServerDrawingPanel.addActor(new ServerWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "wall2":{
                    ServerDrawingPanel.addActor(new ServerWall(23 + (i % 19) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "wall3":{
                    ServerDrawingPanel.addActor(new ServerWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                case "steel":{
                    ServerDrawingPanel.addActor(new ServerSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25));
                    break;
                }
                case "steel0":{
                    ServerDrawingPanel.addActor(new ServerSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "steel1":{
                    ServerDrawingPanel.addActor(new ServerSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "steel2":{
                    ServerDrawingPanel.addActor(new ServerSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "steel3":{
                    ServerDrawingPanel.addActor(new ServerSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                default:break;
            }
            if ("grass".equals(level[i])) {
                int a = 399;
                for (int j = a; j >= 0; j--) {
                    if (ServerDrawingPanel.serverGameComponents[j] == null) {
                        ServerDrawingPanel.serverGameComponents[j] = new ServerGrass(23 + (i % 20) * 25, 23 + (i / 20) * 25);
                        break;
                    }
                }
            }
            if ("river".equals(level[i])) {
                ServerDrawingPanel.addActor(new ServerRiver(23 + (i % 20) * 25, 23 + (i / 20) * 25));
            }
        }
    }

    /**
     * <p>
     *
     * </p>
     */
    public static void spawnEnemy() {
        if (NoOfEnemy < maxNoEnemy && enemyLeft > 0 && (ServerModel.getGameFlow() % enemySpawnTime == 0)) {
            int xPos = 23 + (20 - enemyLeft) % 3 * 238;
            boolean flashing = (enemyLeft % 3 == 0);
            ServerDrawingPanel.addActor(new ServerEnemy(enemySequence[20 - enemyLeft], flashing, xPos, 23));
            enemyLeft--;
            NoOfEnemy++;
        }
    }

    /**
     * <p>
     *     reset all the value when the game restart
     * </p>
     *
     * <p>
     *     用于重新初始化所有数据当一局游戏重开时
     * </p>
     */
    public static void reset() {
        currentLevel = 0;
        enemySpawnTime = 150;
        enemyLeft = 20;
        deathCount = 0;
        maxNoEnemy = 3;
        NoOfEnemy = 0;
    }
}