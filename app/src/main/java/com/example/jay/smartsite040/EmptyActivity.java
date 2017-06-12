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
//        setContentView(R.layout.login_activity);
//        setVisible(false);
//        String filekey = "58c9285fb1db1.pdf";
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        String filekey = bundle.getString("filekey");
//        String fileurl=config.downloadPath+filekey;
        openlocalfile(filekey);


    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("http", "EmptyActivity Pause");
//        Log.e("http", "EmptyActivity Resume");
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
//            SharedPreferences sharedPreferences = this.getSharedPreferences("filereadtime", Context.MODE_PRIVATE);
//            String opentime = sharedPreferences.getString(openfilekey+"_open", "");
//            Log.e("http", "opentime："+opentime);
//            Log.e("http", "所有sharedPreferences："+sharedPreferences.getAll());
            //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值

//            if(opentime!=null || !opentime.equals("")){
//                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                long diff = 0;
//                try {

//                    Date beforetime = sDateFormat.parse(opentime);
//
//                    Log.e("http", "beforetime为："+beforetime);
//
//                    String now = sDateFormat.format(new java.util.Date());
//                    Date nowtime = sDateFormat.parse(now);
//                    diff = (nowtime.getTime() - beforetime.getTime() )/1000;//这样得到的差值是秒级别

                    //保存文件打开时间，标志位1
                    int size=new fileUtils(this).getRecordsSize();
                    new fileUtils(this).saveFileRecords(new Date(),openfilekey,1,size);
                    new fileUtils(this).saveRecordsSize(size+1);  //记录数加1
//					Toast.makeText(getActivity(), "您从" +beforetime+ "开始阅读，到" +nowtime +"为止，共阅读了"+diff+"秒", 1).show();
//                    Toast.makeText(this, "您已阅读了"+diff+"秒", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "您在"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+"秒结束阅读！", Toast.LENGTH_SHORT).show();
//                } catch (ParseException e) {
//                    diff=-1;
//                }
//                Log.e("http" , "您已观看文件"+diff+"秒");
                this.finish();
//            }
            openflag=false;
            openfilekey="";
        }
        else{	//其他系统恢复状态

        }
    }

    public void openlocalfile(String filekey){
//        openflag=true;
        openfilekey=filekey;	//保存当前打开文件名

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());

        //界面失去控制权时保存数据
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("filereadtime", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//        editor.putString(openfilekey+"_open", date);
//        editor.commit();//提交修改

        //保存文件打开时间，标志位1
        new fileUtils(this).saveFileRecords(new Date(),filekey,0,new fileUtils(this).getRecordsSize());

        //提示成功
        Toast.makeText(this, "打开时间"+date, Toast.LENGTH_SHORT).show();
        ////        Toast.makeText( getApplicationContext(), "打开时间"+date, Toast.LENGTH_SHORT).show();
        //或者用 getApplicationContext()


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
