package com.example.jay.smartsite;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


/**
 * Created by Jay on 2017/3/13.
 */
public class systemUtils {

    private Context mContext;

    public systemUtils(Context context){
        this.mContext = context;
    }

    //webview中调用toast原生组件
    //添加了 @JavascriptInterface 注解的方法才能被调用
    @JavascriptInterface
    public void showToast(String toast) { Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show(); }

    //系统控制台输出
    @JavascriptInterface
    public void consolelog(String log){
        Log.e("http", "console log:"+log);
    }

    @JavascriptInterface
    public void startDownloadActivity(String filekey) {     //启动下载activity：EmptyActivity
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putString("filekey", filekey);
        Intent intent = new Intent(mContext,EmptyActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public String getServerUrl(){
        return config.ServerURL;
    }
    @JavascriptInterface
    public String getServerRecordUrl(){
        return config.serverRecordURL;
    }
    @JavascriptInterface
    public String getServerFileUrl(){
        return config.serverFileURL;
    }
    @JavascriptInterface
    public String getServerIntroductionUrl(){
        return config.serverIntroductionURL;
    }
    @JavascriptInterface
    public String getServerImeiUrl(){
        return config.serverImeiURL;
    }
    @JavascriptInterface
    public String getServerTypeUrl(){
        return config.serverTypeURL;
    }
    @JavascriptInterface
    public String getServerTypeListUrl(){ return config.serverTypeListURL; }
    @JavascriptInterface
    public String getServerUserUrl(){
        return config.serverUserURL;
    }
    @JavascriptInterface
    public String getServerStafflogUrl(){ return config.serverStafflogURL; }
    @JavascriptInterface
    public String getServerFileInfoUrl(){
        return config.serverFileInfoURL;
    }
    @JavascriptInterface
    public String getServerPadInfoUrl(){
        return config.serverPadInfoURL;
    }
    @JavascriptInterface
    public String getLocalErrorPage(){
        return config.localErrorPage;
    }
    @JavascriptInterface
    public String getPhotoPath(){
        return config.photoPath;
    }
    @JavascriptInterface
    public String getUploadURL(){
        return config.uploadURL;
    }
    @JavascriptInterface
    public String getDownloadURL(){
        return config.downloadURL;
    }
    @JavascriptInterface
    public String getStaffUrl(){ return config.staffURL; }
    @JavascriptInterface
    public String getRegisterUrl(){ return config.registerURL; }
    @JavascriptInterface
    public String getAndroidDatabaseId(){ return config.databaseid; }
    @JavascriptInterface
    public String getAndroidPadCol(){ return config.androidpadcol; }
    @JavascriptInterface
    public String getAndroidName(){
        return config.androidname;
    }
    @JavascriptInterface
    public String getAndroidPosition(){
        return config.androidposition;
    }
    @JavascriptInterface
    public String getAndroidLevel(){
        return config.androidlevel;
    }
    @JavascriptInterface
    public String getAndroidIdPosition(){ return config.androidposition; }



    @JavascriptInterface
    public void setAndroidDatabaseId(String id){
        config.databaseid=id;
    }
    @JavascriptInterface
    public void setAndroidName(String name){
        config.androidname=name;
    }
    @JavascriptInterface
    public void setAndroidPosition(String position){
        config.androidposition=position;
    }
    @JavascriptInterface
    public void setAndroidLevel(String level){
        config.androidlevel=level;
    }
    @JavascriptInterface
    public void setAndroidIdPosition(String idposition){ config.androididposition=idposition; }


    /**
     * 检测是否已经连接网络。
     * @return 当且仅当连上网络时返回true,否则返回false。
     */
    @JavascriptInterface
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info==null){
            return false;
        }
        else if (info.getType()==ConnectivityManager.TYPE_WIFI){
//            Log.e("http", "wifi已连接！" );
            return (info != null) && info.isAvailable();
        }
        return false;
    }

    //通知SD卡重新挂载，用于照片更新时使用
    public void broadcastPhotoInfo(Uri photouri){
//        Log.e("http", "in broadcastPhotoInfo! photouri:"+photouri);
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photouri));
    }

    //检查摄像头权限
    @JavascriptInterface
    public boolean isCameraGranted() {
        return ContextCompat.checkSelfPermission(mContext,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED;
    }
}
