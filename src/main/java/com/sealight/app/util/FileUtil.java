package com.sealight.app.util;

import com.sealight.app.bean.Constants;

import java.io.File;

/**
 * 文件类
 *
 * @author zhangjj
 * @create 2017-12-13 11:39
 **/
public class FileUtil {

    public static boolean exist(String path){
        File file = new File(path);
        if(file.exists()){
            return true;
        }
        return false;
    }

    public static boolean xlsExist(String make){
        return exist(Constants.XLS_FILE_PATH + Constants.FILE_SIGN + make + ".xls");
    }
}
