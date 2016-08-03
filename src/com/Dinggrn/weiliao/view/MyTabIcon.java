package com.Dinggrn.weiliao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.Dinggrn.weiliao.R;


public class MyTabIcon extends View{
    
	int textColor;//文本和图标的颜色
	int textSize;//底部文字的大小
	Drawable drawable;//使用的图标
	String textContent;//文字内容
	//因为只能绘制Bitmap类型的图片到屏幕上
	//所以需要将Drawable--->Bitmap
	Bitmap icon;
	
	Paint textPaint;//专门画文字的画笔
	Paint drawPaint;//专门画图标的画笔
	//alpha的取值范围是0~255
	//越接近于0，颜色越浅（透明）
	//越接近于255，颜色越深（不透明）
	int alpha;//通过alpha值，来设定textColor的深浅(透明度)
	
	
	public MyTabIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
		initPaint();
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		textPaint = new Paint();
		textPaint.setTextSize(textSize);
		textPaint.setStyle(Style.FILL);
		//画笔的颜色因为在绘制要反复修改，所以没必要在这里指定
		//textPaint.setColor(color);
		
		drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//不设置颜色，道理同上
	}


	/**
	 * 读取布局文件中设置的内容
	 * 为四个属性赋值
	 */
	private void initView(Context context, AttributeSet attrs) {
		TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MyTabIcon);
		textColor = t.getColor(R.styleable.MyTabIcon_icon_color, Color.GREEN);
		int defValue = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 
				12, getResources().getDisplayMetrics());
		textSize = t.getDimensionPixelSize(
				R.styleable.MyTabIcon_icon_textsize, 
				defValue );
		//获得用户在布局文件中指定的drawable类型的文件
		drawable = t.getDrawable(R.styleable.MyTabIcon_icon_icon);
		//把一个Drawable类型的图片转为对应的Bitmap类型
		icon = ((BitmapDrawable)drawable).getBitmap();
		//icon显示时发现太小，放大
		icon = Bitmap.createScaledBitmap(icon, icon.getWidth()*2, icon.getHeight()*2, true);
		
		//获得用户在布局文件中指定的文本内容
		textContent = t.getString(R.styleable.MyTabIcon_icon_textcontent);
		t.recycle();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画图标
		//(left,top)代表图像的左上角坐标
		//图像的横坐标(控件宽度/2-图像的宽度/2)
		float left = getWidth()/2 - icon.getWidth()/2;
		//图像的纵坐标(控件高度/2-图像的高度/2)
		float top = getHeight()/2 - icon.getHeight()/2 - 10;
		canvas.drawBitmap(icon, left, top, drawPaint);
		//画文字
		//文字的范围
		Rect bounds = new Rect();
		textPaint.getTextBounds(
				textContent, 
				0, textContent.length(), bounds);
		//(x,y)代表文字的左下角坐标
		//文字的横坐标(控件宽度/2-文字宽度/2)
		float x = getWidth()/2-bounds.width()/2;
		//文字的纵坐标(控件高度/2+图片高度/2+文字宽度/2)
		float y = getHeight()/2+icon.getHeight()/2+bounds.height() - 20;
		textPaint.setColor(Color.GRAY);
		textPaint.setAlpha(255);//TODO 注释掉看看有啥不同
		canvas.drawText(textContent, x, y, textPaint);
		drawColorText(canvas,x,y);
		drawColorIcon(canvas,left,top);
	}
	/**
	 * 绘制带颜色的图标
	 * @param canvas 可以画到屏幕上的canvas
	 * @param left
	 * @param top
	 */
	private void drawColorIcon(Canvas canvas, float left, float top) {
		
		Bitmap bitmap = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Config.ARGB_8888);
		//myCanvas是用来画bitmap的
		Canvas myCanvas = new Canvas(bitmap);
		myCanvas.drawBitmap(icon, 0, 0, drawPaint);
		
		Rect r = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		drawPaint.setColor(textColor);
		drawPaint.setAlpha(alpha);
		drawPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		myCanvas.drawRect(r, drawPaint);
		//把刚才画带颜色图片时为画笔设置的
		//颜色，透明度和混合模式统统去掉
		//恢复到最初始时的画笔
		drawPaint.reset();//TODO 如果把这句注释掉，会有什么不同
		//把画好的bitmap画到屏幕上
		canvas.drawBitmap(bitmap, left, top, null);
		
	}

	/**
	 * 绘制带颜色的文本
	 * @param canvas 可以画到屏幕上的canvas
	 * @param x
	 * @param y
	 */
	private void drawColorText(Canvas canvas, float x, float y) {
		textPaint.setColor(textColor);
		//为画笔设置透明度
		textPaint.setAlpha(alpha);
		canvas.drawText(textContent, x, y, textPaint);
		
	}
	
	public void setMyTabIconAlpha(int alpha){
		this.alpha = alpha;
		//通过invalidate方法，让系统不断的去
		//调用onDraw方法
		invalidate();
	}
}
