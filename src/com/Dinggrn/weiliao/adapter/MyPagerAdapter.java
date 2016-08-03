package com.Dinggrn.weiliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.Dinggrn.weiliao.fragment.FindFragment;
import com.Dinggrn.weiliao.fragment.FriendFragment;
import com.Dinggrn.weiliao.fragment.MessageFragment;
import com.Dinggrn.weiliao.fragment.SettingFragment;



public class MyPagerAdapter extends FragmentPagerAdapter{
	
	List<Fragment> fragments = new ArrayList<Fragment>();
	
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments.add(new MessageFragment());
		fragments.add(new FriendFragment());
		fragments.add(new FindFragment());
		fragments.add(new SettingFragment());
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
