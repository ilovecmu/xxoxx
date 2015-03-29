package adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
	ArrayList<Fragment> list;
    private ArrayList<String> CONTENT = new ArrayList<String>();

	public MyFragmentPagerAdapter(FragmentManager fragmentManager,ArrayList<Fragment> list) {
		super(fragmentManager);
		Log.d("myapp","MyFragmentPagerAdapter");

		this.list = list;
		
	}
	
	public void setPageTitle(String[] content){
		for(int i=0;i<content.length;i++)
		{
			CONTENT.add(content[i]);
		}
	}

	@Override
    public CharSequence getPageTitle(int position) {
        //return CONTENT[position % CONTENT.length].toUpperCase();
		return CONTENT.get(position);
    }

	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}	
	
	 @Override
	public void destroyItem(View view, int position, Object object)                       //Ïú»ÙItem
	{
	    Log.d("myapp","destroyItem");;

	}
	
	@Override
	public Object instantiateItem(View view, int position)                                //ÊµÀý»¯Item
	{
	    Log.d("myapp","instantiateItem");
	    return null;
	}
	    
}
