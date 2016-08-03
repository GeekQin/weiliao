package com.Dinggrn.weiliao.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.Dinggrn.weiliao.R;
import com.Dinggrn.weiliao.bean.Blog;
import com.Dinggrn.weiliao.bean.Comment;
import com.Dinggrn.weiliao.bean.Favo;
import com.Dinggrn.weiliao.bean.User;
import com.Dinggrn.weiliao.listener.OnPhotoViewListener;
import com.Dinggrn.weiliao.listener.OnSaveCommentListener;
import com.Dinggrn.weiliao.ui.BaseActivity;
import com.Dinggrn.weiliao.util.ChatUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BlogAdapter extends MyBaseAdapter<Blog>{

	OnSaveCommentListener onSaveCommentListener;
	OnPhotoViewListener onPhotoViewListener;

	public BlogAdapter(Context context, List<Blog> datasource,OnSaveCommentListener onSaveCommentListener,OnPhotoViewListener onPhotoViewListener) {
		super(context, datasource);
		this.onSaveCommentListener = onSaveCommentListener;
		this.onPhotoViewListener = onPhotoViewListener;
	}

	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_blog_layout, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}

		Blog blog = getItem(position);

		User user = blog.getUser();

		if(TextUtils.isEmpty(user.getAvatar())){
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		}else{
			ImageLoader.getInstance().displayImage(user.getAvatar(), vh.ivAvatar);
		}

		vh.tvUsername.setText(user.getUsername());

		vh.tvContent.setText(blog.getContent());

		vh.rlIamgesContainer.setVisibility(View.GONE);

		if(!TextUtils.isEmpty(blog.getImages())){
			String url = blog.getImages();
			String[] urls = url.split("&");
			showIamges(urls,vh.rlIamgesContainer);
		}

		vh.tvTime.setText(blog.getCreatedAt());
		vh.tvLove.setText(blog.getLove()+" 赞");

		//点"赞"
		vh.tvLove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dianzan(position);

			}
		});

		vh.llCommentsContainer.setVisibility(View.GONE);

		//点击“评论”
		vh.tvComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSaveCommentListener.saveComment(position);
			}
		});
		showComments(position,vh.llCommentsContainer);


		return convertView;
	}

	/**
	 * 为blog点赞
	 * @param position
	 */
	protected void dianzan(final int pos) {
		//查询一下，当前登录用户是否已经为pos位置的blog点过赞
		BmobQuery<Favo> query = new BmobQuery<Favo>();

		query.addWhereEqualTo("blogId", getItem(pos).getObjectId());
		query.addWhereEqualTo("userId", BmobUserManager.getInstance(context).getCurrentUserObjectId());

		query.findObjects(context, new FindListener<Favo>() {

			@Override
			public void onError(int arg0, String arg1) {
				//TODO if(arg0==错误码(意味着当前没有Favo数据表))
				updateFavoCount(pos);
			}

			@Override
			public void onSuccess(List<Favo> arg0) {
				if(arg0.size()>0){
					//点过赞了，提示用户不能重复点
					((BaseActivity)context).toast("您已经为该blog点过赞了");
				}else{
					//没点过，就更新pos位置blog的赞的数量
					updateFavoCount(pos);
				}
			}
		});




	}

	protected void updateFavoCount(final int pos) {
		//1)创建一个Favo对象，保存到服务器上
		Favo favo = new Favo();
		favo.setBlogId(getItem(pos).getObjectId());
		favo.setUserId(BmobUserManager.getInstance(context).getCurrentUserObjectId());
		favo.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				//2) 更新Blog表中，pos所对应的blog记录中，love字段的值
				Blog newBlog = new Blog();
				newBlog.setLove(getItem(pos).getLove()+1);
				newBlog.update(context, getItem(pos).getObjectId(), new UpdateListener() {

					@Override
					public void onSuccess() {
						// 3)更新ListView数据源中的数据
						Blog blog = getItem(pos);
						blog.setLove(blog.getLove()+1);
						notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});


			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void showComments(int pos, final LinearLayout container) {
		//找到pos对应的blog的objectId
		String blogId = getItem(pos).getObjectId();
		//然后用该objectId作为查询条件，取查询服务器上Comment表中是否有对应的记录
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		//query.include("user");//如果要查询评论发布者的详细内容
		query.addWhereEqualTo("blogId", blogId);
		query.findObjects(context, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				if(arg0.size()>0){
					//如果有，把评论的内容提取，放到TextView中
					//然后将TextView放到container中显示
					container.setVisibility(View.VISIBLE);
					container.removeAllViews();
					int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
					for(int i=0;i<arg0.size();i++){
						Comment comment = arg0.get(i);
						// XXX评论：太好了！ 2016-4-25 10:13:13
						String content = comment.getUsername()+" 评论：" + comment.getContent() + "  "+comment.getCreatedAt();	
						TextView tv = new TextView(context);
						//指定了tv的宽度和高度
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						tv.setLayoutParams(params);
						tv.setText(ChatUtil.getSpannableString(content));
						tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
						tv.setPadding(padding, padding,padding,padding);
						container.addView(tv);
					}
				}

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void showIamges(final String[] urls, RelativeLayout container) {

		container.setVisibility(View.VISIBLE);
		container.removeAllViews();

		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

		for(int i=0;i<urls.length;i++){

			final int idx = i;

			ImageView iv = new ImageView(context);
			iv.setId(i+1);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
			//为ImageView在relativeLayout中的摆放添加规则
			if(i%2!=0){
				params.addRule(RelativeLayout.RIGHT_OF, i);
			}
			if(i>=2){
				params.addRule(RelativeLayout.BELOW, i-1);
			}
			params.setMargins(margin, margin, margin, margin);
			iv.setLayoutParams(params);
			ImageLoader.getInstance().displayImage(urls[i], iv);
			//为iv添加一个监听器
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onPhotoViewListener.onPhotoView(urls[idx]);

				}
			});

			container.addView(iv);
		}
	}

	public class ViewHolder{

		@Bind(R.id.iv_item_blog_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_blog_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_blog_content)
		TextView tvContent;
		@Bind(R.id.rl_item_blog_imagescontainer)
		RelativeLayout rlIamgesContainer;
		@Bind(R.id.tv_blog_item_time)
		TextView tvTime;
		@Bind(R.id.tv_blog_item_love)
		TextView tvLove;
		@Bind(R.id.tv_blog_item_comment)
		TextView tvComment;
		@Bind(R.id.ll_blog_item_commentcontainer)
		LinearLayout llCommentsContainer;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this,convertView);
		}


	}



}
