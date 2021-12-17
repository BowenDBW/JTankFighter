package com.client.ClientUnit;


import java.io.BufferedReader;
import java.io.IOException;
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

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }


    /**
     * 连接主机并初始化流
     * @param serverIP
     * @throws IOException
     */
    public void connectServer(String serverIP) throws IOException {
        InetAddress address = InetAddress.getByName(serverIP);
        clientSocket = new Socket(address, 9999);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

}
