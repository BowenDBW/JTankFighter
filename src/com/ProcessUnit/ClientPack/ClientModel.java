package com.ProcessUnit.ClientPack;

import com.CommunicateUnit.ClientPack.ClientCommunication;
import com.ProcessUnit.LogicalLoop;
import com.ProcessUnit.Ticker;
import com.UI.ClientDrawingPanel;
import com.UI.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>
 *     This class is for client to load a level and initialize
 *     the game once server and client get connection and connect
 *     is proved to be valid.ClientModel functions by load and
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
 *     此类用于客户端加载关卡并初始化游戏一旦
 *     服务器和客户端获得连接并且连接被证明是有效的。
 *     ClientModel 功能通过加载和处理地图文件来放置每个游戏
 *     组件到适当的位置。
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
 * @version 1.1 修复了一些 bug
 * @version 1.0 初始版本
 **/
public class ClientModel implements ActionListener{

    /**
     * TimeStamp of this project
     *
     * 这个游戏运行的时间戳
     */
    private static int gameFlow;


    /**
     * an object of class ClientView used to display
     * game frame.
     *
     * 一个 ClientView 类的对象，用作显示游戏界面。
     */
    private static ClientView view;


    /**
     * A place to store user typed message
     *
     * 一个用于临时存储用户输入消息的字符串属性
     */
    public static String playerTypedMessage = "";


    /**
     *   实际的游戏运行在这个线程,而主线程听用户的输入
     */
    private static Ticker t;


    /**
     * This method is used for initialize ClientModel,
     * ClientModel is where the connect action begin.
     * When user click setup server button, its function
     * is to call all methods associated with a connection
     * event.
     *
     * 该方法用于初始化ClientModel，ClientModel 是连接操作开始的地方。
     * 当用户点击设置服务器按钮时，其功能是调用与连接服务器有关的所有方法。
     *
     * @param thisView frame of this project
     */
    public ClientModel(ClientView thisView) {
        view = thisView;
        ClientDrawingPanel.messageQueue = new String[8];
        ClientDrawingPanel.addMessage("欢迎来到坦克大战用户端！请输入主机IP地址然后点击\"连接主机\"按钮开始游戏");
        t = new Ticker(1000);
        t.addActionListener(this);
    }


    /**
     * This method is used to set gameFlow to a new
     * designated number.
     *
     * 此方法用于修改本类中的属性：游戏时间戳
     * @param gameFlow the running time of this project.
     */
    public static void setGameFlow(int gameFlow) {
        ClientModel.gameFlow = gameFlow;
    }


    /**
     * This method is used to get value of the parameter:
     * playerTypedMessage.
     *
     * 此方法用于返回本类中的属性：玩家输入信息
     * @return value of the parameter: playerTypedMessage.
     */
    public static String getPlayerTypedMessage() {
        return playerTypedMessage;
    }


    /**
     * This method is used to set playerTypedMessage to a new
     * designated number.
     *
     * 此方法用于修改本类中的属性： 玩家输入信息
     * @param playerTypedMessage text typed by player that is
     *                           in TextField of GameFrame.
     */
    public static void setPlayerTypedMessage(String playerTypedMessage) {
        ClientModel.playerTypedMessage = playerTypedMessage;
    }


    /**
     * This method is used to get value of the parameter:
     * gameFlow.
     *
     * 此方法用于返回本类中的属性： 游戏时间戳
     * @return value of the parameter: gameFlow.
     */
    public static int getGameFlow() {
        return gameFlow;
    }


    /**
     * This method is used to get value of the parameter:
     * view.
     *
     * 此方法用于返回本类中的属性： 游戏界面
     * @return value of the parameter: view.
     */
    public static ClientView getView() {
        return view;
    }


    /**
     * This method is used to get value of the parameter:
     * t. T is a object of Ticker
     *
     * 此方法用于返回本类中的属性： 游戏线程
     * @return value of the parameter: t.
     */
    public static Ticker getT() {
        return t;
    }


    /**
     * This method is used for responding to
     * players' commands of connecting to the
     * server, once it connected, it will go
     * into the logic loop.
     *
     * 此方法用于响应玩家的连接命令,
     * 服务器一旦连接，它就会去进入逻
     * 辑循环。
     *
     * @param e a event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ClientCommunication.connectServer();
        //如果程序不能连接到服务器然后什么都不做
        if (ClientStatus.isServerConnected()) {
            return;
        }
        LogicalLoop.clientLogic();
    }
}
