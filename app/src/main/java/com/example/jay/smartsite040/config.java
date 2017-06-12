package com.example.jay.smartsite040;

import android.os.Environment;

/**
 * Created by Jay on 2017/3/15.
 */
public class config {
//    public static String ServerUrl = "http://192.168.1.118:8888/smartsite/";
//    public static String basePath="/storage/emulated/0/2smartsite/";
    //设备信息
//    public static String isactivated="no";
//	public static String androidid=android.os.Build.SERIAL;

    public static String databaseid="unknown";
    public static String androidpadcol=android.os.Build.SERIAL;
    public static String androidname="unknown";
    public static String androidposition="unknown";
    public static String androidlevel="unknown";
    public static String androididposition="unknown";

    public static String sdcardPath=Environment.getExternalStorageDirectory().getAbsolutePath();    //"/storage/emulated/0"
    public static String basePath=sdcardPath+"/2smartsite";
    public static String downloadPath=basePath+"/myfile";
    public static String photoPath=basePath+"/myphoto";
//    public static String photoPath=sdcardPath+"/DCIM/Camera";

    public static String ServerURL="http://202.119.84.51:9090/smartsite/index.php/home";
    public static String serverRecordURL=ServerURL+"/monitor/staff/add";
    public static String serverFileURL=ServerURL+"/document/file/get";
    public static String serverIntroductionURL=ServerURL+"/project/introduction/gethtml";

    public static String serverImeiURL=ServerURL+"/staff/pad/getByPadcol/imei";



    public static String localBase="file:///android_asset/";
    public static String localErrorPage=localBase+"error.html";

    public static String uploadURL = "http://202.119.84.51:9090/smartsite/index.php/home/report/index/save";
    public static String downloadURL = "http://202.119.84.51:9090/smartsite/Public/Uploads/";
}
