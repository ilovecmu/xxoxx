package ui;


import com.example.zuoyebang.R;
import com.example.zuoyebang.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class FragmentAsk extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View  rootView =inflater.inflate(R.layout.activity_ask, container,false);
		ImageButton ask_button = (ImageButton) rootView.findViewById(R.id.photo_ask_question);
		
		ask_button.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("myapp","on click" + arg0.getId());
				Intent it = new Intent(getActivity(),TakePhoto.class);
				startActivityForResult(it,0);				
			}
		});
		return rootView;		
	}
	
}