package com.example.jay.smartsite040;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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
//    private ValueCallback<Uri> mUploadMessage;
//    private Handler mHandler = new Handler();
//    private String url="http://202.119.84.51:9090/mobile/dist";
//    private String url="http://www.baidu.com";
//    private String url="http://192.168.1.118:8888/smartsite/";
//    public static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e("http", "onCreate! ");
        //判断网络连接
//        if(new systemUtils(this).isConnectingToInternet()){
//            Log.e("http", "wifi已连接！" );
//            new fileUtils(this).synchRecords();
//        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		//添加返回按钮
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		//返回按钮添加点击响应时间
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, "back!", Toast.LENGTH_LONG).show();
				webView.goBack();
			}
		});
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//		WindowManager manager = this.getWindowManager();
//		DisplayMetrics outMetrics = new DisplayMetrics();
//		manager.getDefaultDisplay().getMetrics(outMetrics);
//		int width = outMetrics.widthPixels;
//		int height = outMetrics.heightPixels;
//		Log.e("http", "屏幕尺寸为： "+width+"*"+height);

		webView = (WebView) findViewById(R.id.webView1);
//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) webView.getLayoutParams();

//		Log.e("http", "webview尺寸为： "+params.width);
//		params.width = 2500;
//		webView.setLayoutParams(params);
//		webView.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
		webView.getSettings().setAppCacheEnabled(true);// 设置启动缓存
//		webView.getSettings().setUseWideViewPort(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
//		webView.setBackgroundColor(Color.RED);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上，会显示白边
//        webView.addJavascriptInterface(new setFileList(this), "setFileList");
		webView.addJavascriptInterface(new systemUtils(this), "SystemUtils");
		webView.addJavascriptInterface(new fileUtils(this), "FileUtils");
//		webView.addJavascriptInterface(this, "nativeMethod");

//        webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demojava");



		final Handler mHandler = new Handler();
		webView.addJavascriptInterface(new Object(){
			@JavascriptInterface
			public String clickOnAndroid() {//将被js调用
				Log.e("http","in clickOnAndroid! ");
				mHandler.post(new Runnable() {
					public void run() {
						//fromTakePhoto  = true;
						//调用 启用摄像头的自定义方法
						SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
						Date now=new Date();
						String nowtime=sdf.format(now);
						takePhoto("IMG_" + nowtime + ".jpg");
						Log.e("http","========fileFullName: " + fileFullName);

					}
				});
				return fileFullName;
			}
			@JavascriptInterface
			public void synchServerFiles() {//，同步服务器文件，被js调用
				Log.e("http","in synchServerFiles! ");
				mHandler.post(new Runnable() {
					public void run() {

					}
				});
			}
		}, "demo");

        WebSettings webSettings = webView.getSettings();
//        webSettings.setDefaultFontSize(16);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setJavaScriptEnabled(true);//允许运行JavaScript
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //WebView加载web资源
//       webView.loadUrl("http://www.baidu.com");
        //WebView加载本地资源
        webView.loadUrl("file:///android_asset/index.html");


		//webview增加javascript接口，监听html页面中的js点击事件

//		final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
//		webView.addJavascriptInterface(new JavaScriptInterface(this),"Demo");




        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//                Log.e("http", "onLoadResource: ");
//            }
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                //error
//                switch(errorCode)
//                {

//                    case HttpStatus.SC_NOT_FOUND:
//                        view.loadUrl("file:///android_assets/error_handle.html");
//                        break;
//                }
//            }

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
				if (url.indexOf("index.html")>0){

					//webView.loadUrl("javascript: downloadAllFiles() ");
				}
                if (url.indexOf("/Public/Uploads")>0){

//                    String filename = "587b679a9adda.jpg";
                    //String filename = url.substring(url.lastIndexOf("/")+1,url.length());
                    //由于jpg下载有问题，暂时以另起线程下载的方式代替
                    //if (filename.contains(".jpg")){
                        //new HttpThread(url).start();
                    //}
					//List filelist = new fileUtils(MainActivity.this).getLocalFilesList();
					//if(!filelist.contains(filename)){	//若本地无该文件，则进行下载
						new HttpThread(url).start();	//改为由前台进行判断
						//new fileUtils(MainActivity.this).setFileInfo("#file_name",filename);
					//}

                    webView.loadUrl("javascript:getFileNameInfo()");
                }
                if (url.indexOf("localfiles.html")>0){
                    webView.loadUrl("javascript: getLocalFiles() ");
                }
				if (url.indexOf("localfilesmanage.html")>0){
					//Log.e("http", "发现: localfilesmanage.html");
					webView.loadUrl("javascript: getLocalFilesManagement() ");
				}
				if (url.indexOf("localphotosmanage")>0){
					webView.loadUrl("javascript: getLocalPhotosManagement() ");
				}
                if (url.indexOf("localdrawings")>0){
                    webView.loadUrl("javascript: getLocalDrawings() ");
                }
				if (url.indexOf("photo.html")>0){

//					if (photostatus==true){
//						Log.e("http", "发现新照片！");
//					}
//					webView.loadUrl("javascript: getLocalDrawings() ");
				}
				//打开摄像头
//				if(url.indexOf("photo")>0){
//					takePhoto("testimg" + Math.random()*1000+1 + ".jpg");
////					takePhoto("123.jpg");
//				}
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //Log.e("http", "shouldOverrideUrlLoading: "+url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });

        webView.setWebChromeClient(new MyWebChromeClient());
        // 设置web视图客户端
//        webview.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new MyDownloadStart());

    }

	/*
			 * 调用摄像头的方法
			 */
	public String fileFullName;//照相后的照片的全整路径
	private Uri photouri;					//保存拍照uri
	public void takePhoto(String filename) {
		Log.e("http","----start to take photo ----");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, "TakePhoto");

		//确定相片保存路径
//		String targetDir = sdDir + "/" + "webview_camera";
		String targetDir = config.photoPath;
		File file = new File(targetDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileFullName = targetDir + "/" + filename;
		Log.e("http","----taking photo fileFullName: " + fileFullName);
		//初始化并调用摄像头
		intent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		Uri uri = Uri.fromFile(new File(fileFullName));
		photouri=uri;			//将图片uri保存在全局变量中
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
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            Log.e("http", "openFileChooser 1");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            MainActivity.this.startActivityForResult(
//                    Intent.createChooser(i, "Image Browser"),
//                    FILECHOOSER_RESULTCODE);
//        }
//        MyWebChromeClient(){
//            Log.e("http", "MyWebChromeClient!");
//        }
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            Log.e("http", "openFileChooser 1");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);
//        }
//
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//            Log.e("http", "openFileChooser 2");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("*/*");
//            startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
//        }
//
//        // For Android 4.1
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            Log.e("http", "openFileChooser 3");
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);
//        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent intent) {
//        Log.e("http", "onActivityResult");
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage)
//                return;
//            Uri result = intent == null || resultCode != RESULT_OK ? null
//                    : intent.getData();
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//
//        }
//    }



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
				webView.loadUrl("javascript: showPhotoMessage('"+message+ "','msg') ");		//加载当前页面的showPhotoMessage()
//				Log.e("http", "User cancelled the image capture!");
				// User cancelled the image capture
			} else {
				message="系统异常！";
				webView.loadUrl("javascript: showPhotoMessage('"+message+ "','msg') ");		//加载当前页面的showPhotoMessage()
//				Log.e("http", "Image capture failed, advise user");
				// Image capture failed, advise user
			}
		}

//		if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
//			if (resultCode == RESULT_OK) {
//				// Video captured and saved to fileUri specified in the Intent
//				Toast.makeText(this, "Video saved to:\n" +
//						data.getData(), Toast.LENGTH_LONG).show();
//			} else if (resultCode == RESULT_CANCELED) {
//				// User cancelled the video capture
//			} else {
//				// Video capture failed, advise user
//			}
//		}


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
            // TODO Auto-generated method stub
            //调用自己的下载方式
            Log.e("http","onDownloadStart url is: "+url);
            new HttpThread(url).start();
            //调用系统浏览器下载
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }

    }


    //    private void webViewGoBack() {
//        mWeb.goBack();
//    }

    private void Init() {
        //保存文件列表信息
//        SharedPreferences sharedPreferences= this.getSharedPreferences("filelist", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//        editor.clear();		//清空
//        editor.commit();

//        SharedPreferences timesharedPreferences= this.getSharedPreferences("filereadtime", Context.MODE_PRIVATE);
//        SharedPreferences.Editor timeeditor = timesharedPreferences.edit();//获取编辑器
//        timeeditor.clear();		//清空
//        timeeditor.commit();

//        SharedPreferences recordsharedPreferences= this.getSharedPreferences("filerecords", Context.MODE_PRIVATE);
//        SharedPreferences.Editor recordeditor = recordsharedPreferences.edit();//获取编辑器
//        recordeditor.clear();		//清空
//        recordeditor.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e("http","the selected id is: "+id);

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
//            Log.e("http","choose gallery");
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, FileActivity.class);
//            startActivity(intent);
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
