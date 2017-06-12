package com.example.jay.smartsite040;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public void showToast(String toast) {
		//Log.e("http", "Toast:"+toast);
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public void sActivity(String filekey) {
//        mContext.startActivity(intent);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("filekey", filekey);
        Intent intent = new Intent(mContext,EmptyActivity.class);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
//        mContext.startActivity(new Intent(mContext,EmptyActivity.class));
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
    public String getServerUserUrl(){
        return config.serverUserURL;
    }
    @JavascriptInterface
    public String getServerStafflogUrl(){
        return config.serverStafflogURL;
    }
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

//    @JavascriptInterface
//    public String getActivatedStatus(){
//        return config.isactivated;
//    }

//    @JavascriptInterface
//    public void setActivatedStatus(String status){
//        config.isactivated=status;
//    }   //激活设备

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
//    @JavascriptInterface
//    public boolean isWifiConnected() {
//        if (mContext != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (mWiFiNetworkInfo != null) {
//                return mWiFiNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }

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
//        Log.e("http", "info: "+info );
        if (info==null){
//            Log.e("http", "info为空" );
            return false;
        }
        else if (info.getType()==ConnectivityManager.TYPE_WIFI){
//            Log.e("http", "wifi已连接！" );
            return (info != null) && info.isAvailable();
        }
//        Log.e("http", "其他连接方式！" );
        return false;
    }



    public void broadcastPhotoInfo(Uri photouri){
//        Log.e("http", "in broadcastPhotoInfo! photouri:"+photouri);
        //通知SD卡重新挂载
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photouri));
//        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/storage/emulated/0/2smartsite/myphoto/IMG_20170611_210132.jpg"))));

    }

}
