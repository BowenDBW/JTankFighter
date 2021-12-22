package com.CommunicateUnit.ClientPack;


import com.ProcessUnit.ClientPack.ClientLevel;
import com.ProcessUnit.ClientPack.ClientModel;
import com.ProcessUnit.ClientPack.ClientStatus;
import com.SourceUnit.ClientPack.ClientGameComponent;
import com.UI.ClientDrawingPanel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The type Client communication.
 *
 * @author chenhong
 * @version 1.0
 * @description 用于通讯
 * @date 2021 /12/17 21:21
 */
public class ClientCommunication {

    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static String serverIp;

    /**
     * Gets client socket.
     * 获取客户端socket
     * @return the client socket
     */
    public static Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Gets out.
     * 获取流
     * @return the out
     */
    public static PrintWriter getOut() {
        return out;
    }

    /**
     * Gets in.
     * 获取流
     * @return the in
     */
    public static BufferedReader getIn() {
        return in;
    }

    /**
     * Sets server ip.
     * 设置ip
     * @param serverIp the server ip
     */
    public static void setServerIp(String serverIp) {
        ClientCommunication.serverIp = serverIp;
    }

    /**
     * Connect server.
     * 根据ip来连接对应的主机，并且初始化流和socket
     */
    public static void connectServer() {

        ClientDrawingPanel.addMessage("正在连接主机");
        serverIp = ClientModel.getView().getIpField().getText();
        try {
            //连接主机并初始化流
            InetAddress address = InetAddress.getByName(serverIp);
            clientSocket = new Socket(address, 9999);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            ClientModel.getT().stop();
            e.printStackTrace();
            ClientDrawingPanel.addMessage("连接中出现错误， 请确认 1. 输入的IP是否正确,   2. 主机端已存在");
            return;
        }

        //主机连接成功
        ClientStatus.setServerConnected(true);

        ClientDrawingPanel.addMessage("已成功连接到主机，开始载入游戏");

        ClientModel.getView().getIpField().setFocusable(false);
        ClientModel.getView().getIpField().setEnabled(false);

        //加载游戏 texture
        ClientLevel.loadPictures();


        ClientStatus.setGameStarted((true));

        ClientStatus.setGameStarted(true);
        ClientDrawingPanel.drawingList = new ClientGameComponent[400];
        ClientModel.getView().getMessageField().setEnabled(true);
        ClientDrawingPanel.addMessage("载入完毕，游戏开始了！");
    }

}
