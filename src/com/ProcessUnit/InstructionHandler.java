package com.ProcessUnit;//该类从服务器程序解码指令字符串,然后将字符串转换为真正的指令

import com.ProcessUnit.ClientPack.ClientLevel;
import com.ProcessUnit.ClientPack.ClientModel;
import com.ProcessUnit.ClientPack.ClientStatus;
import com.SourceUnit.ClientPack.*;
import com.UI.ClientDrawingPanel;

/**
 * @author chenhong
 * 由客户端程序可读
 */
@SuppressWarnings("all")
public class InstructionHandler {

    public static void handleInstruction(String instruction) {

        if (instruction.length() == 0) {
            return;
        }

        for (int i = 0;i < instruction.length();i++){
            commandProcess(instruction, i);
        }
    }

    public static void commandProcess(String instruction, int i) {
        StringBuilder perInstruction = new StringBuilder();

        //指令由”;“开头在instruction-string分离
        while (instruction.charAt(i) != CommandTable.COMMAND_SPLIT.charAt(0)) {
            perInstruction.append(instruction.charAt(i));
            i++;
        }

        //指令“L”开头是负载水平,其次是“L”数量水平指数
        if (CommandTable.LOAD_SIGN.equals(perInstruction.substring(0, 1))) {
            ClientLevel.loadLevel(Integer.parseInt(perInstruction.substring(1, 2)));
            return;
        }

        //指令“w”开头意味着一些事情改变了在墙上的对象
        if (CommandTable.BASE_LOCK.equals(perInstruction.substring(0, 1))) {

            baseLock(perInstruction);
        }

        //指令“s”开头意味着一些事情改变了一个铁墙对象
        if (CommandTable.BASE_UNLOCK.equals(perInstruction.substring(0, 1))) {
            baseUnlock(perInstruction);
        }

        //指令“b”开头意味着基地已被摧毁
        boolean isBaseDestroyed = CommandTable.BASE_DESTROYED.equals(perInstruction.substring(0, 1));
        if (isBaseDestroyed) {

            ClientGameComponent actor = new ClientNormalObject(260, 498,  "base", 1);
            ClientDrawingPanel.drawingList[4] = actor;
        }

        //指令“n”开头显示正常的对象,如坦克、启动符号
        boolean isObject = CommandTable.OBJECT_STATE.equals(perInstruction.substring(0, 1));
        if (isObject) {

            objectAction(perInstruction);
        }


        //指令“t”开头表明子弹
        if (CommandTable.BULLET_STATE.equals(perInstruction.substring(0, 1))) {

            bulletAction(perInstruction);
        }

        //指令“o”开头表示一个炸弹
        if (perInstruction.charAt(0) == CommandTable.BOMB_STATE.charAt(0)) {

            bombAction(perInstruction);
        }

        //指令“i”开头表明坦克盾牌
        if (perInstruction.charAt(0) == CommandTable.SHIELD_STATE.charAt(0)) {

            shieldAction(perInstruction);
        }

        //指令“p”开头表示水平和玩家信息
        if (perInstruction.charAt(0) == CommandTable.PLAYER_IMPORT.charAt(0)) {

            playerAction(perInstruction);
        }

        //指令“g”开头表明获取胜利的统计数量
        if (perInstruction.charAt(0) == CommandTable.WIN_SIGN.charAt(0)) {

            countAction(perInstruction);
        }

        //指令“m”开头表示服务器玩家的信息
        if (perInstruction.charAt(0) == CommandTable.SERVER_IMPORT.charAt(0)) {

            ClientDrawingPanel.addMessage("主机端玩家说：" + perInstruction.substring(1, perInstruction.length()));
        }

        //指令“a”开头表示游戏结束
        if (perInstruction.charAt(0) == CommandTable.FAIL_SIGN.charAt(0)) {

            if (!ClientStatus.isGameOver()) {

                ClientDrawingPanel.addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                ClientStatus.setGameOver(true);
            }
        }

        //指令“j”开头表示服务器玩家想在玩一次
        if (perInstruction.charAt(0) == CommandTable.RESTART_SIGN.charAt(0)) {

            if (ClientStatus.isGameOver()) {

                ClientStatus.setServerVote(true);
            }
        }

        //指令“x”开头表示服务器玩家暂停游戏
        if (perInstruction.charAt(0) == CommandTable.PAUSE_SIGN.charAt(0)) {

            pauseAction(perInstruction);
        }
    }

    public static void baseLock(StringBuilder perInstruction) {
        int xPos;
        int yPos;
        boolean[] shape = new boolean[16];
        StringBuilder temp = new StringBuilder();

        int j = 1;
        //得到x的位置
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        xPos = Integer.parseInt(temp.toString());

        //得到y的位置
        temp = new StringBuilder();
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        yPos = Integer.parseInt(temp.toString());

        //墙的详细的边界
        final int brickWallSuite = 16;
        for (int k = 0; k < brickWallSuite; k++) {

            shape[k] = "1".equals(perInstruction.substring(j, j + 1));
            j++;
        }

        //执行指令
        for (int k = 0; k < ClientDrawingPanel.drawingList.length; k++) {
            if (ClientDrawingPanel.drawingList[k] != null) {
                if (ClientDrawingPanel.drawingList[k].getX() == xPos && ClientDrawingPanel.drawingList[k].getY() == yPos) {
                    ClientBrickClientWall tempClientBrickWall = new ClientBrickClientWall(xPos, yPos, 4);
                    tempClientBrickWall.shape = shape;
                    ClientDrawingPanel.drawingList[k] = tempClientBrickWall;
                }
            }
        }
    }

    public static void baseUnlock(StringBuilder perInstruction) {
        int xPos;
        int yPos;
        boolean[] shape = new boolean[4];
        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到x的位置
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        xPos = Integer.parseInt(temp.toString());

        //得到y的位置
        temp = new StringBuilder();
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        yPos = Integer.parseInt(temp.toString());

        //详细的钢墙边境
        final int stonewallSuite = 4;
        for (int k = 0; k < stonewallSuite; k++) {
            shape[k] = "1".equals(perInstruction.substring(j, j + 1));
            j++;
        }

        //执行指令
        for (int k = 0; k < ClientDrawingPanel.drawingList.length; k++) {
            if (ClientDrawingPanel.drawingList[k] != null) {
                if (ClientDrawingPanel.drawingList[k].getX() == xPos && ClientDrawingPanel.drawingList[k].getY() == yPos) {
                    ClientSteelClientWall tempWall = new ClientSteelClientWall(xPos, yPos, 4);
                    tempWall.shape = shape;
                    ClientDrawingPanel.drawingList[k] = tempWall;
                }
            }
        }
    }

    public static void bulletAction(StringBuilder perInstruction) {
        int xPos;
        int yPos;
        int direction;
        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到x子弹的位置
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        xPos = Integer.parseInt(temp.toString());

        //得到y子弹的位置
        temp = new StringBuilder();
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
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
        ClientDrawingPanel.addActor(new ClientBullet(xPos, yPos, direction));
    }

    public static void objectAction(StringBuilder perInstruction) {
        int xPos;
        int yPos;
        int textureIndex;
        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到x对象的位置
        while (!CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1))) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        xPos = Integer.parseInt(temp.toString());

        //得到y对象的位置
        temp = new StringBuilder();
        boolean getNextCommandY = CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1));
        while (!getNextCommandY) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
            getNextCommandY = CommandTable.NEXT_COMMAND.equals(perInstruction.substring(j, j + 1));
        }
        j++;
        yPos = Integer.parseInt(temp.toString());

        //获得对象的纹理指数
        temp = new StringBuilder();
        for (int k = j; k < perInstruction.length(); k++) {

            temp.append(perInstruction.substring(j, j + 1));
        }
        textureIndex = Integer.parseInt(temp.toString());

        //执行指令
        ClientDrawingPanel.addActor(new ClientNormalObject(xPos, yPos,"normal", textureIndex));
    }

    public static void bombAction(StringBuilder perInstruction) {

        int xPos;
        int yPos;
        int size;
        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到x炸弹的位置
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {
            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        j++;
        xPos = Integer.parseInt(temp.toString());

        //得到y炸弹的位置
        temp = new StringBuilder();
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {
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
        if (CommandTable.BOMB_SMALL.equals(temp.toString())) {
            size = 1;
        } else {
            size = 0;
        }
        //执行指令
        ClientDrawingPanel.addActor(new ClientBomb(xPos, yPos, size));
    }

    public static void shieldAction(StringBuilder perInstruction) {

        int xPos;
        int yPos;
        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到x位置的盾牌
        boolean isContinuable = perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0);
        while (isContinuable) {

            temp.append(perInstruction.substring(j, j + 1));
            j++;
            isContinuable = perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0);
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
        ClientDrawingPanel.addActor(new ClientShield(xPos, yPos));
    }

    public static void playerAction(StringBuilder perInstruction) {

        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到敌人离开的数量
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        j++;
        ClientModel.getView().getMainPanel().setEnemyLeft(Integer.parseInt(temp.toString()));

        //得到水平指数
        temp = new StringBuilder();
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        j++;
        ClientModel.getView().getMainPanel().setLevelIndex(Integer.parseInt(temp.toString()));

        //玩家1的生命量
        temp = new StringBuilder();
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        j++;
        ClientModel.getView().getMainPanel().setP1Life(Integer.parseInt(temp.toString()));

        //玩家1的分数
        temp = new StringBuilder();
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        j++;
        ClientModel.getView().getMainPanel().setP1Score(Integer.parseInt(temp.toString()));

        //玩家2的生命量
        temp = new StringBuilder();
        while (perInstruction.charAt(j) != CommandTable.NEXT_COMMAND.charAt(0)) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        j++;
        ClientModel.getView().getMainPanel().setP2Life(Integer.parseInt(temp.toString()));

        //玩家2的分数
        temp = new StringBuilder();
        while (j < perInstruction.length()) {

            temp.append(perInstruction.charAt(j));
            j++;
        }
        ClientModel.getView().getMainPanel().setP2Score(Integer.parseInt(temp.toString()));
    }

    public static void countAction(StringBuilder perInstruction) {

        StringBuilder temp = new StringBuilder();
        int j = 1;
        //得到敌人离开的数量
        while (j < perInstruction.length()) {

            temp.append(perInstruction.substring(j, j + 1));
            j++;
        }
        ClientLevel.setWinningCount(Integer.parseInt(temp.toString()));
    }

    public static void pauseAction(StringBuilder perInstruction) {

        int temp = Integer.parseInt(perInstruction.substring(1, 2));
        if (temp == 0) {

            if (ClientStatus.isGamePaused()) {

                ClientDrawingPanel.addMessage("主机端玩家取消了暂停");
                ClientStatus.setGamePaused(false);
            }
        } else {

            if (!ClientStatus.isGamePaused()) {

                ClientDrawingPanel.addMessage("主机端玩家暂停了游戏");
                ClientStatus.setGamePaused(true);
            }
        }
    }
}