package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.zuoyebang.R;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SavePhotoACtivity extends Activity  {
	Bitmap bmp;
    private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/zuoyebang/"; 
    
    RelativeLayout scan;
    ImageView imageview1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    RelativeLayout.LayoutParams scan_params ;
    RelativeLayout.LayoutParams old_scan_params ;

    int screenWidth ;
    int screenHeight ;
    int REGION_LEFT ;
    int REGION_RIGHT;
    int REGION_TOP;
    int REGION_BOTTOM;
    int x_down;
    int y_down;
   
    final int MOVE_LEFT=0;
    final int MOVE_RIGHT=1;
    final int MOVE_TOP=2;
    final int MOVE_BOTTOM=3;
    final int MOVE_SHIFT=4;
    final int MOVE_LEFT_TOP=5;
    final int MOVE_LEFT_BOTTOM=6;
    final int MOVE_RIGHT_TOP=7;
    final int MOVE_RIGHT_BOTTOM=8;
    int move;
    int max_width ;
    int max_height;
    final int MIN_WIDTH = 320;
    final int MIN_HEIGHT = 100;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.capture_save);
		
				
		// 获取saveDialog对话框上的ImageView组件
		ImageView show = (ImageView)findViewById(R.id.show);		
		Bitmap  bmp =	BitmapFactory.decodeFile(ALBUM_PATH+"1.jpg");

        show.setImageBitmap(bmp);
        scan = (RelativeLayout)findViewById(R.id.shift);        
        scan_params =(RelativeLayout.LayoutParams)scan.getLayoutParams();
    
        scan.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					
					RelativeLayout scan_choose = (RelativeLayout)findViewById(R.id.shift);	
				    screenWidth = scan_choose.getWidth();
				    screenHeight = scan_choose.getHeight();
					Log.d("myapp","screenWidth "+ screenWidth);
				    Log.d("myapp","screenHeight "+ screenHeight);
				    REGION_LEFT = screenWidth/3;
				    REGION_RIGHT =  screenWidth/3*2;    
				    REGION_TOP = screenHeight/3;
				    REGION_BOTTOM =  screenHeight/3*2;			    
					x_down = (int) arg1.getX() ;
					y_down = (int) arg1.getY() ;
					scan_params =(RelativeLayout.LayoutParams)scan.getLayoutParams();
					
					  ImageView capture_save_container = (ImageView)findViewById(R.id.show);
				      
				        max_width = (int)capture_save_container.getWidth();
				        max_height = (int)capture_save_container.getHeight();
				        Log.d("myapp","max_width"+max_width);
				        Log.d("myapp","max_height"+max_height);
				    
						if(y_down > REGION_TOP && y_down<REGION_BOTTOM){
							if(x_down > REGION_LEFT && x_down< REGION_RIGHT){
								move = MOVE_SHIFT;
							}else if(x_down<REGION_LEFT){
								move = MOVE_LEFT;
							}else if(x_down>REGION_RIGHT){
								move = MOVE_RIGHT;
							}
						}else if(y_down < REGION_TOP){
							if(x_down > REGION_LEFT && x_down< REGION_RIGHT){
								move = MOVE_TOP;
							}else if(x_down<REGION_LEFT){
								move = MOVE_LEFT_TOP;
							}else if(x_down>REGION_RIGHT){
								move = MOVE_RIGHT_TOP;
							}
						}else{
							if(x_down > REGION_LEFT && x_down< REGION_RIGHT){
								move = MOVE_BOTTOM;
							}else if(x_down<REGION_LEFT){
								move = MOVE_LEFT_BOTTOM;
							}else if(x_down>REGION_RIGHT){
								move = MOVE_RIGHT_BOTTOM;
							}
						}
						x_down += scan_params.leftMargin;
						y_down +=  scan_params.topMargin;
				
				
					break;
				
				case MotionEvent.ACTION_MOVE:
				
					int x = (int) arg1.getX() + scan_params.leftMargin;
					int y = (int) arg1.getY() + scan_params.topMargin;
					Log.d("myapp","x poing "+x);
					Log.d("myapp","y poing "+y);

			
					int distance_y = y - y_down;
					int distance_x = x - x_down;
					Log.d("myapp","move + "+move);
					Log.d("myapp","distance_x = "+distance_x);
					Log.d("myapp","distance_y = "+distance_y);


					switch(move){
					case MOVE_TOP:
						scan_params.topMargin =  	scan_params.topMargin  + distance_y;	
						if(scan_params.topMargin < 0 ) scan_params.topMargin=0;
						if(scan_params.topMargin + MIN_HEIGHT > max_height - scan_params.bottomMargin ) scan_params.topMargin = max_height - scan_params.bottomMargin - MIN_HEIGHT;

						break;
					case MOVE_LEFT_TOP:
						scan_params.topMargin =  	scan_params.topMargin + distance_y;			
						scan_params.leftMargin = scan_params.leftMargin + distance_x;
						if(scan_params.topMargin < 0 ) scan_params.topMargin=0;
						if(scan_params.leftMargin < 0 ) scan_params.leftMargin=0;
						if(scan_params.topMargin + MIN_HEIGHT > max_height - scan_params.bottomMargin ) scan_params.topMargin = max_height - scan_params.bottomMargin - MIN_HEIGHT;
						if(scan_params.leftMargin+ MIN_WIDTH > max_width -scan_params.rightMargin) scan_params.leftMargin = max_width-scan_params.rightMargin - MIN_WIDTH;
						break;
					case MOVE_RIGHT_TOP:
						scan_params.topMargin =  	scan_params.topMargin + distance_y;			
						scan_params.rightMargin = scan_params.rightMargin - distance_x;
						if(scan_params.topMargin < 0 ) scan_params.topMargin=0;
						if(scan_params.rightMargin >max_width ) scan_params.rightMargin=max_width;
						if(scan_params.topMargin + MIN_HEIGHT > max_height - scan_params.bottomMargin ) scan_params.topMargin = max_height - scan_params.bottomMargin - MIN_HEIGHT;
						if(max_width - scan_params.rightMargin < scan_params.leftMargin + MIN_WIDTH) scan_params.rightMargin = max_width - (scan_params.leftMargin + MIN_WIDTH);

						break;
					case MOVE_LEFT:
						scan_params.leftMargin =  	scan_params.leftMargin + distance_x;
						if(scan_params.leftMargin < 0 ) scan_params.leftMargin=0;
						if(scan_params.leftMargin+ MIN_WIDTH > max_width -scan_params.rightMargin) scan_params.leftMargin = max_width-scan_params.rightMargin - MIN_WIDTH;

						break;
					case MOVE_RIGHT:
						scan_params.rightMargin =  	scan_params.rightMargin - distance_x;	
						if(scan_params.rightMargin >max_width ) scan_params.rightMargin=max_width;
						if(max_width - scan_params.rightMargin < scan_params.leftMargin + MIN_WIDTH) scan_params.rightMargin = max_width - (scan_params.leftMargin + MIN_WIDTH);

						break;
					case MOVE_BOTTOM:
						scan_params.bottomMargin = scan_params.bottomMargin - distance_y;
						if(scan_params.bottomMargin <0 ) scan_params.bottomMargin= 0;
						if(max_height-scan_params.bottomMargin < scan_params.topMargin + MIN_HEIGHT ) scan_params.bottomMargin = max_height - ( scan_params.topMargin + MIN_HEIGHT);

					case MOVE_LEFT_BOTTOM:
						scan_params.leftMargin =  	scan_params.leftMargin + distance_x;			
						scan_params.bottomMargin = scan_params.bottomMargin - distance_y;
						if(scan_params.leftMargin < 0 ) scan_params.leftMargin=0;
						if(scan_params.bottomMargin <0 ) scan_params.bottomMargin= 0;
						if(scan_params.leftMargin+ MIN_WIDTH > max_width -scan_params.rightMargin) scan_params.leftMargin = max_width-scan_params.rightMargin - MIN_WIDTH;
						if(max_height-scan_params.bottomMargin < scan_params.topMargin + MIN_HEIGHT ) scan_params.bottomMargin = max_height - ( scan_params.topMargin + MIN_HEIGHT);
						
						break;
					case MOVE_RIGHT_BOTTOM:			
						scan_params.rightMargin =  	scan_params.rightMargin - distance_x;			
						scan_params.bottomMargin = scan_params.bottomMargin - distance_y;

						if(scan_params.bottomMargin <0 ) scan_params.bottomMargin= 0;
						if(max_height-scan_params.bottomMargin < scan_params.topMargin + MIN_HEIGHT ) scan_params.bottomMargin = max_height - ( scan_params.topMargin + MIN_HEIGHT);
						if(scan_params.rightMargin <0 ) scan_params.rightMargin=0;
						if(max_width - scan_params.rightMargin < scan_params.leftMargin + MIN_WIDTH) scan_params.rightMargin = max_width - (scan_params.leftMargin + MIN_WIDTH);

						break;
					case MOVE_SHIFT:
						scan_params.rightMargin =  	scan_params.rightMargin - distance_x;			
						scan_params.bottomMargin = scan_params.bottomMargin - distance_y;
						scan_params.leftMargin =  	scan_params.leftMargin + distance_x;			
						scan_params.topMargin =  	scan_params.topMargin + distance_y;
						if(scan_params.rightMargin < 0 ) scan_params.rightMargin=0;
						if(scan_params.leftMargin < 0 ) scan_params.leftMargin = 0;
						if(scan_params.bottomMargin <0 ) scan_params.bottomMargin= 0;
						if(scan_params.topMargin <0 ) scan_params.topMargin=0;


						break;
					}
					Log.d("myapp","after moved " );
					Log.d("myapp","scan_params.topMargin  "+ scan_params.topMargin );
					Log.d("myapp","scan_params.bottomMargin  "+ scan_params.bottomMargin );
					Log.d("myapp","scan_params.leftpMargin  "+ scan_params.leftMargin );
					Log.d("myapp","scan_params.rightMargin  "+ scan_params.rightMargin );
					
					scan.setLayoutParams(scan_params);
					y_down = y;
					x_down = x;
					break;
				// TODO Auto-generated method stub
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
        });  

    }
   

	public void takePhoto(View source){
		//可以通过createbitmap截图
		
		finish();
	}

}
