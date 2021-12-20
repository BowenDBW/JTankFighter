package com.server.ServerUnit;

import com.ProcessUnit.Instruction;
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

/**
 * @author 26317
 */
public class ServerModel implements ActionListener {
    //游戏变量
    private static int gameFlow;

    //视图参考
    private static ServerView view;
    //游戏消息
    public String playerTypedMessage = "";
    //实际的游戏在这个线程上运行，而主线程监听用户的输入
    public static Image[] textures;
    public static GameComponent[] gameComponents;
    //由服务器玩家控制的坦克
    private static Player p1;
    //有客户端玩家控制的坦克
    private static Player p2;

    public static int getGameFlow() {
        return gameFlow;
    }

    private static Ticker t;

    public ServerModel(ServerView thisView) {

        view = thisView;
        DrawingPanel.setMessageQueue(new String[8]);

        DrawingPanel.addMessage("欢迎来到坦克大战主机端!  请点击\"建立主机\"按钮开始游戏");

        t = new Ticker(1000);
        t.addActionListener(this);
    }

    public static Ticker getT() {

        return t;
    }

    public static Player getP1() {

        return p1;
    }

    public static Player getP2() {

        return p2;
    }

    public void createServer() {

        DrawingPanel.addMessage("正在建立主机(端口9999)");

        try {
            ServerCommunication.setServerSocket(new ServerSocket(9999));
            Status.setServerCreated(true);
        } catch (Exception e) {
            DrawingPanel.addMessage("无法建立主机，请确认端口9999没有被别的程序使用");
            e.printStackTrace();
            t.stop();
            return;
        }

        DrawingPanel.addMessage("建立完成，等待玩家连接");


        try {
            ServerCommunication.setClientSocket(ServerCommunication.getServerSocket().accept());


            ServerCommunication.setOut(new PrintWriter(ServerCommunication.getClientSocket().getOutputStream(), true));
            ServerCommunication.setIn(new BufferedReader(new InputStreamReader(
                    ServerCommunication.getClientSocket().getInputStream())));

        } catch (Exception e) {
            DrawingPanel.addMessage("连接中出现错误，请重新建立主机");
            Status.setServerCreated(false);

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

        view.getMessageField().setEnabled(true);
        DrawingPanel.addMessage("玩家已连接上，开始载入游戏");

        //一旦客户端连接，然后告诉客户端开始加载游戏
        ServerCommunication.getOut().println("L1;");

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
        DrawingPanel.gameComponents = gameComponents;
        view.getMainPanel().setGameStarted(true);

        DrawingPanel.addMessage("载入完毕，游戏开始了！");
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

            String line;
            while ((line = ServerCommunication.getIn().readLine()) != null) {
                //处理客户反馈消息
                FeedbackHandler.handleInstruction(line);

                if (!Status.isGamePaused()) {
                    gameFlow++;
                }

                if (!Status.isPausePressed()) {

                    if (!Status.isGamePaused()) {

                        Instruction.getFromSever().append("x0;");
                    } else {

                        Instruction.getFromSever().append("x1;");
                    }
                    Status.setPausePressed(false);
                }

                if (Status.isGameOver() || (p1.getLife() == 0 && p2.getLife() == 0)) {

                    if (p1.getFrozen() != 1) {

                        Instruction.getFromSever().append("a;");
                    }

                    if ((p1.getFrozen() != 1 || DrawingPanel.getMessageIndex() == 1) && Status.isServerVoteYes()) {

                        DrawingPanel.addMessage("等待用户端玩家的回应...");
                    }
                    if (p1.getFrozen() != 1 || DrawingPanel.getMessageIndex() == 0) {

                        DrawingPanel.addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
                    }
                    Status.setGameOver(true);
                    p1.setFrozen(1);
                    p2.setFrozen(1);

                    if (Status.isServerVoteNo() && !Status.isServerVoteYes()){

                        System.exit(0);
                    }

                    if (Status.isServerVoteYes()) {

                        Instruction.getFromSever().append("j;");
                        if (Status.isClientVoteYes()) {

                            DrawingPanel.addMessage("用户端玩家决定再玩一次，游戏重新开始了...");

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
                            Instruction.getFromSever().append("L1;");
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
                        Instruction.getFromSever().append("L").append(1 + (Level.getCurrentLevel() - 1) % 8).append(";");
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
                    DrawingPanel.removeMessage();
                }

                //将玩家、关卡的信息写入输出行
                Instruction.getFromSever().append("p").append(Level.getEnemyLeft()).append(",").append(Level.getCurrentLevel())
                        .append(",").append(p1.getLife()).append(",").append(p1.scores).append(",").append(p2.getLife())
                        .append(",").append(p2.scores).append(";");
                Instruction.getFromSever().append("g").append(Level.getWinningCount()).append(";");

                //将玩家类型信息写入输出行
                if (!"".equals(playerTypedMessage)) {
                    Instruction.getFromSever().append(playerTypedMessage);
                    playerTypedMessage = "";
                }

                //将最后的指令字符串发送到客户端程序
                ServerCommunication.getOut().println(Instruction.getFromSever());
                Instruction.setFromSever(new StringBuffer());
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
            DrawingPanel.addMessage("玩家退出了，请重新建立主机");

            //当发生错误在游戏中，摧毁任何东西，包括游戏的变量
            try {

                ServerCommunication.getOut().close();
                ServerCommunication.getIn().close();
                ServerCommunication.getClientSocket().close();
                ServerCommunication.getServerSocket().close();
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
    public static void addActor(GameComponent gameComponent) {
        for (int i = 0; i < gameComponents.length; i++) {
            if (gameComponents[i] == null) {
                gameComponents[i] = gameComponent;
                break;
            }
        }
    }

    //从游戏系统中移除游戏对象
    public static void removeActor(GameComponent gameComponent) {
        for (int i = 0; i < gameComponents.length; i++) {
            if (gameComponents[i] == gameComponent) {
                gameComponents[i] = null;
                break;
            }
        }
    }

    public static ServerView getView() {
        return view;
    }
}