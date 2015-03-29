package ui;


import com.example.zuoyebang.R;
import com.example.zuoyebang.R.layout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentAnswerHighScore extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View  rootView =inflater.inflate(R.layout.fragment_answer_high_score, container,false);
		return rootView;		
	}
	
}