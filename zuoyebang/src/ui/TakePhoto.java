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
	// ����ϵͳ���õ������
	Camera camera;
	// �Ƿ���Ԥ����
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
		// ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.photo);
		focus_indicator_idle_small=(ImageView)findViewById(R.id.focus_indicator_idle_small);
	    focus_indicator_idle =(ImageView)findViewById(R.id.focus_indicator_idle);
	    focus_indicator_suce =(ImageView)findViewById(R.id.focus_indicator_success);
	    focus_indicator_fail =(ImageView)findViewById(R.id.focus_indicator_fail);
		// ��ȡ���ڹ�����
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// ��ȡ��Ļ�Ŀ�͸�
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// ��ȡ������SurfaceView���
		sView = (SurfaceView) findViewById(R.id.preview);
		// ���ø�Surface����Ҫ�Լ�ά��������
		// ���SurfaceView��SurfaceHolder
		surfaceHolder = sView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// ΪsurfaceHolder���һ���ص�������
		surfaceHolder.addCallback(new Callback(){
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height){
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder){

				// ������ͷ
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder){
				// ���camera��Ϊnull ,�ͷ�����ͷ
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
			// �˴�Ĭ�ϴ򿪺�������ͷ��
			// ͨ������������Դ�ǰ������ͷ
			camera = Camera.open();  //��
			camera.setDisplayOrientation(0);
		}
		if (camera != null && !isPreview){
			try{
	
				Camera.Parameters parameters = camera.getParameters();
				//�豸��֧����������
				// ����Ԥ����Ƭ�Ĵ�С
//				parameters.setPreviewSize(screenWidth, screenHeight);
					// ����ͼƬ��ʽ
				parameters.setPictureFormat(ImageFormat.JPEG);
				// ����JPG��Ƭ������
				parameters.set("jpeg-quality", 100);
				// ������Ƭ�Ĵ�С
				parameters.setPictureSize(screenWidth, screenHeight);
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				camera.setParameters(parameters);

				// ͨ��SurfaceView��ʾȡ������
				camera.setPreviewDisplay(surfaceHolder);  //
				
				camera.setAutoFocusMoveCallback(moveCallBack);

				
				// ��ʼԤ��
				camera.startPreview();  //��
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
			// ��������ͷ�Զ��Խ��������
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
         /* ����Pictures����Type�趨Ϊimage */  
         intent.setType("image/*");  
         /* ʹ��Intent.ACTION_GET_CONTENT���Action */  
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
		// ���Զ��Խ�ʱ�����÷���
		@Override
		public void onAutoFocus(boolean success, Camera camera){
			Log.d("myapp","onAutoFocus");

			if (success){
			
				// takePicture()������Ҫ����3������������
				// ��1�������������û����¿���ʱ�����ü�����
				// ��2�����������������ȡԭʼ��Ƭʱ�����ü�����
				// ��3�����������������ȡJPG��Ƭʱ�����ü�����
				camera.takePicture(new ShutterCallback(){
					public void onShutter()
					{
						Log.d("myapp","onShutter");
						// ���¿���˲���ִ�д˴�����
					}
				}, new PictureCallback(){
					public void onPictureTaken(byte[] data, Camera c){
						// �˴�������Ծ����Ƿ���Ҫ����ԭʼ��Ƭ��Ϣ
						Log.d("myapp","onPictureTaken");
					}
				}, myJpegCallback);  //��
			}else{
				restoreCamera();
				Log.d("myapp","focus faled");
			}
	
		}
	};

	PictureCallback myJpegCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, final Camera camera){

			// �����������õ����ݴ���λͼ
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,data.length);
			// ����/layout/save.xml�ļ���Ӧ�Ĳ�����Դ
			isPreview = true;
			//��ʵ�����ȷ���SD��ֻ����·����ͬ�������ص�����Ҳֻ�Ǵ���·��
			Intent it = new Intent(TakePhoto.this,SavePhotoACtivity.class);
			startActivityForResult(it,0);
//            new Thread(saveFileRunnable).start(); 
			try {
				saveFile(bm,"1.jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "û��SD��", Toast.LENGTH_SHORT).show();
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

