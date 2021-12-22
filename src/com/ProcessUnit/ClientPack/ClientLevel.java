package com.ProcessUnit.ClientPack;//服务器端的level类
//因为只有一层对象,所以在这个类是一个静态变量

import com.ProcessUnit.level.Level;
import com.SourceUnit.ClientPack.ClientBrickWall;
import com.SourceUnit.ClientPack.ClientNormalObject;
import com.SourceUnit.ClientPack.ClientSteelWall;
import com.UI.ClientDrawingPanel;

import java.awt.*;

/**
 * @author chenhong
 */
public class ClientLevel extends Level {
    private static int winningCount;

    public static Image[] textures;

    public static int getWinningCount() {
        return winningCount;
    }

    public static void setWinningCount(int winningCount) {
        ClientLevel.winningCount = winningCount;
    }

    public static void loadLevel(int levelIndex) {
        //清除所有的东西
        int a = 400;
        for (int i = 0; i < a; i++) {
            ClientDrawingPanel.drawingList[i] = null;
        }

        //加载基地
        ClientDrawingPanel.drawingList[0] = new ClientBrickWall(248, 498, 2);
        ClientDrawingPanel.drawingList[1] = new ClientBrickWall(273, 498, 3);
        ClientDrawingPanel.drawingList[2] = new ClientBrickWall(248, 473, 1);
        ClientDrawingPanel.drawingList[3] = new ClientBrickWall(273, 473, 1);
        ClientDrawingPanel.drawingList[4] = new ClientNormalObject(260, 498, "base", 0);

        //加载一个级别
        int choiceMap = 1 + (levelIndex - 1) % 8;
        switch (choiceMap){
            case 1: {
                String[] level = readArray("maps\\level1.txt");
                loadLevel(level);
                break;
            }
            case 2: {
                String[] level = readArray("maps\\level2.txt");
                loadLevel(level);
                break;
            }
            case 3: {
                String[] level = readArray("maps\\level3.txt");
                loadLevel(level);
                break;
            }
            case 4: {
                String[] level = readArray("maps\\level4.txt");
                loadLevel(level);
                break;
            }
            case 5: {
                String[] level = readArray("maps\\level5.txt");
                loadLevel(level);
                break;
            }
            case 6: {
                String[] level = readArray("maps\\level6.txt");
                loadLevel(level);
                break;
            }
            case 7: {
                String[] level = readArray("maps\\level7.txt");
                loadLevel(level);
                break;
            }
            case 8: {
                String[] level = readArray("maps\\level8.txt");
                loadLevel(level);
                break;
            }
            default:break;
        }
    }

    public static void loadLevel(String[] level) {
        for (int i = 0; i < level.length; i++) {
            String type = level[i];
            switch (type){
                case "wall":{
                    ClientDrawingPanel.addActor(new ClientBrickWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 4));
                    break;
                }
                case "wall0":{
                    ClientDrawingPanel.addActor(new ClientBrickWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "wall1":{
                    ClientDrawingPanel.addActor(new ClientBrickWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "wall2":{
                    ClientDrawingPanel.addActor(new ClientBrickWall(23 + (i % 19) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "wall3":{
                    ClientDrawingPanel.addActor(new ClientBrickWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                case "steel":{
                    ClientDrawingPanel.addActor(new ClientSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 4));
                    break;
                }
                case "steel0":{
                    ClientDrawingPanel.addActor(new ClientSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "steel1":{
                    ClientDrawingPanel.addActor(new ClientSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "steel2":{
                    ClientDrawingPanel.addActor(new ClientSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "steel3":{
                    ClientDrawingPanel.addActor(new ClientSteelWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                default:break;
            }
            if ("grass".equals(level[i])) {
                int a = 399;
                for (int j = a; j >= 0; j--) {
                    if (ClientDrawingPanel.drawingList[j] == null) {
                        ClientDrawingPanel.drawingList[j] = new ClientNormalObject(23 + (i % 20) * 25, 23 + (i / 20) * 25,  "grass", -1);
                        break;
                    }
                }
            }
            if ("river".equals(level[i])) {
                ClientDrawingPanel.addActor(new ClientNormalObject(23 + (i % 20) * 25, 23 + (i / 20) * 25, "river", 71));
            }
        }
    }

    public static void loadPictures() {
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++) {
            textures[i - 1] = Toolkit.getDefaultToolkit().
                    getImage("image\\" + i + ".jpg");
        }
    }
}