package ui;

import com.example.zuoyebang.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class WelcomeActivity extends Activity {
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			Log.d("myapp","myhandle");

			switch(msg.what){
			case UPDATE:
				Intent it = new Intent(mthis,MainActivity.class);
				startActivity(it);
				mHandler = null;
				finish();
			default:
				break;
			}
		}
	};
	
	final int UPDATE= 1;
	Activity mthis = this;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
        new Thread(new Runnable() {  
        	@Override
        	public void run(){
        		doInback(); 	
        	}
        }).start();
	
	}
	void doInback(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Message msg = mHandler.obtainMessage(UPDATE); 
        mHandler.sendMessage(msg);	
	}
}
