package com.ProcessUnit.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenhong
 * @version 1.0
 * @description TODO
 * @date 2021/12/21 19:03
 */
public abstract class Level {

    public static String[] readArray(String choice){

        //声明字符输入流
        FileReader reader = null;
        //声明字符输入缓冲流
        BufferedReader readerBuf = null;
        //声明二维数组
        String[] array = null;

        try {
            //指定读取路径
            reader = new FileReader(choice);
            //通过BufferedReader包装字符输入流
            readerBuf = new BufferedReader(reader);
            //存放读取的文件的数据
            List<String> strList = new ArrayList<>();
            //存放一行的数据
            String lineStr;
            //逐行读取文件
            while((lineStr = readerBuf.readLine()) != null) {
                //读取的行添加到list中
                strList.add(lineStr);
            }
            //获取文件行
            int lineNum = strList.size();
            //获取数组列
            String s =  strList.get(0);

            int columnNum = s.split(",").length;
            //根据文件行数创建对应的数组
            int num= lineNum * columnNum;
            array = new String[num];

            int j = 0;
            //循环遍历集合，将集合中的数据放入数组中
            for (String value : strList) {
                // 将读取的str按照","分割，用字符串数组来接收
                String[] strings = value.split(",");

                for (String str : strings) {
                    array[j] = str;
                    j++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭字符输入流
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //关闭字符输入缓冲流
            try {
                if(readerBuf != null) {
                    readerBuf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

}
