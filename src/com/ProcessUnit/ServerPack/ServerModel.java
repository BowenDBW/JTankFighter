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
 * <p>
 *     This class is for server to load a level and initialize
 *     the game once server and client get connection and connect
 *     is proved to be valid.ServerModel functions by load and
 *     processing map files to put every game component into
 *     proper position.
 * </p>
 *
 * <p>
 *     compose of file map.txt:
 *     a 20 x 20 element cubes
 *     -- represents nothing
 *     else represents name of the class name
 * </p>
 *
 * <p>
 *     此类用于服务器端加载关卡并初始化游戏一旦
 *     服务器和客户端获得连接并且连接被证明是有效的。
 *     ServerModel 功能通过加载和处理地图文件来放置
 *     每个游戏组件到适当的位置。
 * </p>
 *
 * <p>
 *     文件map.txt组成：
 *     一个 20 x 20 元素的容器
 *     -- 代表没有任何元素
 *     否则存入该元素类名
 * </p>
 *
 * @author BowenDeng
 * @version 1.0 初始版本
 **/
public class ServerModel implements ActionListener {


    /**
     * <p>
     * TimeStamp of this project
     * </p>
     * <p>
     * 游戏运行的时间戳
     * </p>
     */
    private static int gameFlow;


    /**
     * <p>
     * an object of class ClientView used to display
     * game frame.
     * </p>
     * 游戏的界面
     * <p></p>
     */
    private static ServerView view;


    /**
     * <p>
     * A place to store user typed message
     *</p>
     *
     * <p>
     * 一个用于临时存储用户输入消息的字符串属性
     * </p>
     */
    private static String playerTypedMessage = "";


    /**
     * <p>
     * image source collection. To store image source when a game loaded.
     * </p>
     *
     * <p>
     * 游戏图片资源加载库
     * </p>
     */
    public static Image[] textures;


    /**
     * <p>
     * Tank Controlled by player from Server
     *</p>
     *
     * <p>
     * 由服务端玩家控制的坦克
     * </p>
     */
    private static ServerPlayer p1;


    /**
     * <p>
     * Tank Controlled by player from Client
     *</p>
     *
     * <p>
     * 由客户端玩家控制的坦克
     */
    private static ServerPlayer p2;


    /**
     *   实际的游戏运行在这个线程,而主线程听用户的输入
     */
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

    /**
     * <p>
     * This method tells how to set up a server
     * and waiting for connections.Its function
     * by calling methods from the class named
     * ServerCommunication. Once it has done,
     * ServerModel will move through next step
     * which is to load the game and initialize
     * the game frame.
     *</p>
     *
     * <p>
     * 此方法告诉如何设置服务器和等待连接。
     * 它通过从名为服务器通信的类调用方法
     * 一旦完成，ServerModel 将进入下一步:
     * 加载游戏并初始化游戏框架。
     * </p>
     */
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

    /**
     * <p>
     * This method explains how to initialize a new game
     * when connection finished.Its function by calling a
     * series of methods to accomplish loading process.
     * Once it has done, ServerModel will move to next step
     * which is to monit the game.
     * </p>
     *
     * <p>
     * 这个方法解释了如何初始化一个新游戏
     * 当连接完成时。它的函数通过调用一系列方法完成加载过程。
     * 一旦完成，ServerModel 将进入下一步:
     * 监控游戏。
     * </p>
     */
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


    /**
     * <p>
     * This method is used to process player's request
     * when a game end, player type y/n to choose whether
     * to restart a new game. Its function is to respond
     * to player's decision about whether to restart a new
     * game or not, and this method will call proper
     * method to accomplish user's demand.
     * </p>
     *
     * <p>
     * 该方法用于处理玩家的请求
     * 当游戏结束时，玩家输入 y/n 来选择是否重新开始新游戏。
     * 它的功能是响应玩家决定是否重新开始一个新的游戏，此方法
     * 将调用合适的方法来完成用户输入的需求。
     * </p>
     */
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


    /**
     * <p>
     * This method is used for responding to
     * players' commands of connecting to the
     * server, once it connected, it will go
     * into the logic loop.
     * </p>
     *
     * <p>
     * 此方法用于响应玩家的连接命令,
     * 服务器一旦连接，它就会去进入逻
     * 辑循环。
     * </p>
     *
     * @param e a event
     */
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


    /**
     * <p>
     * This method is used to get value of the parameter:
     * view.
     * </p>
     *
     * <p>
     * 此方法用于返回本类中的属性：游戏界面
     *</p>
     *
     * @return game frame of the game
     */
    public static ServerView getView() {
        return view;
    }


    /**
     * <p>
     * This method is used to get value of the parameter:
     * playerTypedMessage.
     * </p>
     *
     * <p>
     * 此方法用于修改本类中的属性：玩家输入信息
     *</p>
     *
     * @return value of the parameter: playerTypedMessage.
     */
    public static String getPlayerTypedMessage() {
        return playerTypedMessage;
    }


    /**
     * <p>
     * This method is used to get value of the parameter:
     * gameFlow.
     * </p>
     *
     * <p>
     * 此方法用于返回本类中的属性：游戏时间戳
     *</p>
     *
     * @return the running time of this project
     */
    public static int getGameFlow() {
        return gameFlow;
    }


    /**
     * <p>
     * This method is used to set gameFlow to a new
     * designated number.
     * </p>
     *
     * <p>
     * 此方法用于修改本类中的属性：游戏时间戳
     * </p>
     *
     * @param gameFlow the running time of this project.
     */
    public static void setGameFlow(int gameFlow) {
        ServerModel.gameFlow = gameFlow;
    }


    /**
     * <p>
     * This method is used to set playerTypedMessage to a new
     * designated number.
     * </p>
     *
     * <p>
     * 此方法用于修改本类中的属性：玩家输入信息
     * </p>
     *
     * @param playerTypedMessage text typed by player that is
     *                           in TextField of GameFrame.
     */
    public static void setPlayerTypedMessage(String playerTypedMessage) {
        ServerModel.playerTypedMessage = playerTypedMessage;
    }

    /**
     * <p>
     * This method is used to set p1 to a new
     * designated number.
     * </p>
     *
     * <p>
     * 此方法用于返回本类中的属性：客户端玩家信息
     *</p>
     *
     * @param p1 player on server side.
     */
    public static void setP1(ServerPlayer p1) {
        ServerModel.p1 = p1;
    }

    /**
     * <p>
     * This method is used to set p2 to a new
     * designated number.
     * </p>
     *
     * <p>
     * 此方法用于返回本类中的属性：服务端玩家信息
     *</p>
     *
     * @param p2 player on client side.
     */
    public static void setP2(ServerPlayer p2) {
        ServerModel.p2 = p2;
    }
}