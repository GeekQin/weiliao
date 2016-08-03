package com.Dinggrn.weiliao.ui;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.Dinggrn.weiliao.R;

public class BlackListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.black_list, menu);
		return true;
	}

}
