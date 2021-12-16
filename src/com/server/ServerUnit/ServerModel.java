package com.server.ServerUnit;

import com.server.ConponentPack.Actor;
import com.server.ConponentPack.Enemy;
import com.server.ConponentPack.Player;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerModel implements ActionListener {
    //游戏变量
    private static int gameFlow;
    //视图参考
    private ServerView view;
    //连接变量
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public String inputLine, outputLine;
    //服务器状态
    private boolean serverCreated;
    private boolean clientConnected;
    private boolean gameStarted;
    private boolean gamePaused;
    private boolean gameOver;
    private boolean serverVoteYes, serverVoteNo;
    private boolean clientVoteYes;
    private boolean pausePressed;
    //游戏消息
    private final String[] messageQueue;
    private int messageIndex;
    public String playerTypedMessage = "";
    //实际的游戏在这个线程上运行，而主线程监听用户的输入
    private Ticker t;
    public Image[] textures;
    public Actor[] actors;
    private Player P1;   //由服务器玩家控制的坦克
    private Player P2;   //有客户端玩家控制的坦克

    public static int getGameFlow() {
        return gameFlow;
    }

    public boolean isServerCreated() {
        return serverCreated;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }


    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isServerVoteYes() {
        return serverVoteYes;
    }

    public void setServerVoteYes(boolean serverVoteYes) {
        this.serverVoteYes = serverVoteYes;
    }

    public void setServerVoteNo(boolean serverVoteNo) {
        this.serverVoteNo = serverVoteNo;
    }

    public void setClientVoteYes(boolean clientVoteYes) {
        this.clientVoteYes = clientVoteYes;
    }


    public void setPausePressed(boolean pausePressed) {
        this.pausePressed = pausePressed;
    }


    public Ticker getT() {
        return t;
    }


    public Image[] getTextures() {
        return textures;
    }

    public void setTextures(Image[] textures) {
        this.textures = textures;
    }

    public Player getP1() {
        return P1;
    }

    public Player getP2() {
        return P2;
    }

    public ServerModel(ServerView thisView) {

        view = thisView;
        messageQueue = new String[8];
        view.getMainPanel().messageQueue = messageQueue;

        addMessage("欢迎来到坦克大战主机端!  请点击\"建立主机\"按钮开始游戏");

        t = new Ticker(1000);
        t.addActionListener(this);
    }


    public void createServer() {

        addMessage("正在建立主机(端口9999)");

        try {
            serverSocket = new ServerSocket(9999);
            serverCreated = true;
        } catch (Exception e) {
            addMessage("无法建立主机，请确认端口9999没有被别的程序使用");
            e.printStackTrace();
            t.stop();
            return;
        }

        addMessage("建立完成，等待玩家连接");

        try {
            clientSocket = serverSocket.accept();
            clientConnected = true;

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

        } catch (Exception e) {
            addMessage("连接中出现错误，请重新建立主机");
            serverCreated = false;
            clientConnected = false;
            t.stop();

            //当发生错误，摧毁一切已创建的
            try {
                serverSocket.close();
                clientSocket.close();
                out.close();
                in.close();
            } catch (Exception ex) {

                ex.printStackTrace();
            }

            return;
        }

        view.getMessageField().setEnabled(true);
        addMessage("玩家已连接上，开始载入游戏");

        //一旦客户端连接，然后告诉客户端开始加载游戏
        out.println("L1;");

        //加载游戏
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++) {
            textures[i - 1] = Toolkit.getDefaultToolkit().getImage("image\\" + i + ".jpg");
        }


        //设置第一关
        actors = new Actor[400];
        Level.loadLevel(this);

        P1 = new Player("1P", this);
        addActor(P1);
        P2 = new Player("2P", this);
        addActor(P2);

        gameStarted = true;
        view.getMainPanel().actors = actors;
        view.getMainPanel().setGameStarted(true);

        addMessage("载入完毕，游戏开始了！");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createServer();

        //如果程序未能创建服务器，则什么也不做
        if (!serverCreated) {
            return;
        }

        //游戏逻辑回路，
        try {
            while ((inputLine = in.readLine()) != null) {
                //处理客户反馈消息
                FeedbackHandler.handleInstruction(this, inputLine);

                outputLine = "";

                if (!gamePaused) {
                    gameFlow++;
                }

                if (pausePressed) {

                    if (!gamePaused) {

                        outputLine += "x0;";
                    } else {

                        outputLine += "x1;";
                    }
                    pausePressed = false;
                }

                if (gameOver || (P1.getLife() == 0 && P2.getLife() == 0)) {

                    if (P1.getFrozen() != 1) {

                        outputLine += "a;";
                    };

                    if ((P1.getFrozen() != 1 || messageIndex == 1) && serverVoteYes) {

                        addMessage("等待用户端玩家的回应...");
                    }
                    if (P1.getFrozen() != 1 || messageIndex == 0) {

                        addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                    }
                    gameOver = true;
                    P1.setFrozen(1);
                    P2.setFrozen(1);

                    if (serverVoteNo && !serverVoteYes){

                        System.exit(0);
                    }

                    if (serverVoteYes) {

                        outputLine += "j;";
                        if (clientVoteYes) {

                            addMessage("用户端玩家决定再玩一次，游戏重新开始了...");

                            //重新启动游戏
                            P1 = new Player("1P", this);
                            P2 = new Player("2P", this);
                            Level.reset();
                            Level.loadLevel(this);
                            gameOver = false;
                            serverVoteYes = false;
                            clientVoteYes = false;
                            serverVoteNo = false;
                            Enemy.setFrozenMoment(0);
                            Enemy.setFrozenTime(0);
                            gameFlow = 0;

                            //告诉客户端程序重新启动游戏
                            outputLine += "L1;";
                        }
                    }
                }

                if (Level.getDeathCount() == 20 && !gameOver) {
                    int winningCount = Level.getWinningCount();
                    winningCount++;
                    Level.setWinningCount(winningCount);
                    if (Level.getWinningCount() == 120) {
                        P1.setFrozen(1);
                        P2.setFrozen(1);
                    }
                    if (Level.getWinningCount() == 470) {
                        if (P1.getLife() > 0) {
                            P1.reset();
                        }
                        if (P2.getLife() > 0) {
                            P2.reset();
                        }
                        Level.loadLevel(this);
                        //告诉客户端程序加载下一关
                        outputLine += "L" + (1 + (Level.getCurrentLevel() - 1) % 8) + ";";
                    }
                    if (Level.getWinningCount() == 500) {
                        P1.setFrozen(0);
                        P2.setFrozen(0);
                        Level.setDeathCount(0);
                        Level.setWinningCount(0);
                    }

                }

                //大量生产敌人坦克
                if (!gamePaused) {
                    Level.spawnEnemy(this);
                }

                for (Actor actor : actors) {
                    if (actor != null) {
                        actor.move();
                    }
                }

                //从消息队列中删除一个消息每10秒，（如果有的话）
                if (gameFlow % 300 == 0) {
                    removeMessage();
                }

                //将玩家、关卡的信息写入输出行
                outputLine += "p" + Level.getEnemyLeft() + "," + Level.getCurrentLevel() + "," + P1.getLife() + "," + P1.scores + "," + P2.getLife() + "," + P2.scores + ";";
                outputLine += "g" + Level.getWinningCount() + ";";

                //将玩家类型信息写入输出行
                if (!"".equals(playerTypedMessage)) {
                    outputLine += playerTypedMessage;
                    playerTypedMessage = "";
                }

                //将最后的指令字符串发送到客户端程序
                out.println(outputLine);

                //调用视图重绘本身
                view.getMainPanel().repaint();

                //如果玩家切换到对话框模式，则停止所有坦克动作
                if (!view.getMainPanel().hasFocus()) {
                    P1.setMoveLeft(false);
                    P1.setMoveUp(false);
                    P1.setMoveDown(false);
                    P1.setMoveRight(false);
                    P1.setFire(false);
                }

                Thread.sleep(30);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            view.getMessageField().setEnabled(false);
            serverVoteYes = false;
            serverVoteNo = false;
            clientVoteYes = false;
            serverCreated = false;
            gameStarted = false;
            gameOver = false;
            gameFlow = 0;
            Enemy.setFrozenTime(0);
            Enemy.setFrozenMoment(0);
            view.getMainPanel().setGameStarted(false);
            t.stop();
            addMessage("玩家退出了，请重新建立主机");

            //当发生错误在游戏中，摧毁任何东西，包括游戏的变量
            try {

                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
            } catch (Exception exc) {

                exc.printStackTrace();
            }

            //破坏游戏数据
            P1 = null;
            P2 = null;
            Level.reset();
        }
    }

    //添加游戏对象（如坦克，子弹等..）到游戏系统
    public void addActor(Actor actor) {
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] == null) {
                actors[i] = actor;
                break;
            }
        }
    }

    //从游戏系统中移除游戏对象
    public void removeActor(Actor actor) {
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] == actor) {
                actors[i] = null;
                break;
            }
        }
    }


    //在屏幕上显示一行消息
    public void addMessage(String message) {
        if (messageIndex < 8) {
            messageQueue[messageIndex] = message;
            messageIndex++;
        } else {
            System.arraycopy(messageQueue, 1, messageQueue, 0, 7);
            messageQueue[7] = message;
        }

        //调用视图重绘屏幕如果游戏有没有开始
        if (!gameStarted) {
            view.getMainPanel().repaint();
        }
    }

    //删除屏幕上最早的信息
    public void removeMessage() {

        if (messageIndex == 0) {
            return;
        }

        messageIndex--;
        if (messageIndex >= 0) {
            System.arraycopy(messageQueue, 1, messageQueue, 0, messageIndex);
        }
        messageQueue[messageIndex] = null;

        //调用视图重绘屏幕如果比赛还没开始
        if (!gameStarted) {
            view.getMainPanel().repaint();
        }
    }

}