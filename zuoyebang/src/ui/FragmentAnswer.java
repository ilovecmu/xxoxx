package ui;

import java.util.ArrayList;

import com.example.zuoyebang.R;

import adapter.MyFragmentPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;  
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.viewpagerindicator.TabPageIndicator;


public class FragmentAnswer extends Fragment {
	ArrayList<Fragment> fragmentList;
	private static View  rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		Log.d("myapp","FragmentAnswer onCreateView");

		Context ctxWithTheme = new ContextThemeWrapper(getActivity().getApplicationContext(), R.style.StyledIndicators);
		LayoutInflater localLayoutInflater = inflater.cloneInContext(ctxWithTheme);
		rootView =localLayoutInflater.inflate(R.layout.activity_answer, container,false);
		
		Fragment answer_fragment_not_processed = new FragmentAnswerDaijiejue();
		Fragment answer_fragment_high_score = new FragmentAnswerHighScore();	
		Fragment answer_fragment_diffcuilt = new FragmentAnswerDiffcuilt();	

		ViewPager mPager = (ViewPager)rootView.findViewById(R.id.pager1);
		fragmentList = new ArrayList<Fragment> ();
		fragmentList.add(answer_fragment_not_processed);
		fragmentList.add(answer_fragment_high_score);
		fragmentList.add(answer_fragment_diffcuilt);
	
		MyFragmentPagerAdapter adapter= new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
		String[] content =new String[]{"待解答","高分","难题"};
		adapter.setPageTitle(content);
		
		mPager.setAdapter(adapter);	
		mPager.setCurrentItem(0);
//		mPager.setOffscreenPageLimit(3);
		
		TabPageIndicator indicator = (TabPageIndicator)rootView.findViewById(R.id.indicator);

		indicator.setViewPager(mPager); 
        
		indicator.setOnPageChangeListener(new OnPageChangeListener(){
			public void onPageSelected(int arg0){
				String s[]={"page1","page2","page3"};
				Toast.makeText(getActivity().getApplicationContext(), s[arg0], Toast.LENGTH_SHORT).show();
			}
			public void onPageScrolled(int arg0,float arg1,int arg2){
				
			}
			public void onPageScrollStateChanged(int arg0){
				
			}
		});
		final Animation anim_press = AnimationUtils.loadAnimation(getActivity(), R.anim.triangle_rotate_press);
		anim_press.setFillAfter(true);
				
		final TextView textGrade = (TextView)rootView.findViewById(R.id.grade_choose);
		
		ImageButton askButton =(ImageButton) rootView.findViewById(R.id.ask_button);
		askButton.setOnClickListener( new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("myapp","on click ask quesiton" + arg0.getId());
			}
		});
		
		LinearLayout askButton1 =(LinearLayout) rootView.findViewById(R.id.tri_indicator_layout);
		final ImageButton angel =(ImageButton) rootView.findViewById(R.id.tri_indicator);
		
		askButton1.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch(arg1.getAction()){
				case MotionEvent.ACTION_DOWN:
					Log.d("myapp","action down");
					angel.setEnabled(false);
					
					break;
				case MotionEvent.ACTION_UP:
					Log.d("myapp","action up");
					angel.setEnabled(true);
					Intent it = new Intent(getActivity(),ChooseGradeActivity.class);	
					RelativeLayout layout= (RelativeLayout)rootView.findViewById(R.id.banner);
					it.putExtra("Height", layout.getHeight());
					it.putExtra("Width", layout.getWidth());
					angel.startAnimation(anim_press);

					startActivityForResult(it, 0);
					break;
				default:
					Log.d("myapp","action "+arg1.getAction());
				}
				// TODO Auto-generated method stub
				return true;
			}
			
		});
		

		return rootView;		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		final ImageButton angel =(ImageButton) rootView.findViewById(R.id.tri_indicator);

		final Animation anim_normal = AnimationUtils.loadAnimation(getActivity(), R.anim.triangel_rotate_normal);
		anim_normal.setFillAfter(true);
		anim_normal.setFillAfter(true);
		
		String grade = data.getStringExtra("grade");
		String subject =data.getStringExtra("subject");
		Log.d("myapp","get data changed" + grade +"  " + subject);
		
		final TextView grade_choose = (TextView)rootView.findViewById(R.id.grade_choose);
		grade_choose.setText(grade+subject);

		angel.startAnimation(anim_normal);

	}
	@Override
	public void onDestroyView() {
		Log.d("myapp", "-->onDestroyView");
	    super .onDestroyView();
	    if (null != rootView) {
	        ((ViewGroup) rootView.getParent()).removeView(rootView);
	    }
	}
}