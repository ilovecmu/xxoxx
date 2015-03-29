package ui;


import model.QuestionItem;
import model.QuestionItemInfo;
import model.QuestionItemList;
import network.GsonRequest;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zuoyebang.R;
import com.example.zuoyebang.R.layout;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import adapter.QuestionItemAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;



public class FragmentAnswerDaijiejue extends Fragment {
	private static View  rootView;
	FragmentActivity  activity;
	QuestionItemAdapter adapter;
	PullToRefreshListView mPullRefreshListView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		Log.d("myapp","FragmentAnswerDaijiejue onCreateView");
				
		rootView =inflater.inflate(R.layout.fragment_answer_daijiejue, container,false);
		activity = this.getActivity();
		
		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.question_item);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label1 = "更新于" + DateUtils.formatDateTime(activity, System.currentTimeMillis(),  
			                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
				
				Drawable draw = getResources().getDrawable(R.drawable.ic_launcher);
				refreshView.getLoadingLayoutProxy().setPullLabel("下拉可以刷新....");  
				refreshView.getLoadingLayoutProxy().setRefreshingLabel("加载中....");  
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label1); 
				refreshView.getLoadingLayoutProxy().setReleaseLabel("加载完成");  
				refreshView.getLoadingLayoutProxy().setLoadingDrawable(draw);  		
			        
				new getDataTask().execute();

				Toast.makeText(activity, "onPullDownToRefresh", Toast.LENGTH_SHORT).show();
			}
		});

		adapter = new QuestionItemAdapter();
		adapter.setContext(this.getActivity());
		ListView listview = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(listview);

		listview.setAdapter(adapter);
		new getDataTask().execute();

		Log.d("myapp","FragmentAnswerDaijiejue");
		return rootView;		
	}
	
	
	@Override
	public void onDestroyView() {
		Log.d("myapp", "-->daijiejue onDestroyView");
	    super .onDestroyView();
	    if (null != rootView) {
	        ((ViewGroup) rootView.getParent()).removeView(rootView);
	    }
	}
	private class getDataTask extends AsyncTask<Void,Void,String>{	
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			RequestQueue mQueue = Volley.newRequestQueue(getActivity());

			try {
				Thread.sleep(1000);
				Log.d("myapp","getDataTask");
				GsonRequest<QuestionItem> gsonREquest = new GsonRequest<QuestionItem>(
						"http://10.255.255.67:8000/login1111/", QuestionItem.class,
				         new Response.Listener<QuestionItem>() {
				        	public void onResponse(QuestionItem item){
				        		QuestionItemList info = item.getQuestionItemInfo();
				        		for(int i=0;i<info.getInfo().size();i++){
				        			QuestionItemInfo qii = new QuestionItemInfo();
				        			Log.d("myapp","item" + info.getInfo().get(i).getGrade());
//				        			qii.setGrade(info.getInfo().get(i).getGrade());
//				        			qii.setTime((info.getInfo().get(i).getTime()));
//				        			qii.setQuestion(info.getInfo().get(i).getQuestion());
//				        			qii.setUsername(info.getInfo().get(i).getUsername());
				        			

				        			adapter.addItem(info.getInfo().get(i));
				        			adapter.notifyDataSetChanged();

				        		}
				        	}
						},new Response.ErrorListener() {  
				            @Override  
				            public void onErrorResponse(VolleyError error) {  
				                Log.e("TAG", error.getMessage(), error);  
				            } 
						});
				mQueue.add(gsonREquest);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String result){
			Log.d("myapp","onPostExecute");
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	@Override
	public void onStop(){
	    super.onStop();  
	    adapter.onStop();
	}

}