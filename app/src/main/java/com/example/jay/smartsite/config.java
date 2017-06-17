package com.example.jay.smartsite;

import android.os.Environment;

/**
 * Created by Jay on 2017/3/15.
 */
public class config {

    public static String databaseid="未知";                                              //平板设备的数据库索引
    public static String androidpadcol=android.os.Build.SERIAL;                            //平板设备的串号
    public static String androidname="未知";                                             //平板设备的岗位名称
    public static String androidposition="未知";                                         //平板设备的岗位等级
    public static String androidlevel="未知";                                            //平板设备的岗位编号
    public static String androididposition="未知";                                       //平板设备的岗位权限

    public static String sdcardPath=Environment.getExternalStorageDirectory().getAbsolutePath();    //内存卡绝对路径"/storage/emulated/0"
    public static String basePath=sdcardPath+"/2smartsite";                                 //项目文件在本地存储的根目录
    public static String downloadPath=basePath+"/myfile";                                   //文件下载的目录
    public static String photoPath=basePath+"/myphoto";                                     //照片存储的目录

    public static String ServerIP="http://202.119.84.51:9090";
    public static String ProjectName="/smartsite";
    public static String ServerURL=ServerIP+ProjectName+"/index.php/home";        //服务器地址
    public static String downloadURL=ServerIP+ProjectName+"/Public/Uploads/";               //文件下载
    public static String serverIntroductionURL=ServerURL+"/project/introduction/gethtml";   //工地简介
    public static String registerURL=ServerURL+"/common/user/checkIsRegister/username/12345";   //测试是否注册
    public static String serverFileInfoURL=ServerURL+"/document/index/getById";             //文件详情
    public static String serverFileURL=ServerURL+"/document/file/get";                      //文件列表
    public static String serverTypeURL=ServerURL+"/document/type/getchild/id";              //文件分类
    public static String serverTypeListURL=ServerURL+"/document/type/get";                  //获得所有级别分类
    public static String serverRecordURL=ServerURL+"/monitor/staff/add";                    //同步记录
    public static String serverStafflogURL=ServerURL+"/monitor/staff/getstafflog/id";       //用户浏览记录
    public static String uploadURL=ServerURL+"/report/index/save";                          //文件上传
    public static String staffURL=ServerURL+"/staff/index/get";                             //员工信息列表
    public static String serverUserURL=ServerURL+"/staff/pad/get";                          //用户列表
    public static String serverPadInfoURL=ServerURL+"/staff/pad/getById";                   //平板详情
    public static String serverImeiURL=ServerURL+"/staff/pad/getByPadcol/imei";             //根据imei获取平板详情

    public static String localBase="file:///android_asset/";                                //本地资源根目录
    public static String localErrorPage=localBase+"error.html";                             //出错页面


//    private static String[] documentCategory={"图纸CAD","技术控制","安全控制","项目进度","计划物资","现场监控","操作记录"};   //一级文档分类（默认）
//    private static String[] documentOrder={"52","53","54","56","78","89","90"};                                         //一级文档序号（默认）
//    public static ArrayList<String> documentCategoryList=new ArrayList<>(Arrays.asList(documentCategory));
//    public static ArrayList<String> documentOrderList=new ArrayList<>(Arrays.asList(documentOrder));
}
