package com.example.jay.smartsite040;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private long exitTime;  //记录退出时间
//    a、初始化 全局变量

//    private final static int FILECHOOSER_RESULTCODE = 1;
    private static final int FILE_SELECT_CODE = 0;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e("http", "onCreate! ");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);		//添加返回按钮
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {	//返回按钮添加点击响应事件
			@Override
			public void onClick(View v) {
				webView.goBack();
			}
		});
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setAppCacheEnabled(true);// 设置启动缓存
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上，会显示白边
		webView.addJavascriptInterface(new systemUtils(this), "SystemUtils");
		webView.addJavascriptInterface(new fileUtils(this), "FileUtils");

		final Handler mHandler = new Handler();
		webView.addJavascriptInterface(new Object(){
			@JavascriptInterface
			public String getAndroidPhoto() {//将被js调用
				Log.e("http","in getAndroidPhoto! ");
				mHandler.post(new Runnable() {
					public void run() {
						SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
						String nowtime=sdf.format(new Date());
						takePhoto("IMG_" + nowtime + ".jpg");	//自定义调用摄像头方法
						Log.e("http","========fileFullName: " + fileFullName);
					}
				});
				return fileFullName;
			}
			@JavascriptInterface
			public void setAndroidTitle(final String title) {//设置title，被js调用
				Log.e("http","in setAndroidTitle! ");
				mHandler.post(new Runnable() {
					public void run() {
						MainActivity.this.setTitle(title);
					}
				});
			}
		}, "AndroidUtils");

        WebSettings webSettings = webView.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setJavaScriptEnabled(true);					//允许运行JavaScript
		webSettings.setDatabaseEnabled(true);					//设置数据库可用
		String dbPath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dbPath);
        webSettings.setAllowFileAccess(true);					// 设置可以访问文件
        webView.loadUrl("file:///android_asset/index.html");	//WebView加载本地资源
		webView.setWebChromeClient(new MyWebChromeClient());	// 设置web视图客户端
		webView.setDownloadListener(new MyDownloadStart());		//下载监听器

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
				if (url.indexOf("index.html")>0){
					getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				}
				else{
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				}
				String title=view.getTitle();
				if(title==null){
					title=MainActivity.this.getString(R.string.app_name);
				}
				MainActivity.this.setTitle(title);
                Log.e("http", "onPageFinished: "+url);
				if (url.indexOf("index.html")>0){}
                if (url.indexOf("/Public/Uploads")>0){
					new HttpThread(url).start();	//改为由前台进行判断
                    webView.loadUrl("javascript:getFileNameInfo()");
                }
                if (url.indexOf("localfiles.html")>0){
                    webView.loadUrl("javascript: getLocalFiles() ");
                }
				if (url.indexOf("localfilesmanage.html")>0){
					webView.loadUrl("javascript: getLocalFilesManagement() ");
				}
				if (url.indexOf("localphotosmanage")>0){
					webView.loadUrl("javascript: getLocalPhotosManagement() ");
				}
                if (url.indexOf("localdrawings")>0){}
				if (url.indexOf("photo.html")>0){
					if (new systemUtils(MainActivity.this).isCameraGranted()){
						Log.e("http", "已获得摄像头权限！");
					}
					else{
						Log.e("http", "请在【设置】中打开摄像头权限！");
						webView.loadUrl("javascript: cameraDiabled() ");
					}
				}
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //Log.e("http", "shouldOverrideUrlLoading: "+url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //view.loadUrl(url);
                return false;
            }
        });
    }

	//自定义摄像头方法
	public String fileFullName;//照相后的照片的完整路径
	private Uri photouri;					//保存拍照uri
	public void takePhoto(String filename) {
		Log.e("http","----start to take photo ----");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");
		String targetDir = config.photoPath;									//照片保存路径
		File file = new File(targetDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileFullName = targetDir + "/" + filename;
		Log.e("http","----taking photo fileFullName: " + fileFullName);
		intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");		//初始化并调用摄像头
		Uri uri = Uri.fromFile(new File(fileFullName));
		photouri=uri;													//将图片uri保存在全局变量中
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

    private class MyWebChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
        {
            Log.e("http", "openFileChooser 1");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
        {
            Log.e("http", "openFileChooser 2");
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                intent = fileChooserParams.createIntent();
            }
            try
            {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e)
            {
                uploadMessage = null;
                Toast.makeText(MainActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
        {
            Log.e("http", "openFileChooser 3");
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg)
        {
            Log.e("http", "openFileChooser 5");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 107;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			String message=null;		//message保存了照片的url或者出错信息
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
//				Toast.makeText(this, "Image saved to:" + intent.getData(), Toast.LENGTH_LONG).show();
				Log.e("http", "Image captured and saved to fileUri specified in the Intent!");
				Log.e("http", "global photouri:"+photouri);
				new systemUtils(MainActivity.this).broadcastPhotoInfo(photouri);
				message=photouri.toString();
				webView.loadUrl("javascript: showPhotoMessage('"+message+ "','url') ");		//加载当前页面的showPhotoMessage()
			} else if (resultCode == RESULT_CANCELED) {
				message="已取消！";
				webView.loadUrl("javascript: showPhotoMessage('"+message+ "','msg') ");
//				Log.e("http", "User cancelled the image capture!");
				// User cancelled the image capture
			} else {
				message="系统异常！";
				webView.loadUrl("javascript: showPhotoMessage('"+message+ "','msg') ");
//				Log.e("http", "Image capture failed, advise user");
				// Image capture failed, advise user
			}
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }


    class MyDownloadStart implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
									String contentDisposition, String mimetype, long contentLength) {
            //调用自己的下载方式HttpThread
            Log.e("http","onDownloadStart url is: "+url);
            new HttpThread(url).start();
        }

    }

    private void Init() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {				//监听退出事件
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                Log.e("http", "exitTime:"+exitTime);
            } else {
                finish();
            }
        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e("http","the selected id is: "+id);

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
