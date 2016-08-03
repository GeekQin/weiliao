package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.Emo;


public class EmoAdapter extends MyBaseAdapter<Emo>{

	public EmoAdapter(Context context, List<Emo> datasource) {
		super(context, datasource);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_emo_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		Emo emo = getItem(position);
		String id = emo.getId();//[emo]ue057
		//[emo]ue057--字符串截取-->ue057--方法-->R.drawable.ue057
		
		String emoId = id.substring(5);//ue057
		//context.getResources().getIdentifier可以通过资源的名字来反向查找
		//该资源名字所对应的资源id
		int resId = context.getResources().getIdentifier(emoId, "drawable", context.getPackageName());
		
		vh.ivEmo.setImageResource(resId);
		
		return convertView;
	}
	
	public class ViewHolder{
		@Bind(R.id.iv_item_emo)
		ImageView ivEmo;
		public ViewHolder(View convertView) {
			ButterKnife.bind(this,convertView);
		}
	}

}
