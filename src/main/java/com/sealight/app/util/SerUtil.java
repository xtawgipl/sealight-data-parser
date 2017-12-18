package com.sealight.app.util;

import java.io.*;

/**
 * 序列化对象工具类
 *
 * @author zhangjj
 * @create 2017-12-16 16:05
 **/
public class SerUtil {

    /**
     * 定义方法把对象的信息写到硬盘上------>对象的序列化。
     * @param
     * @author zhangjj
     * @Date 2017/12/16 16:01
     * @return
     * @exception
     */
    public static void writeObject(String path, Object obj) {
        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(path);//创建文件字节输出流对象
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 把文件中的对象信息读取出来-------->对象的反序列化
    * @param
    * @author zhangjj
    * @Date 2017/12/16 16:05
    * @return
    * @exception
    */
    public static <T> T readObject(String path, Class<T> clazz) {
        FileInputStream inputStream = null;//创建文件字节输出流对象
        ObjectInputStream objectInputStream = null;
        T obj = null;
        try {
            inputStream = new FileInputStream(path);//创建文件字节输出流对象
            objectInputStream = new ObjectInputStream(inputStream);
            obj = clazz.cast(objectInputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
