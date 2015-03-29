package ui;

import java.util.ArrayList;

import com.example.zuoyebang.R;
import com.example.zuoyebang.R.layout;

import adapter.MyFragmentPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	TextView bt_answer ;
	TextView bt_ask;	
	MainActivity mthis;
	ViewPager mPager;
	ArrayList<Fragment> fragmentList;
	Fragment ask_fragment = new FragmentAsk();
	Fragment answer_fragment = new FragmentAnswer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mthis = this;
		
		OnClickListener listener = new OnClickListener(){//创建监听对象    
	        public void onClick(View v){    
	            Log.d("myapp","button is "+v.getId());
	            resetBtn();
	            FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
	            switch(v.getId()){
	            case R.id.imageButton_answer:
	            	
	            	bt_answer.setEnabled(false);
	            	transation.hide(ask_fragment);
	            	transation.show(answer_fragment);  
//	            	transation.replace(R.id.pager, answer_fragment);
	 
	            	break;
	            case R.id.imageButton_ask:
	            	bt_ask.setEnabled(false);
	            	transation.hide(answer_fragment);
	            	transation.show(ask_fragment);  
//	            	transation.replace(R.id.pager, ask_fragment);


	            	break;
	            default:
	            	break;
	            }
	           	transation.commit();  

	        }  
		};
		
		bt_answer = (TextView) findViewById(R.id.imageButton_answer);
		bt_ask = (TextView) findViewById(R.id.imageButton_ask);
		bt_answer.setOnClickListener(listener);
		bt_ask.setOnClickListener(listener);
		bt_ask.setEnabled(false);
		InitViewPager();
	}
	private void resetBtn(){
		bt_answer.setEnabled(true);
		bt_ask.setEnabled(true);
	}
	private void InitViewPager(){
		/*
		mPager = (ViewPager)findViewById(R.id.pager);
		
		fragmentList = new ArrayList<Fragment> ();
		fragmentList.add(ask_fragment);
		fragmentList.add(answer_fragment);
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList)); 
		mPager.setCurrentItem(0);	
		 * */
		FragmentTransaction transation = getSupportFragmentManager().beginTransaction();
    	transation.add(R.id.pager,answer_fragment);
    	transation.add(R.id.pager,ask_fragment);
    	transation.show(ask_fragment);
//		transation.replace(R.id.pager, ask_fragment);
       	transation.commit();  

	}
}
