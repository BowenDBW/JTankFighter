package com.ProcessUnit.ServerPack;

import com.CommunicateUnit.Instruction;
import com.CommunicateUnit.ServerPack.ServerCommunication;
import com.ProcessUnit.LogicalLoop;
import com.ProcessUnit.Ticker;
import com.SourceUnit.ServerPack.ServerGameComponent;
import com.SourceUnit.ServerPack.ServerEnemy;
import com.SourceUnit.ServerPack.ServerPlayer;
import com.UI.ServerDrawingPanel;
import com.UI.ServerView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

/**
 * @author 26317
 */
public class ServerModel implements ActionListener {
    //游戏变量
    private static int gameFlow;

    //视图参考
    private static ServerView view;
    //游戏消息
    private static String playerTypedMessage = "";
    //实际的游戏在这个线程上运行，而主线程监听用户的输入
    public static Image[] textures;
    //由服务器玩家控制的坦克
    private static ServerPlayer p1;
    //有客户端玩家控制的坦克
    private static ServerPlayer p2;

    public static int getGameFlow() {
        return gameFlow;
    }

    private static Ticker t;

    public ServerModel(ServerView thisView) {

        view = thisView;
        ServerDrawingPanel.setMessageQueue(new String[8]);

        ServerDrawingPanel.addMessage("欢迎来到坦克大战主机端!  请点击\"建立主机\"按钮开始游戏");

        t = new Ticker(1000);
        t.addActionListener(this);
    }

    public static Ticker getT() {

        return t;
    }

    public static ServerPlayer getP1() {

        return p1;
    }

    public static ServerPlayer getP2() {

        return p2;
    }

    public static void loadImage(){

        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++) {
            textures[i - 1] = Toolkit.getDefaultToolkit().getImage("image\\" + i + ".jpg");
        }
    }

    public static void createServer() {

        ServerDrawingPanel.addMessage("正在建立主机(端口9999)");

        try {
            ServerCommunication.setServerSocket(new ServerSocket(9999));
            ServerStatus.setServerCreated(true);
        } catch (Exception e) {
            ServerDrawingPanel.addMessage("无法建立主机，请确认端口9999没有被别的程序使用");
            e.printStackTrace();
            t.stop();
            return;
        }

        ServerDrawingPanel.addMessage("建立完成，等待玩家连接");


        try {
            ServerCommunication.setClientSocket(ServerCommunication.getServerSocket().accept());


            ServerCommunication.setOut(
                    new PrintWriter(ServerCommunication.getClientSocket().getOutputStream(), true));
            ServerCommunication.setIn(new BufferedReader(
                    new InputStreamReader(ServerCommunication.getClientSocket().getInputStream())));

        } catch (Exception e) {

            ServerDrawingPanel.addMessage("连接中出现错误，请重新建立主机");
            ServerStatus.setServerCreated(false);

            t.stop();

            //当发生错误，摧毁一切已创建的
            try {

                ServerCommunication.getServerSocket().close();
                ServerCommunication.getClientSocket().close();
                ServerCommunication.getOut().close();
                ServerCommunication.getIn().close();

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            return;
        }

        initGame();
    }

    public static void initGame(){

        view.getMessageField().setEnabled(true);
        ServerDrawingPanel.addMessage("玩家已连接上，开始载入游戏");

        //一旦客户端连接，然后告诉客户端开始加载游戏
        ServerCommunication.getOut().println("L1;");

        //加载游戏
        loadImage();

        //设置第一关
        ServerDrawingPanel.setGameComponents(new ServerGameComponent[400]);
        ServerLevel.loadLevel();

        p1 = new ServerPlayer("1P");
        ServerDrawingPanel.addActor(p1);
        p2 = new ServerPlayer("2P");
        ServerDrawingPanel.addActor(p2);

        ServerStatus.setGameStarted(true);
        view.getMainPanel().setGameStarted(true);

        ServerDrawingPanel.addMessage("载入完毕，游戏开始了！");
    }

    public static void restartGame() {

        ServerDrawingPanel.addMessage("用户端玩家决定再玩一次，游戏重新开始了...");

        //重新启动游戏
        p1 = new ServerPlayer("1P");
        p2 = new ServerPlayer("2P");
        ServerLevel.reset();
        ServerLevel.loadLevel();
        ServerStatus.setGameOver(false);
        ServerStatus.setServerVoteYes(false);
        ServerStatus.setClientVoteYes(false);
        ServerStatus.setServerVoteNo(false);
        ServerEnemy.setFrozenMoment(0);
        ServerEnemy.setFrozenTime(0);
        gameFlow = 0;

        //告诉客户端程序重新启动游戏
        Instruction.getFromSever().append("L1;");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        createServer();

        //如果程序未能创建服务器，则什么也不做
        if (!ServerStatus.isServerCreated()) {
            return;
        }

        //游戏逻辑回路，
        LogicalLoop.serverLogic();
    }

    public static ServerView getView() {
        return view;
    }

    public static String getPlayerTypedMessage() {
        return playerTypedMessage;
    }

    public static void setGameFlow(int gameFlow) {
        ServerModel.gameFlow = gameFlow;
    }

    public static void setPlayerTypedMessage(String playerTypedMessage) {
        ServerModel.playerTypedMessage = playerTypedMessage;
    }

    public static void setP1(ServerPlayer p1) {
        ServerModel.p1 = p1;
    }

    public static void setP2(ServerPlayer p2) {
        ServerModel.p2 = p2;
    }
}