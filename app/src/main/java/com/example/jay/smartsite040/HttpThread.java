package com.example.jay.smartsite040;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jay on 2017/3/11.
 */
public class HttpThread  extends Thread {
    private String mUrl;

    public HttpThread(String url){
        this.mUrl = url;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            URL httpUrl = new URL(mUrl);
            Log.e("http","in HttpThread! url is:"+mUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            //get请求的话默认就行了，post请求需要setDoOutput(true)，这个默认是false的。
//          conn.setDoInput(true);
//          conn.setDoOutput(true);
            InputStream in = conn.getInputStream();

            File downloadFile;
            File sdFile;
            FileOutputStream out = null;
            //截取文件名
            String filename = mUrl.substring(mUrl.lastIndexOf("/")+1,mUrl.length());
            Log.e("http","filename is:"+filename);
            //判断SD卡
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                downloadFile = Environment.getExternalStorageDirectory();
//                sdFile = new File(downloadFile, "2myfile/"+filename);

                String targetDir = config.downloadPath;
                File file = new File(targetDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
//                fileFullName = targetDir + "/" + filename;
                sdFile = new File(file, filename);
                out = new FileOutputStream(sdFile);
            }
            else{
                Log.e("http","需要SD卡");
//                Toast t=Toast.makeText(MainActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER, 0, 0);
//                t.show();
                return;
            }
            //buffer 6k
            byte[] b = new byte[6*1024];
            int len;

            while((len=in.read(b))!=-1){
                if(out!=null){
                    out.write(b, 0, len);
                }
            }
            if(out != null){
                out.close();
            }

            if(in != null){
                in.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
