package com.client.ClientUnit;

import com.client.ConponentPack.Actor;
import com.client.ConponentPack.Ticker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientModel implements ActionListener {
    //游戏变量
    private static int gameFlow;
    private ClientView view;
    //连接变量
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String fromServer, fromUser;
    private String serverIP;
    //客户端状态
    private boolean serverConnected;
    private boolean gameStarted;
    private boolean gamePaused;
    private boolean gameOver;
    private boolean serverVote;
    private boolean clientVoteYes, clientVoteNo;
    private boolean pausePressed;
    //图像信息
    public String[] messageQueue;
    private int messageIndex;

    public String playerTypedMessage = "";

    //textures
    public Image[] textures;
    //实际的游戏运行在这个线程,而主线程听用户的输入
    private Ticker t;

    private Actor[] drawingList;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean fire;


    public ClientView getView() {
        return view;
    }


    public boolean isServerConnected() {
        return serverConnected;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public boolean isGameOver() {
        return gameOver;
    }


    public boolean isClientVoteYes() {
        return clientVoteYes;
    }


    public Image[] getTextures() {
        return textures;
    }

    public Ticker getT() {
        return t;
    }

    public Actor getDrawingList(int k) {
        return drawingList[k];
    }

    public Actor[] getDrawingList() {
        return drawingList;
    }


    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }


    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setServerVote(boolean serverVote) {
        this.serverVote = serverVote;
    }

    public void setClientVoteYes(boolean clientVoteYes) {
        this.clientVoteYes = clientVoteYes;
    }

    public void setClientVoteNo(boolean clientVoteNo) {
        this.clientVoteNo = clientVoteNo;
    }

    public void setPausePressed(boolean pausePressed) {
        this.pausePressed = pausePressed;
    }


    public void setTextures(Image[] textures) {
        this.textures = textures;
    }


    public void setDrawingList(int k, Actor actor) {
        this.drawingList[k] = actor;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public ClientModel(ClientView thisView) {
        view = thisView;
        messageQueue = new String[8];
        view.getMainPanel().messageQueue = messageQueue;
        addMessage("欢迎来到坦克大战用户端！请输入主机IP地址然后点击\"连接主机\"按钮开始游戏");

        t = new Ticker(1000);
        t.addActionListener(this);

    }

    public void connectServer() {
        addMessage("正在连接主机");

        try {
            serverIP = view.getIPField().getText();
            InetAddress address = InetAddress.getByName(serverIP);
            clientSocket = new Socket(address, 9999);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader
                    (clientSocket.getInputStream()));

        } catch (Exception e) {
            t.stop();
            e.printStackTrace();
            addMessage("连接中出现错误， 请确认 1. 输入的IP是否正确,   2. 主机端已存在");
            return;
        }

        serverConnected = true;
        addMessage("已成功连接到主机，开始载入游戏");
        view.getIPField().setFocusable(false);
        view.getIPField().setEnabled(false);

        //加载游戏 texture
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++) {
            textures[i - 1] = Toolkit.getDefaultToolkit().
                    getImage("image\\" + i + ".jpg");
        }

        drawingList = new Actor[400];

        gameStarted = true;
        view.getMainPanel().setGameStarted(true);
        view.getMainPanel().drawingList = drawingList;
        view.getMessageField().setEnabled(true);
        addMessage("载入完毕，游戏开始了！");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connectServer();

        //如果程序不能连接到服务器然后什么都不做
        if (!serverConnected) {
            return;
        }

        //游戏逻辑循环,客户端程序实际不执行任何逻辑计算,它只接受drawing-instructions
        try {
            while ((fromServer = in.readLine()) != null) {
                fromUser = "";

                gameFlow++;

                if (pausePressed) {
                    fromUser += "x;";
                    pausePressed = false;
                }

                if (gameOver) {
                    if (clientVoteNo) {
                        System.exit(0);
                    }

                    if (clientVoteYes) {
                        fromUser += "j;";
                        if (serverVote) {
                            addMessage("主机端玩家决定再玩一次，游戏重新开始了...");
                            gameOver = false;
                            clientVoteYes = false;
                            serverVote = false;
                        }
                    }
                }

                //指令字符串做出反馈,告诉服务器客户端在做什么
                fromUser += "m";
                if (moveUp) {
                    fromUser += "1";
                } else {
                    fromUser += "0";
                }
                if (moveDown) {
                    fromUser += "1";
                } else {
                    fromUser += "0";
                }
                if (moveLeft) {
                    fromUser += "1";
                } else {
                    fromUser += "0";
                }
                if (moveRight) {
                    fromUser += "1";
                } else {
                    fromUser += "0";
                }
                if (fire) {
                    fromUser += "1";
                } else {
                    fromUser += "0";
                }
                fromUser += ";";

                //来自服务器的进程指令
                InstructionHandler.handleInstruction(this, fromServer);

                //从消息队列中删除一个消息每10秒,(如果有)
                if (gameFlow % 300 == 0) {
                    removeMessage();
                }

                //输出玩家坦克信息
                if (!"".equals(playerTypedMessage)) {
                    fromUser += playerTypedMessage;
                    playerTypedMessage = "";
                }

                //发送反馈指令
                out.println(fromUser);

                //调用视图重新绘制它自己
                view.getMainPanel().repaint();

                //如果切换到对话模式的玩家,那么停止所有坦克行动
                if (!view.getMainPanel().hasFocus()) {
                    moveLeft = false;
                    moveUp = false;
                    moveDown = false;
                    moveRight = false;
                    fire = false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            t.stop();
            view.getMessageField().setEnabled(false);
            serverConnected = false;
            gameStarted = false;
            view.getMainPanel().setGameStarted(false);
            gameOver = false;
            addMessage("主机端退出了");
            view.getIPField().setFocusable(true);
            view.getIPField().setEnabled(true);

            //当有错误发生时,关闭创建的任何事情
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    //在屏幕上显示一条消息
    public void addMessage(String message) {
        if (messageIndex < 8) {
            messageQueue[messageIndex] = message;
            messageIndex++;
        } else {
            System.arraycopy
                    (messageQueue, 1, messageQueue, 0, 7);
            messageQueue[7] = message;
        }

        //调用视图来重新绘制屏幕，如果没有开始游戏
        if (!gameStarted) {
            view.getMainPanel().repaint();
        }
    }

    //删除最早的消息在屏幕上
    public void removeMessage() {
        if (messageIndex == 0) {
            return;
        }

        messageIndex--;
        if (messageIndex >= 0) {
            System.arraycopy
                    (messageQueue, 1, messageQueue, 0, messageIndex);
        }
        messageQueue[messageIndex] = null;

        //调用视图来重新绘制屏幕如果没有开始游戏
        if (!gameStarted) {
            view.getMainPanel().repaint();
        }
    }

    //添加一个游戏对象(如坦克、子弹等)图纸清单
    public void addActor(Actor actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == null) {
                drawingList[i] = actor;
                break;
            }
        }
    }

    //删除一个游戏对象从图纸清单
    public void removeActor(Actor actor) {
        for (int i = 0; i < drawingList.length; i++) {
            if (drawingList[i] == actor) {
                drawingList[i] = null;
                break;
            }
        }
    }


}
