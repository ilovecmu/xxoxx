package adapter;


import java.util.ArrayList;


import model.QuestionItemInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import com.example.zuoyebang.R;

public class QuestionItemAdapter extends BaseAdapter{
	private int size = 0;
	private Context context;
	private ArrayList<QuestionItemInfo> array = new ArrayList<QuestionItemInfo>();
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	

	private void init(){
		mRequestQueue = Volley.newRequestQueue(context);
		final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
				20);
		ImageCache imageCache = new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap value) {
				mImageCache.put(key, value);
			}

			@Override
			public Bitmap getBitmap(String key) {
				return mImageCache.get(key);
			}
		};
		mImageLoader = new ImageLoader(mRequestQueue, imageCache);
	}
	public void setContext(Context context){
		this.context = context;
		init();
	}
	
	public void addItem(QuestionItemInfo info){
		array.add(info);
		size++;
//		Log.d("myapp","addItem" +size);
	}
	@Override
	public int getCount() {
//		Log.d("myapp","getCount" +size);
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
//		Log.d("myapp","getItem");
//		return array.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View  convertView , ViewGroup arg2) {
		Log.d("myapp","getView");
		final String[] gradeString={"全部","小学","初一","初二","初三","高一","高二","高三"};


		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context); 
		ViewHolder holder =  new ViewHolder();
		if(convertView ==null){
			convertView =  (LinearLayout)inflater.inflate(R.layout.question_item, null);
			holder.grade = (TextView) convertView.findViewById(R.id.grade);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.question_text = (TextView) convertView.findViewById(R.id.question_text);
			holder.user_icon = (ImageView) convertView.findViewById(R.id.user_icon);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.user_name = (TextView) convertView.findViewById(R.id.use_name);
			holder.use_cnt = (TextView) convertView.findViewById(R.id.answer_num);
			holder.question_img=(ImageView) convertView.findViewById(R.id.question_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.grade.setText(gradeString[array.get(arg0).getGrade()]);
		holder.time.setText(array.get(arg0).getTime() + "分钟前");
		holder.question_text.setText(array.get(arg0).getQuestion());
		holder.user_name.setText(array.get(arg0).getUsername());

		Log.d("myapp","grade is " + array.get(arg0).getGrade());
		Log.d("myapp","time is " + array.get(arg0).getTime());
		Log.d("myapp","question_text is " + array.get(arg0).getQuestion());

		// imageView是一个ImageView实例
		// ImageLoader.getImageListener的第二个参数是默认的图片resource id
		// 第三个参数是请求失败时候的资源id，可以指定为0
		
		ImageListener listener = ImageLoader
				.getImageListener(holder.question_img, R.drawable.video_loading_animation_rotation,
						R.drawable.common_image_list_placeholder_loading);
		mImageLoader.get("http://10.255.255.67:8000/login/", listener);
		
		/*
		String url="http://10.255.255.67:8000/login/";
		final ViewHolder old_holder = holder;
		
		ImageRequest imgRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap arg0) {
				// TODO Auto-generated method stub
				old_holder.question_img.setImageBitmap(arg0);
			}
		}, 300, 200, Config.ARGB_8888, new ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		
        mRequestQueue.add(imgRequest);  
		 */
		return convertView;
	}
	private class ViewHolder{
		TextView grade;
		TextView time;
		TextView score;
		TextView question_text;
		ImageView question_img;
		ImageView user_icon;
		TextView user_name;
		TextView  use_cnt;
	}
	
	public void onStop(){
	    mRequestQueue.cancelAll(this);  
	}
}