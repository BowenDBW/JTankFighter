package com.server.ServerUnit;

import com.ProcessUnit.Ticker;
import com.server.ComponentPack.GameComponent;
import com.server.ComponentPack.Enemy;
import com.server.ComponentPack.Player;
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
    private final ServerView view;
    //连接变量
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public String inputLine, outputLine;
    //服务器状态
    //游戏消息
    private final String[] messageQueue;
    private int messageIndex;
    public String playerTypedMessage = "";
    //实际的游戏在这个线程上运行，而主线程监听用户的输入
    private final Ticker t;
    public Image[] textures;
    public GameComponent[] gameComponents;
    private Player p1;   //由服务器玩家控制的坦克
    private Player p2;   //有客户端玩家控制的坦克

    public static int getGameFlow() {
        return gameFlow;
    }



    public Ticker getT() {
        return t;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
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
            Status.setServerCreated(true);
        } catch (Exception e) {
            addMessage("无法建立主机，请确认端口9999没有被别的程序使用");
            e.printStackTrace();
            t.stop();
            return;
        }

        addMessage("建立完成，等待玩家连接");


        try {
            clientSocket = serverSocket.accept();


            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

        } catch (Exception e) {
            addMessage("连接中出现错误，请重新建立主机");
            Status.setServerCreated(false);

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
        gameComponents = new GameComponent[400];
        Level.loadLevel(this);

        p1 = new Player("1P", this);
        addActor(p1);
        p2 = new Player("2P", this);
        addActor(p2);

        Status.setGameStarted(true);
        view.getMainPanel().gameComponents = gameComponents;
        view.getMainPanel().setGameStarted(true);

        addMessage("载入完毕，游戏开始了！");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createServer();

        //如果程序未能创建服务器，则什么也不做
        if (!Status.isServerCreated()) {
            return;
        }

        //游戏逻辑回路，
        try {
            while ((inputLine = in.readLine()) != null) {
                //处理客户反馈消息
                FeedbackHandler.handleInstruction(this, inputLine);

                outputLine = "";

                if (!Status.isGamePaused()) {
                    gameFlow++;
                }

                if (!Status.isPausePressed()) {

                    if (!Status.isGamePaused()) {

                        outputLine += "x0;";
                    } else {

                        outputLine += "x1;";
                    }
                    Status.setPausePressed(false);
                }

                if (Status.isGameOver() || (p1.getLife() == 0 && p2.getLife() == 0)) {

                    if (p1.getFrozen() != 1) {

                        outputLine += "a;";
                    };

                    if ((p1.getFrozen() != 1 || messageIndex == 1) && Status.isServerVoteYes()) {

                        addMessage("等待用户端玩家的回应...");
                    }
                    if (p1.getFrozen() != 1 || messageIndex == 0) {

                        addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                    }
                    Status.setGameOver(true);
                    p1.setFrozen(1);
                    p2.setFrozen(1);

                    if (Status.isServerVoteNo() && !Status.isServerVoteYes()){

                        System.exit(0);
                    }

                    if (Status.isServerVoteYes()) {

                        outputLine += "j;";
                        if (Status.isClientVoteYes()) {

                            addMessage("用户端玩家决定再玩一次，游戏重新开始了...");

                            //重新启动游戏
                            p1 = new Player("1P", this);
                            p2 = new Player("2P", this);
                            Level.reset();
                            Level.loadLevel(this);
                            Status.setGameOver(false);
                            Status.setServerVoteYes(false);
                            Status.setClientVoteYes(false);
                            Status.setServerVoteNo(false);
                            Enemy.setFrozenMoment(0);
                            Enemy.setFrozenTime(0);
                            gameFlow = 0;

                            //告诉客户端程序重新启动游戏
                            outputLine += "L1;";
                        }
                    }
                }

                if (Level.getDeathCount() == 20 && !Status.isGameOver()) {
                    int winningCount = Level.getWinningCount();
                    winningCount++;
                    Level.setWinningCount(winningCount);
                    if (Level.getWinningCount() == 120) {
                        p1.setFrozen(1);
                        p2.setFrozen(1);
                    }
                    if (Level.getWinningCount() == 470) {
                        if (p1.getLife() > 0) {
                            p1.reset();
                        }
                        if (p2.getLife() > 0) {
                            p2.reset();
                        }
                        Level.loadLevel(this);
                        //告诉客户端程序加载下一关
                        outputLine += "L" + (1 + (Level.getCurrentLevel() - 1) % 8) + ";";
                    }
                    if (Level.getWinningCount() == 500) {
                        p1.setFrozen(0);
                        p2.setFrozen(0);
                        Level.setDeathCount(0);
                        Level.setWinningCount(0);
                    }

                }

                //大量生产敌人坦克
                if (!Status.isGamePaused()) {
                    Level.spawnEnemy(this);
                }

                for (GameComponent gameComponent : gameComponents) {
                    if (gameComponent != null) {
                        gameComponent.move();
                    }
                }

                //从消息队列中删除一个消息每10秒，（如果有的话）
                if (gameFlow % 300 == 0) {
                    removeMessage();
                }

                //将玩家、关卡的信息写入输出行
                outputLine += "p" + Level.getEnemyLeft() + "," + Level.getCurrentLevel() + "," + p1.getLife() + "," + p1.scores + "," + p2.getLife() + "," + p2.scores + ";";
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
                    p1.setMoveLeft(false);
                    p1.setMoveUp(false);
                    p1.setMoveDown(false);
                    p1.setMoveRight(false);
                    p1.setFire(false);
                }

                Thread.sleep(30);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            view.getMessageField().setEnabled(false);
            Status.setServerVoteYes(false);
            Status.setServerVoteNo (false);
            Status.setClientVoteYes(false);
            Status.setServerCreated(false);
            Status.setGameStarted(false);
            Status.setGameOver(false);
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
            p1 = null;
            p2 = null;
            Level.reset();
        }
    }

    //添加游戏对象（如坦克，子弹等..）到游戏系统
    public void addActor(GameComponent gameComponent) {
        for (int i = 0; i < gameComponents.length; i++) {
            if (gameComponents[i] == null) {
                gameComponents[i] = gameComponent;
                break;
            }
        }
    }

    //从游戏系统中移除游戏对象
    public void removeActor(GameComponent gameComponent) {
        for (int i = 0; i < gameComponents.length; i++) {
            if (gameComponents[i] == gameComponent) {
                gameComponents[i] = null;
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
        if (!Status.isGameStarted()) {
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
        if (!Status.isGameStarted()) {
            view.getMainPanel().repaint();
        }
    }

}