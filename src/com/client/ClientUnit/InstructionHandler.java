package com.client.ClientUnit;//该类从服务器程序解码指令字符串,然后将字符串转换为真正的指令

import com.client.ConponentPack.*;

/**
 * @author chenhong
 * 由客户端程序可读
 */
public class InstructionHandler {
    public static void handleInstruction(ClientModel gameModel, String instruction) {
        if (instruction.length() == 0) {
            return;
        }

        int i = 0;
        while (i < instruction.length()) {
            StringBuilder perInstruction = new StringBuilder();

            //指令由”;“开头在instruction-string分离
            while (instruction.charAt(i) != ';') {
                perInstruction.append(instruction.charAt(i));
                i++;
            }

            //指令“L”开头是负载水平,其次是“L”数量水平指数
            if ("L".equals(perInstruction.substring(0, 1))) {
                Level.loadLevel(gameModel, Integer.parseInt(perInstruction.substring(1, 2)));
                return;
            }

            //指令“w”开头意味着一些事情改变了在墙上的对象
            if ("w".equals(perInstruction.substring(0, 1))) {
                int xPos;
                int yPos;
                boolean[] shape = new boolean[16];
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x的位置
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y的位置
                temp = new StringBuilder();
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                yPos = Integer.parseInt(temp.toString());

                //墙的详细的边界
                for (int k = 0; k < 16; k++) {

                    shape[k] = "1".equals(perInstruction.substring(j, j + 1));
                    j++;
                }

                //执行指令
                for (int k = 0; k < gameModel.getDrawingList().length; k++) {
                    if (gameModel.getDrawingList(k) != null) {
                        if (gameModel.getDrawingList(k).getXPos() == xPos && gameModel.getDrawingList(k).getYPos() == yPos) {
                            Wall tempWall = new Wall(xPos, yPos, 4);
                            tempWall.shape = shape;
                            gameModel.setDrawingList(k, tempWall);
                        }
                    }
                }
            }

            //指令“s”开头意味着一些事情改变了一个铁墙对象
            if ("s".equals(perInstruction.substring(0, 1))) {
                int xPos;
                int yPos;
                boolean[] shape = new boolean[4];
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x的位置
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y的位置
                temp = new StringBuilder();
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                yPos = Integer.parseInt(temp.toString());

                //详细的钢墙边境
                for (int k = 0; k < 4; k++) {
                    shape[k] = "1".equals(perInstruction.substring(j, j + 1));
                    j++;
                }

                //执行指令
                for (int k = 0; k < gameModel.getDrawingList().length; k++) {
                    if (gameModel.getDrawingList()[k] != null) {
                        if (gameModel.getDrawingList()[k].getXPos() == xPos && gameModel.getDrawingList()[k].getYPos() == yPos) {
                            SteelWall tempWall = new SteelWall(xPos, yPos, 4, gameModel);
                            tempWall.shape = shape;
                            gameModel.getDrawingList()[k] = tempWall;
                        }
                    }
                }
            }

            //指令“b”开头意味着基地已被摧毁
            if ("b".equals(perInstruction.substring(0, 1))) {
                Actor actor = new NormalObject(260, 498, gameModel, "base", 1);
                gameModel.setDrawingList(4, actor);
            }

            //指令“n”开头显示正常的对象,如坦克、启动符号
            if ("n".equals(perInstruction.substring(0, 1))) {
                int xPos;
                int yPos;
                int textureIndex;
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x对象的位置
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y对象的位置
                temp = new StringBuilder();
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                yPos = Integer.parseInt(temp.toString());

                //获得对象的纹理指数
                temp = new StringBuilder();
                while (j < perInstruction.length()) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                textureIndex = Integer.parseInt(temp.toString());

                //执行指令
                gameModel.addActor(new NormalObject(xPos, yPos, gameModel, "normal", textureIndex));
            }


            //指令“t”开头表明子弹
            if ("t".equals(perInstruction.substring(0, 1))) {
                int xPos;
                int yPos;
                int direction;
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x子弹的位置
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y子弹的位置
                temp = new StringBuilder();
                while (!",".equals(perInstruction.substring(j, j + 1))) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                yPos = Integer.parseInt(temp.toString());

                //子弹的方向
                temp = new StringBuilder();
                while (j < perInstruction.length()) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                direction = Integer.parseInt(temp.toString());

                //执行指令
                gameModel.addActor(new Bullet(xPos, yPos, gameModel, direction));
            }

            //指令“o”开头表示一个炸弹
            if (perInstruction.charAt(0) == 'o') {
                int xPos;
                int yPos;
                int size;
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x炸弹的位置
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y炸弹的位置
                temp = new StringBuilder();
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                yPos = Integer.parseInt(temp.toString());

                //炸弹的大小
                temp = new StringBuilder();
                while (j < perInstruction.length()) {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                if ("small".equals(temp.toString())) {
                    size = 1;
                } else {
                    size = 0;
                }
                //执行指令
                gameModel.addActor(new Bomb(xPos, yPos, size, gameModel));
            }

            //指令“i”开头表明坦克盾牌
            if (perInstruction.charAt(0) == 'i') {
                int xPos;
                int yPos;
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到x位置的盾牌
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                j++;
                xPos = Integer.parseInt(temp.toString());

                //得到y位置的盾牌
                temp = new StringBuilder();
                while (j < perInstruction.length()) {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                yPos = Integer.parseInt(temp.toString());

                //执行指令
                gameModel.addActor(new Shield(xPos, yPos, gameModel));
            }

            //指令“p”开头表示水平和玩家信息
            if (perInstruction.charAt(0) == 'p') {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到敌人离开的数量
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                j++;
                gameModel.getView().getMainPanel().setEnemyLeft(Integer.parseInt(temp.toString()));

                //得到水平指数
                temp = new StringBuilder();
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                j++;
                gameModel.getView().getMainPanel().setLevelIndex(Integer.parseInt(temp.toString()));

                //玩家1的生命量
                temp = new StringBuilder();
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                j++;
                gameModel.getView().getMainPanel().setP1Life(Integer.parseInt(temp.toString()));

                //玩家1的分数
                temp = new StringBuilder();
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                j++;
                gameModel.getView().getMainPanel().setP1Score(Integer.parseInt(temp.toString()));

                //玩家2的生命量
                temp = new StringBuilder();
                while (perInstruction.charAt(j) != ',') {
                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                j++;
                gameModel.getView().getMainPanel().setP2Life(Integer.parseInt(temp.toString()));

                //玩家2的分数
                temp = new StringBuilder();
                while (j < perInstruction.length()) {

                    temp.append(perInstruction.charAt(j));
                    j++;
                }
                gameModel.getView().getMainPanel().setP2Score(Integer.parseInt(temp.toString()));
            }

            //指令“g”开头表明获取胜利的统计数量
            if (perInstruction.charAt(0) == 'g') {
                StringBuilder temp = new StringBuilder();
                int j = 1;
                //得到敌人离开的数量
                while (j < perInstruction.length()) {
                    temp.append(perInstruction.substring(j, j + 1));
                    j++;
                }
                Level.setWinningCount(Integer.parseInt(temp.toString()));
            }

            //指令“m”开头表示服务器玩家的信息
            if (perInstruction.charAt(0) == 'm') {
                gameModel.addMessage("主机端玩家说：" + perInstruction.substring(1, perInstruction.length()));
            }

            //指令“a”开头表示游戏结束
            if (perInstruction.charAt(0) == 'a') {
                if (!Status.isGameOver()) {
                    gameModel.addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                    Status.setGameOver(true);
                }
            }
            //指令“j”开头表示服务器玩家想在玩一次
            if (perInstruction.charAt(0) == 'j') {
                if (Status.isGameOver()) {
                    Status.setServerVote(true);
                }
            }

            //指令“x”开头表示服务器玩家暂停游戏
            if (perInstruction.charAt(0) == 'x') {
                int temp = Integer.parseInt(perInstruction.substring(1, 2));
                if (temp == 0) {
                    if (Status.isGamePaused()) {
                        gameModel.addMessage("主机端玩家取消了暂停");
                        Status.setGamePaused(false);
                    }
                } else {
                    if (!Status.isGamePaused()) {
                        gameModel.addMessage("主机端玩家暂停了游戏");
                        Status.setGamePaused(true);
                    }
                }
            }
            i++;
        }
    }
}