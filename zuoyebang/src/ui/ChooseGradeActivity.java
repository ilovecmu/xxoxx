package ui;

import com.example.zuoyebang.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableRow;

public class ChooseGradeActivity extends Activity{
	private int grade_selected;
	private int subject_selected;

	final String[] gradeString={"全部","小学","初一","初二","初三","高一","高二","高三"};
	final String[][] subjectString={
			{"全部","数学","英语","语文","物理","化学","生物","政治","历史","地理"},
			{"数学","英语","语文"},
			{"数学","英语","语文","生物","政治","历史","地理"},
			{"数学","英语","语文","物理","生物","政治","历史","地理"},
			{"数学","英语","语文","物理","化学","生物","政治","历史","地理"},
			{"数学","英语","语文","物理","化学","生物","政治","历史","地理"},
			{"数学","英语","语文","物理","化学","生物","政治","历史","地理"},
			{"数学","英语","语文","物理","化学","生物","政治","历史","地理"},
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent it = getIntent();
		int width = it.getIntExtra("Width", 100);
		int height = it.getIntExtra("Height", 100);

		setContentView(R.layout.activity_choose_grade);
		
		setGradeContent(false);
		setSubjectContent();
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		
		Log.d("myapp","width"+lp.width);
		Log.d("myapp","height"+lp.height);
		Log.d("myapp","x"+lp.x);
		Log.d("myapp","y"+lp.y);

		lp.width = LayoutParams.FILL_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.x=  0;
		lp.y = height + 10;
		lp.gravity=Gravity.LEFT | Gravity.TOP;
		getWindow().setAttributes(lp);	
		
		Button sureButton = (Button)findViewById(R.id.choose_finish);
		sureButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setReturnData();
				finish();			
			}
			
		});
	}

	private void setGradeContent(boolean hide){
		int skip=0;
		TableRow tabrow1= (TableRow)findViewById(R.id.tableRow1);
		TableRow tabrow2= (TableRow)findViewById(R.id.tableRow2);
		TableRow[] tabArray = new TableRow[2] ;
		final Button[] btnArray = new Button[8];

		tabArray[0] = tabrow1;
		tabArray[1] = tabrow2;
		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		lp.weight=1;
		OnClickListener gradeListener =  new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				for(int i=0;i<8;i++){
					btnArray[i].setEnabled(true);
				}
					
				// TODO Auto-generated method stub
				grade_selected=(int) arg0.getTag();
				Log.d("myapp","grade is "+ grade_selected);	
				arg0.setEnabled(false);
				setSubjectContent();
			}		
		};

		if(hide==true){
			grade_selected=1;
			skip=1;
		}
		for(int i=0;i<8;i++)
		{
			Button btn = new Button(this);
			btn.setLayoutParams(lp);
			
			tabArray[i/4].addView(btn);	
			if(i+skip>=gradeString.length){
				btn.setText(gradeString[gradeString.length-1]);
				btn.setVisibility(View.INVISIBLE);
			}
			else{
				btn.setText(gradeString[i+skip]);
			}
			btnArray[i]=btn;
			btn.setBackgroundResource(R.drawable.selector_choose_grade);
			btn.setOnClickListener(gradeListener);
			btn.setTag(i+skip);			
		}
	}
	private void setSubjectContent(){
		int skip=0;
		TableRow tabrow3= (TableRow)findViewById(R.id.tableRow3);
		TableRow tabrow4= (TableRow)findViewById(R.id.tableRow4);
		TableRow tabrow5= (TableRow)findViewById(R.id.tableRow5);
		final Button[] btnArray = new Button[12];
		tabrow3.removeAllViews();
		tabrow4.removeAllViews();
		tabrow5.removeAllViews();

		TableRow[] tabArray = new TableRow[3] ;
		tabArray[0] = tabrow3;
		tabArray[1] = tabrow4;
		tabArray[2] = tabrow5;

		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		lp.weight=1;
		OnClickListener subjectListener =  new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for(int i=0;i<12;i++){
					btnArray[i].setEnabled(true);
				}
				subject_selected=(int) arg0.getTag();
				Log.d("myapp","subject_selected is "+ subject_selected);	
				arg0.setEnabled(false);
			}			
		};

		for(int i=0;i<12;i++){
			Button btn = new Button(this);
			btn.setLayoutParams(lp);
			if(i+skip >=subjectString[grade_selected].length){
				btn.setText(subjectString[grade_selected][subjectString[grade_selected].length-1]);
				btn.setVisibility(View.INVISIBLE);
				
			}
			else{
				btn.setText(subjectString[grade_selected][i+skip]);
			}
//			btn.setBackgroundResource(R.drawable.grade_button_style_normal);
			btn.setBackgroundResource(R.drawable.selector_choose_grade);
			
			tabArray[i/4].addView(btn);		
			btn.setOnClickListener(subjectListener);
			btn.setTag(i);	
			btnArray[i]=btn;
		}
	}
 

	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.d("myapp","onTouchEvent dismiss");
		setReturnData();
		finish();
		return true;
	}
	private void setReturnData(){
		Intent it = new Intent();
		it.putExtra("grade", gradeString[grade_selected]);
		it.putExtra("subject", subjectString[grade_selected][subject_selected]);
		setResult(RESULT_OK,it);	
	}
	
}