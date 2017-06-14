package com.example.jay.smartsite040;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zcy
 *
 */
public class EmptyActivity extends Activity {
    private boolean openflag;
    private String openfilekey;    //保存当前打开的文件名

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("http", "EmptyActivity onCreate!");
        openflag = false;
        openfilekey = "";
        Bundle bundle = this.getIntent().getExtras();       //新页面接收数据
        String filekey = bundle.getString("filekey");
        openlocalfile(filekey);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("http", "EmptyActivity Pause");
        if (!openfilekey.equals("")) {
            openflag = true;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("http", "Destroy");
        new fileUtils(this).checkAllFileRecords();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("http", "EmptyActivity Resume");
        Log.e("http", "openflag："+openflag+"    openfilekey:"+openfilekey);
        if(openflag==true && !openfilekey.equals("")){	//文件处于打开状态

            //保存文件打开时间，标志位1
            int size=new fileUtils(this).getRecordsSize();
            new fileUtils(this).saveFileRecords(new Date(),openfilekey,1,size);
            new fileUtils(this).saveRecordsSize(size+1);  //记录数加1
//			Toast.makeText(getActivity(), "您从" +beforetime+ "开始阅读，到" +nowtime +"为止，共阅读了"+diff+"秒", 1).show();
//          Toast.makeText(this, "您已阅读了"+diff+"秒", Toast.LENGTH_SHORT).show();
//          Toast.makeText(this, "您在"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+"秒结束阅读！", Toast.LENGTH_SHORT).show();
//          Log.e("http" , "您已观看文件"+diff+"秒");
            this.finish();
            openflag=false;
            openfilekey="";
        }
        else{	//其他系统恢复状态

        }
    }

    public void openlocalfile(String filekey){
        openfilekey=filekey;	//保存当前打开文件名
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        //保存文件打开时间，标志位1
        new fileUtils(this).saveFileRecords(new Date(),filekey,0,new fileUtils(this).getRecordsSize());
        //Toast.makeText(this, "打开时间"+date, Toast.LENGTH_SHORT).show();     //提示成功
        try{
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String localPath = config.downloadPath+"/"+filekey;
            File currentFile = new File(localPath);
            Uri uri = Uri.fromFile(currentFile);
            //根据文件类型打开文件
            if(filekey.contains(".pdf")){
                intent.setDataAndType(uri, "application/pdf");
                Log.e("http", "open pdf file");
            }
            else if(filekey.contains(".jpg")){
                intent.setDataAndType(uri, "image/*");
                Log.e("http", "open jpg file");
            }
            else if(filekey.contains(".dwg")){
                intent.setDataAndType(uri, "application/x-autocad");
                Log.e("openFile", "open dwg file");
            }
            else{
                intent.setDataAndType(uri, "text/plain");
                Log.e("openFile", "open txt file");
            }
            startActivity(intent);
        }
        catch(Exception e){
            Log.e("http", "文件打开错误");
            openflag=false;
            openfilekey="error";
        }
    }

}
