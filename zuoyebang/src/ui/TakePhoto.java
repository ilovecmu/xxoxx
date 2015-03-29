package ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.zuoyebang.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.animation.AnimationUtils;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class TakePhoto extends Activity
{
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	// 定义系统所用的照相机
	Camera camera;
	// 是否在预览中
	boolean isPreview = false;
    private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/zuoyebang/"; 
	private ImageView focus_indicator_idle;
	private ImageView focus_indicator_fail ;
	private ImageView focus_indicator_suce;
	private ImageView focus_indicator_idle_small;

	final int FOCUSING=0;
	final int FOCUS_SUCC=1;
	final int FOCUS_FAIL=2;
	final int FOCUS_IDLE =3;
	private int state = FOCUS_IDLE;
	private boolean captureStarted=false;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.photo);
		focus_indicator_idle_small=(ImageView)findViewById(R.id.focus_indicator_idle_small);
	    focus_indicator_idle =(ImageView)findViewById(R.id.focus_indicator_idle);
	    focus_indicator_suce =(ImageView)findViewById(R.id.focus_indicator_success);
	    focus_indicator_fail =(ImageView)findViewById(R.id.focus_indicator_fail);
		// 获取窗口管理器
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// 获取屏幕的宽和高
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) findViewById(R.id.preview);
		// 设置该Surface不需要自己维护缓冲区
		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback(){
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height){
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder){

				// 打开摄像头
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder){
				// 如果camera不为null ,释放摄像头
				if (camera != null){
					if (isPreview) camera.stopPreview();
					camera.release();
					camera = null;
					isPreview = false;
				}
			}
		});
		
		final Handler myHandler = new Handler(){

	          public void handleMessage(Message msg) { 
	        	  Log.d("myapp","msg is"+msg.what);
	        	  switch(msg.what){	        	  
	        	 
						case FOCUSING:
						focus_indicator_idle.setVisibility(View.VISIBLE);
						Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.camera_scan_focus);
						animation.setFillAfter(false);					
						focus_indicator_idle.startAnimation(animation);	
						focus_indicator_idle.setVisibility(View.INVISIBLE);
						focus_indicator_idle_small.setVisibility(View.VISIBLE);
						break;

					case FOCUS_SUCC:
						focus_indicator_idle_small.setVisibility(View.INVISIBLE);
						focus_indicator_idle.setVisibility(View.INVISIBLE);	
						focus_indicator_fail.setVisibility(View.INVISIBLE);
						focus_indicator_suce.setVisibility(View.VISIBLE);
						break;
					case FOCUS_FAIL:
						focus_indicator_idle_small.setVisibility(View.INVISIBLE);
						focus_indicator_idle.setVisibility(View.INVISIBLE);	
						focus_indicator_suce.setVisibility(View.INVISIBLE);
						focus_indicator_fail.setVisibility(View.VISIBLE);
						break;

					default:
						focus_indicator_idle_small.setVisibility(View.INVISIBLE);
						focus_indicator_idle.setVisibility(View.INVISIBLE);	
						focus_indicator_suce.setVisibility(View.INVISIBLE);
						focus_indicator_fail.setVisibility(View.INVISIBLE);
						break;
	        	    }   
	                super.handleMessage(msg);   
	          }

	     };
	
		new Thread(new Runnable(){

			@Override
			public void run() {
				int what_do = FOCUS_IDLE;
				int cnt=0;
				while(true){
				// TODO Auto-generated method stub

						if( what_do!=state ){
							what_do= state;
							cnt=0;
							myHandler.sendEmptyMessage(what_do);
						}else{
							cnt++;
							if(cnt==2){
								Log.d("myapp","return to idle");
//								state = FOCUS_IDLE;
//								what_do = FOCUS_IDLE;
								myHandler.sendEmptyMessage(FOCUS_IDLE);
//								cnt=0;
							}
						}
							
					try {			
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}

	@SuppressLint("NewApi")
	private void initCamera(){

		if (!isPreview){
			// 此处默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			camera = Camera.open();  //①
			camera.setDisplayOrientation(0);
		}
		if (camera != null && !isPreview){
			try{
	
				Camera.Parameters parameters = camera.getParameters();
				//设备不支持设置如下
				// 设置预览照片的大小
//				parameters.setPreviewSize(screenWidth, screenHeight);
					// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 100);
				// 设置照片的大小
				parameters.setPictureSize(screenWidth, screenHeight);
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				camera.setParameters(parameters);

				// 通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder);  //
				
				camera.setAutoFocusMoveCallback(moveCallBack);

				
				// 开始预览
				camera.startPreview();  //③
			}
			catch (Exception e){
				e.printStackTrace();
			}
			isPreview = true;
		}
	}
	public void cancelPhoto(View source){
		Log.d("myapp","cancel");
		finish();
	}
	public void capture(View source){

		if (camera != null){
			Log.d("myapp","autoFocus");
			// 控制摄像头自动对焦后才拍照
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			camera.setParameters(parameters);
			if(captureStarted == false){
				Log.d("myapp","start capture");
				camera.autoFocus(autoFocusCallback);
				captureStarted =true;
			}else{
				Log.d("myapp","already");
			}
		}
	}
	public void openStore(View source){

		 Intent intent = new Intent();  
         /* 开启Pictures画面Type设定为image */  
         intent.setType("image/*");  
         /* 使用Intent.ACTION_GET_CONTENT这个Action */  
         intent.setAction(Intent.ACTION_GET_CONTENT);   
		 startActivityForResult(intent,1);

 
	}
	
	AutoFocusMoveCallback moveCallBack = new AutoFocusMoveCallback(){

		@SuppressLint("NewApi")
		@Override
		public void onAutoFocusMoving(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			Log.d("myapp","onAutoFocusMoving + "+arg0);
			if(arg0 == true){
				state = FOCUSING;
			}else if(state == FOCUS_FAIL){
				state = FOCUS_SUCC;
			}else{
				state = FOCUS_FAIL;
			}
		}
		
	};
	
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback(){
		// 当自动对焦时激发该方法
		@Override
		public void onAutoFocus(boolean success, Camera camera){
			Log.d("myapp","onAutoFocus");

			if (success){
			
				// takePicture()方法需要传入3个监听器参数
				// 第1个监听器：当用户按下快门时激发该监听器
				// 第2个监听器：当相机获取原始照片时激发该监听器
				// 第3个监听器：当相机获取JPG照片时激发该监听器
				camera.takePicture(new ShutterCallback(){
					public void onShutter()
					{
						Log.d("myapp","onShutter");
						// 按下快门瞬间会执行此处代码
					}
				}, new PictureCallback(){
					public void onPictureTaken(byte[] data, Camera c){
						// 此处代码可以决定是否需要保存原始照片信息
						Log.d("myapp","onPictureTaken");
					}
				}, myJpegCallback);  //⑤
			}else{
				restoreCamera();
				Log.d("myapp","focus faled");
			}
	
		}
	};

	PictureCallback myJpegCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, final Camera camera){

			// 根据拍照所得的数据创建位图
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,data.length);
			// 加载/layout/save.xml文件对应的布局资源
			isPreview = true;
			//其实可是先放在SD，只传递路径，同样，返回的数据也只是传递路径
			Intent it = new Intent(TakePhoto.this,SavePhotoACtivity.class);
			startActivityForResult(it,0);
//            new Thread(saveFileRunnable).start(); 
			try {
				saveFile(bm,"1.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "没有SD卡", Toast.LENGTH_SHORT).show();
			}
			restoreCamera();
		}
		
	};
	private void restoreCamera(){
		captureStarted = false;
		Camera.Parameters parameters = camera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		camera.setParameters(parameters);
		camera.cancelAutoFocus();

	}
	protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case 0:
				break;
			case 1:
				break;
			default:
				break;
			}	
		}
	
	public void saveFile(Bitmap bm, String fileName) throws IOException {  
        File dirFile = new File(ALBUM_PATH);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(ALBUM_PATH + fileName);  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
        bos.flush();  
        bos.close();  
    }  
      
//    private Runnable saveFileRunnable = new Runnable(){  
//        @Override  
//        public void run() {  
//            try {  
//                saveFile(bitmap, fileName);  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//        }  
//              
//    };  
}

