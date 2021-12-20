package com.CommunicateUnit;

/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/17 21:52
 */
public class Instruction {

    private static StringBuffer FROM_USER = new StringBuffer();

    private static StringBuffer FROM_SEVER = new StringBuffer();

    public static StringBuffer getFromUser() {

        return FROM_USER;
    }

    public static StringBuffer getFromSever(){

        return FROM_SEVER;
    }

    public static void setFromSever(StringBuffer stringBuffer) {

        FROM_SEVER = stringBuffer;
    }

    public static void setFromUser(StringBuffer stringBuffer) {

        FROM_USER = stringBuffer;
    }
}
