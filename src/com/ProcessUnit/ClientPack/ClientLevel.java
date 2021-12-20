package com.ProcessUnit.ClientPack;//服务器端的level类
//因为只有一层对象,所以在这个类是一个静态变量

import com.SourceUnit.ClientPack.ClientBrickClientWall;
import com.SourceUnit.ClientPack.ClientNormalObject;
import com.SourceUnit.ClientPack.ClientSteelClientWall;
import com.UI.ClientDrawingPanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhong
 */
public class ClientLevel {
    private static int winningCount;

    //textures
    public static Image[] textures;

    public static int getWinningCount() {
        return winningCount;
    }

    public static void setWinningCount(int winningCount) {
        ClientLevel.winningCount = winningCount;
    }

    public static String[] readArray(String choice){

        //声明字符输入流
        FileReader reader = null;
        //声明字符输入缓冲流
        BufferedReader readerBuf = null;
        //声明二维数组
        String[] array = null;

        try {
            //指定读取路径
            reader = new FileReader(choice);
            //通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //存放一行的数据
            String lineStr;
            //逐行读取文件
            while((lineStr = readerBuf.readLine()) != null) {
                //读取的行添加到list中
                strList.add(lineStr);
            }
            //获取文件行
            int lineNum = strList.size();
            //获取数组列
            String s =  strList.get(0);

            int columnNum = s.split(",").length;
            //根据文件行数创建对应的数组
            int num= lineNum * columnNum;
            array = new String[num];

            int j = 0;
            //循环遍历集合，将集合中的数据放入数组中
            for (String value : strList) {
                // 将读取的str按照","分割，用字符串数组来接收
                String[] strings = value.split(",");

                for (String str : strings) {
                    array[j] = str;
                    j++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭字符输入流
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //关闭字符输入缓冲流
            try {
                if(readerBuf != null) {
                    readerBuf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
    public static void loadLevel( int levelIndex) {
        //清除所有的东西
        for (int i = 0; i < 400; i++) {
            ClientDrawingPanel.drawingList[i] = null;
        }

        //加载基地
        ClientDrawingPanel.drawingList[0] = new ClientBrickClientWall(248, 498, 2);
        ClientDrawingPanel.drawingList[1] = new ClientBrickClientWall(273, 498, 3);
        ClientDrawingPanel.drawingList[2] = new ClientBrickClientWall(248, 473, 1);
        ClientDrawingPanel.drawingList[3] = new ClientBrickClientWall(273, 473, 1);
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
                    ClientDrawingPanel.addActor(new ClientBrickClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 4));
                    break;
                }
                case "wall0":{
                    ClientDrawingPanel.addActor(new ClientBrickClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "wall1":{
                    ClientDrawingPanel.addActor(new ClientBrickClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "wall2":{
                    ClientDrawingPanel.addActor(new ClientBrickClientWall(23 + (i % 19) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "wall3":{
                    ClientDrawingPanel.addActor(new ClientBrickClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                case "steel":{
                    ClientDrawingPanel.addActor(new ClientSteelClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 4));
                    break;
                }
                case "steel0":{
                    ClientDrawingPanel.addActor(new ClientSteelClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 0));
                    break;
                }
                case "steel1":{
                    ClientDrawingPanel.addActor(new ClientSteelClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 1));
                    break;
                }
                case "steel2":{
                    ClientDrawingPanel.addActor(new ClientSteelClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 2));
                    break;
                }
                case "steel3":{
                    ClientDrawingPanel.addActor(new ClientSteelClientWall(23 + (i % 20) * 25, 23 + (i / 20) * 25, 3));
                    break;
                }
                default:break;
            }
            if ("grass".equals(level[i])) {
                for (int j = 399; j >= 0; j--) {
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