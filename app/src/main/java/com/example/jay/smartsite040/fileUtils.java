package com.example.jay.smartsite040;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jay on 2017/3/13.
 */
public class fileUtils {


    private Context mContext;


    public fileUtils(Context context){
        this.mContext = context;
    }

    //添加了 @JavascriptInterface 注解的方法才能被调用
    @JavascriptInterface
    public void setFileInfo(String filename,String savename){
        //保存文件列表信息
        SharedPreferences filePreferences= mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);
        SharedPreferences.Editor fileeditor = filePreferences.edit();//获取编辑器

        fileeditor.putString(savename, filename);
        fileeditor.commit();//提交修改
    }

    @JavascriptInterface
    public void getAllFileInfo(){
        //保存文件列表信息
        SharedPreferences filePreferences= mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);
//        SharedPreferences.Editor fileeditor = filePreferences.edit();//获取编辑器

        Map<String, ?> allContent = filePreferences.getAll();
        String content="";
        //注意遍历map的方法
        for(Map.Entry<String, ?>  entry : allContent.entrySet()){
            content+=(entry.getKey()+":"+entry.getValue())+"\n";
        }
        Log.e("http", "getAllFileInfo:"+content);
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
//        fileeditor.commit();//提交修改
    }

    @JavascriptInterface
    public String getFileList(){
//        Log.e("http", "getSpecFileList! " );
        SharedPreferences filePreferences= mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);
//        SharedPreferences.Editor fileeditor = filePreferences.edit();//获取编辑器

        Map<String, ?> allContent = filePreferences.getAll();
//        String content="";

        JSONObject map;
        JSONArray array = new JSONArray();
        //注意遍历map的方法
        try {
            for (Map.Entry<String, ?> entry : allContent.entrySet()) {
//                content += (entry.getKey() + ":" + entry.getValue()) + "\n";
                map = new JSONObject();
                map.put(entry.getKey(), entry.getValue());
                array.put(map);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("http", "getFileList: "+array );
        return array.toString();
    }

	public List<String> getLocalFilesList(){	//获取文件列表，供本地使用
		SharedPreferences filePreferences= mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);

		Map<String, ?> allContent = filePreferences.getAll();
		List<String> list = new ArrayList();
//        String content="";

		JSONObject map;
		//注意遍历map的方法
		for (Map.Entry<String, ?> entry : allContent.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}

	@JavascriptInterface
	public String getPhotoList(){	//获取图片列表
		File dir = new File(config.photoPath);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return null;
		}

		boolean flag=false;		//是否找到同一个key
		JSONObject map,temp;
		List photolist;
		JSONArray array = new JSONArray();
		//注意遍历map的方法
		File[] list = dir.listFiles();
		String filedate;
		try {
			for(int i=0;i<list.length;i++){
				map = new JSONObject();
				photolist = new ArrayList();
				filedate = list[i].getName().substring(4,12);
				photolist.add(list[i].getName());

//				Log.e("http", "------------------------------------------filedate: "+filedate );
				for (int j=0;j<array.length();j++){
					temp = (JSONObject)array.get(j);
//					Log.e("http", "temp: "+temp );
					if(!temp.isNull(filedate)) {        //可以用isNull(key)来判断是否含有某个key
						((List)((JSONObject)array.get(j)).get(filedate)).add(list[i].getName());
						flag=true;
						break;
					}
				}
				if (!flag){
					map.put(filedate, photolist);
					array.put(map);
				}
				flag=false;		//继续下一次查找

			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("http", "photoarray: "+array );
		return array.toString();
	}

    @JavascriptInterface
    public String getSpecFileList(String type){
//        Log.e("http", "getSpecFileList! " );
        SharedPreferences filePreferences= mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);
//        SharedPreferences.Editor fileeditor = filePreferences.edit();//获取编辑器

        Map<String, ?> allContent = filePreferences.getAll();
//        String content="";

        JSONObject map;
        JSONArray array = new JSONArray();
        //注意遍历map的方法
        try {
            for (Map.Entry<String, ?> entry : allContent.entrySet()) {
//                Log.e("http", "in getSpecFileList: "+entry.getValue().toString() );
                map = new JSONObject();
                if(entry.getKey().toString().contains(type)){
                    map.put(entry.getKey(), entry.getValue());
                    array.put(map);
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("http", "getSpecFileList: "+array );
        return array.toString();
    }


    @JavascriptInterface
    public void openlocalfile(String filekey){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        //界面失去控制权时保存数据
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("zcy", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//        editor.putString(openfilename, date);
//        editor.commit();//提交修改
        //提示成功
        Toast.makeText(mContext, "打开时间"+date, Toast.LENGTH_SHORT).show();
        ////        Toast.makeText( getApplicationContext(), "打开时间"+date, Toast.LENGTH_SHORT).show();
        //或者用 getApplicationContext()

        try{
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            String localPath = config.downloadPath+filekey;
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
            mContext.startActivity(intent);
        }
        catch(Exception e){
            Log.e("http", "文件打开错误");
        }
    }

    //当前时间为id
//    public long getTimeId() {
//        Date date = new Date();
//        long time=date.getTime();
//        return time;
//    }

    public void saveRecordsSize(int size){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("size", size);
        editor.commit();//提交修改
    }

    public int getRecordsSize(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("size",0);
    }

    public void saveFileRecords(Date date,String key, int flag, int size){
        //保存文件的访问时间，0代表打开，1代表关闭，size代表序列号
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        //当前时间为id
        long time=date.getTime();
        editor.putString(size+"_"+flag, key+"_"+String.valueOf(time));
//        editor.putString(size+"_"+String.valueOf(time), key+"_"+flag);
        editor.commit();//提交修改
    }

    public void checkAllFileRecords(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        Map<String, ?> allContent = sharedPreferences.getAll();
//        String content="";

        JSONObject map;
        JSONArray array = new JSONArray();
        //注意遍历map的方法
        try {
            for (Map.Entry<String, ?> entry : allContent.entrySet()) {
//                content += (entry.getKey() + ":" + entry.getValue()) + "\n";
                map = new JSONObject();
                map.put(entry.getKey(), entry.getValue());
                array.put(map);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("http", "all contents in filerecords: "+array );
    }

    @JavascriptInterface
    public String getAllFileRecords(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        Map<String, ?> allContent = sharedPreferences.getAll();

        JSONObject map;
        JSONArray array = new JSONArray();
        //注意遍历map的方法
        try {
            for (Map.Entry<String, ?> entry : allContent.entrySet()) {
//                content += (entry.getKey() + ":" + entry.getValue()) + "\n";
                map = new JSONObject();
                map.put(entry.getKey(), entry.getValue());
                array.put(map);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("http", "filerecords: "+array );
        return array.toString();
    }

    @JavascriptInterface
    public void deleteLocalRecords(){
        SharedPreferences recordsharedPreferences= mContext.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor recordeditor = recordsharedPreferences.edit();//获取编辑器
        recordeditor.clear();		//清空
        recordeditor.commit();
    }

	@JavascriptInterface
	public String deleteLocalFile(String keys){		//删除本地文件，0表示未找到文件，1表示正常删除，-1表示异常
		Log.e("http", "deleteLocalFile! keys: "+keys );
		try {
			SharedPreferences filePreferences = mContext.getSharedPreferences("filelist", Context.MODE_PRIVATE);
			SharedPreferences.Editor fileeditor = filePreferences.edit();//获取编辑器
			fileeditor.remove(keys);
			fileeditor.commit();
			File dir = new File(config.downloadPath);
			if (dir == null || !dir.exists() || !dir.isDirectory()) {
//				return 0;
				return "系统路径异常，删除失败！";
			}
			for (File file : dir.listFiles()) {
				//Log.e("http", file.getName() );
				if (file.getName().equals(keys)) {
					file.delete();        // 删除指定文件
//					return 1;
					return "文件删除成功！";
				}
				//			if (file.isFile())
				//				file.delete(); // 删除所有文件
				//			else if (file.isDirectory())
				//				deleteDirWihtFile(file); // 递规的方式删除文件夹
			}
//			return 0;
			return "文件已失效！";
		}catch (Exception e){
			Log.e("http", "文件删除异常！" );
//			return -1;
			return "文件删除异常！";
		}
	}

	@JavascriptInterface
	public String deleteLocalPhotos(String[] photolist){		//删除本地文件，0表示未找到文件，1表示正常删除，-1表示异常
		Log.e("http", "deleteLocalPhotos! photolist: "+photolist[0] );
		File dir = new File(config.photoPath);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
//				return 0;
			return "系统路径异常，删除失败！";
		}
		int count=0;	//记录成功删除的文件数
		for (int i=0;i<photolist.length;i++) {
			try {
				for (File file : dir.listFiles()) {
					//Log.e("http", file.getName() );
					if (file.getName().equals(photolist[i])) {
						file.delete();        // 删除指定文件
						new systemUtils(mContext).broadcastPhotoInfo(Uri.fromFile(file));	//通知SD卡重新加载
						count++;
						break;
//					return 1;
//						return "文件删除成功！";
					}
				}
//				return "文件已失效！";
			} catch (Exception e) {
				Log.e("http", "文件删除异常！");
//				return "文件删除异常！";
			}
		}
		return "已成功删除"+count+"张照片";
//		return null;
	}
}
