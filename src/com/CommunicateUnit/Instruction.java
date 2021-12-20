package com.CommunicateUnit;

/**
 * The type Instruction.
 *
 * @author chenhong
 * @version 1.0
 * @description 接受指令
 * @date 2021 /12/17 21:52
 */
public class Instruction {

    private static StringBuffer FROM_USER = new StringBuffer();

    private static StringBuffer FROM_SEVER = new StringBuffer();

    /**
     * Gets from user.
     * 获取
     * @return the form user
     */
    public static StringBuffer getFromUser() {

        return FROM_USER;
    }

    /**
     * Get from sever string buffer.
     * 获取
     * @return the string buffer
     */
    public static StringBuffer getFromSever(){

        return FROM_SEVER;
    }

    /**
     * Sets from sever.
     * 设置
     * @param stringBuffer the string buffer
     */
    public static void setFromSever(StringBuffer stringBuffer) {

        FROM_SEVER = stringBuffer;
    }

    /**
     * Sets from user.
     * 设置
     * @param stringBuffer the string buffer
     */
    public static void setFromUser(StringBuffer stringBuffer) {

        FROM_USER = stringBuffer;
    }
}
