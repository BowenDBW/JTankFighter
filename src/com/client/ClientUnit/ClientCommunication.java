package com.client.ClientUnit;


import com.client.ConponentPack.Actor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/17 21:21
 */
public class ClientCommunication {

    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    private static String serverIP;

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static void setClientSocket(Socket clientSocket) {
        ClientCommunication.clientSocket = clientSocket;
    }

    public static PrintWriter getOut() {
        return out;
    }

    public static void setOut(PrintWriter out) {
        ClientCommunication.out = out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static void setIn(BufferedReader in) {
        ClientCommunication.in = in;
    }

    public static String getServerIP() {
        return serverIP;
    }

    public static void setServerIP(String serverIP) {
        ClientCommunication.serverIP = serverIP;
    }

    public static void connectServer() {

        DrawingPanel.addMessage("正在连接主机");
        serverIP = ClientModel.getView().getIpField().getText();
        try {
            //连接主机并初始化流
            InetAddress address = InetAddress.getByName(serverIP);
            clientSocket = new Socket(address, 9999);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            ClientModel.getT().stop();
            e.printStackTrace();
            DrawingPanel.addMessage("连接中出现错误， 请确认 1. 输入的IP是否正确,   2. 主机端已存在");
            return;
        }

        //主机连接成功
        Status.setServerConnected(true);

        DrawingPanel.addMessage("已成功连接到主机，开始载入游戏");

        ClientModel.getView().getIpField().setFocusable(false);
        ClientModel.getView().getIpField().setEnabled(false);

        //加载游戏 texture
        Level.loadPictures();


        Status.setGameStarted((true));

        Status.setGameStarted(true);
        DrawingPanel.drawingList = new Actor[400];
        ClientModel.getView().getMessageField().setEnabled(true);
        DrawingPanel.addMessage("载入完毕，游戏开始了！");
    }

}
