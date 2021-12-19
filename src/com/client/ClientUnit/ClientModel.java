package com.client.ClientUnit;

import com.ProcessUnit.Ticker;
import com.client.ConponentPack.Actor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientModel implements ActionListener{
    //游戏变量
    private static int gameFlow;
    private static ClientView view;

    private static String serverIP;

    private static final ClientCommunication CLIENT_COMMUNICATION = new ClientCommunication();


    public static String playerTypedMessage = "";

    //textures
    public static Image[] textures;
    //实际的游戏运行在这个线程,而主线程听用户的输入
    private static Ticker t;
    public static Actor[] drawingList;

    public static ClientCommunication getClientCommunication() {
        return CLIENT_COMMUNICATION;
    }


    public static void setGameFlow(int gameFlow) {
        ClientModel.gameFlow = gameFlow;
    }

    public static String getPlayerTypedMessage() {
        return playerTypedMessage;
    }

    public static void setPlayerTypedMessage(String playerTypedMessage) {
        ClientModel.playerTypedMessage = playerTypedMessage;
    }

    public static int getGameFlow() {
        return gameFlow;
    }

    public static ClientView getView() {
        return view;
    }

    public static Ticker getT() {
        return t;
    }


    public static void setServerIp(String initserverIP) {
        serverIP = initserverIP;
    }

    public ClientModel(ClientView thisView) {
        view = thisView;

        DrawingPanel.messageQueue = new String[8];
        DrawingPanel.addMessage("欢迎来到坦克大战用户端！请输入主机IP地址然后点击\"连接主机\"按钮开始游戏");

        t = new Ticker(1000);
        t.addActionListener(this);

    }

    public static void connectServer() {

        DrawingPanel.addMessage("正在连接主机");
        serverIP = view.getIpField().getText();
        try {
           //连接主机并初始化流
            CLIENT_COMMUNICATION.connectServer(serverIP);
        } catch (Exception e) {
            t.stop();
            e.printStackTrace();
            DrawingPanel.addMessage("连接中出现错误， 请确认 1. 输入的IP是否正确,   2. 主机端已存在");
            return;
        }

        //主机连接成功
        Status.setServerConnected(true);

        DrawingPanel.addMessage("已成功连接到主机，开始载入游戏");

        view.getIpField().setFocusable(false);
        view.getIpField().setEnabled(false);

        //加载游戏 texture
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++) {
            textures[i - 1] = Toolkit.getDefaultToolkit().
                    getImage("image\\" + i + ".jpg");
        }
        drawingList = new Actor[400];

        Status.setGameStarted((true));

        Status.setGameStarted(true);
        view.getMainPanel().drawingList = drawingList;
        view.getMessageField().setEnabled(true);
        DrawingPanel.addMessage("载入完毕，游戏开始了！");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connectServer();

        //如果程序不能连接到服务器然后什么都不做
        if (Status.isServerConnected()) {
            return;
        }

        LogicalLoop.logic();
    }




    //添加一个游戏对象(如坦克、子弹等)图纸清单
    public static void addActor(Actor actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == null) {
                drawingList[i] = actor;
                break;
            }
        }
    }

    //删除一个游戏对象从图纸清单
    public static void removeActor(Actor actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == actor) {
                drawingList[i] = null;
                break;
            }
        }
    }


}
