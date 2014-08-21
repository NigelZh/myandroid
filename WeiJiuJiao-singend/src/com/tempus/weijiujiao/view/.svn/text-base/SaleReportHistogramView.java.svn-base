package com.tempus.weijiujiao.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tempus.weijiujiao.bean.ReportEntity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class SaleReportHistogramView extends View {
	private Paint paint;
	private int w;// 屏幕宽度
	private int h, positionX, positionY;
	private int maxSalesVolume;// 最大销量
	private float xPitch;// 柱状图每百分之一的宽度
	private float yPitch;// 每个柱状图之间的间隙/布距
	private Point mPoint, xPoint, yPoint;// 坐标系的原点坐标、X轴末端坐标、Y轴末端坐标，
	private int xLength, yLength;// X、y轴长度
	private List<ReportEntity> reportList;// 柱状图的集合、个数

	public Point getmPoint() {
		return mPoint;
	}

	public void setmPoint(Point mPoint) {
		this.mPoint = mPoint;
	}

	public SaleReportHistogramView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SaleReportHistogramView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}

	public SaleReportHistogramView(Context context, AttributeSet attrs,
			int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
	}

	public void Init(List<ReportEntity> reportList) {
		this.reportList = reportList;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		w = measureWidth(widthMeasureSpec);
		h = measureHeight(heightMeasureSpec);
		int[] location = new int[2];
		this.getLocationOnScreen(location);
		positionX = location[0];
		positionY = location[1];
		setMeasuredDimension(w, h);
	}

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸
		switch (widthMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = widthSize;
			break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;
		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
		System.out.println("mode=" + heightMode + "whight=" + heightSize);
		switch (heightMode) {
		case MeasureSpec.UNSPECIFIED:
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = heightSize;
			break;
		}
		return result;
	}

	// 先初始化一些默认的参数
	private void initBasedata() {
		// TODO Auto-generated method stub
		comparatorFist();
		mPoint = new Point(w / 10, h / 20);// 坐标系的原点坐标
		xPoint = new Point(w * 9 / 10, h / 20);// X轴末端坐标
		yPoint = new Point(w / 10, h * 9 / 10);// Y轴末端坐标
		xLength = Math.round(w - mPoint.x * 2);// X轴长度
		yLength = Math.round(h - mPoint.y * 2);// y轴长度
		// xPitch = 20;//初始化的时候给个默认的
		yPitch = (yLength - 15) / 22;// 间隙占一个yPitch，柱状图占两个yPitch，总共11个柱状图
		float fPitch = (float) (Math.round((xLength * 0.8) * 100/ maxSalesVolume)) / 100;
		setxPitch(fPitch);
	
	}

	private void comparatorFist() {
		// TODO Auto-generated method stub
		Comparator<ReportEntity> comparator=new Comparator<ReportEntity>(){ 
			public int compare(ReportEntity r1, ReportEntity r2) { 
				return r2.getSaleCount()-r1.getSaleCount();
			}
		};
		Collections.sort(reportList, comparator);
		maxSalesVolume=reportList.get(0).getSaleCount();
	}
	public float getxPitch() {
		return xPitch;
	}

	public void setxPitch(float xPitch) {
		this.xPitch = xPitch;
	}

	public float getyPitch() {
		return yPitch;
	}

	public void setyPitch(float yPitch) {
		this.yPitch = yPitch;
	}

	/**
	 * 开始画图 1、两边箭头线（X轴Y轴） 2、和旁边的标题：单位瓶 3、循环画 （1）横向柱状图 （2）柱状图的名字 （3）柱状图的销量
	 * （4）柱状图下面的坐标线 （5）画ID
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		initBasedata();
		paint = new Paint();
		paint.setAntiAlias(true);// 去鋸齒
		paint.setColor(Color.GRAY);// 线设置为灰色的
		int size = reportList.size();
		// X轴画线、Y轴画线
		canvas.drawLine(mPoint.x, mPoint.y, xPoint.x, xPoint.y, paint);
		canvas.drawLine(mPoint.x, mPoint.y, yPoint.x, yPoint.y, paint);
		// 画箭头(图片还是画线)
		paint.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.moveTo(xPoint.x, xPoint.y);
		path.lineTo(xPoint.x - 10, xPoint.y - 5);
		path.lineTo(xPoint.x - 5, xPoint.y);
		path.close();
		canvas.drawPath(path, paint);
		path.moveTo(xPoint.x, xPoint.y);
		path.lineTo(xPoint.x - 10, xPoint.y + 5);
		path.lineTo(xPoint.x - 5, xPoint.y);
		path.close();
		canvas.drawPath(path, paint);
		// Y轴箭头
		path.moveTo(yPoint.x, yPoint.y);
		path.lineTo(yPoint.x - 5, yPoint.y - 10);
		path.lineTo(yPoint.x, yPoint.y - 5);
		path.close();
		canvas.drawPath(path, paint);
		path.moveTo(yPoint.x, yPoint.y);
		path.lineTo(yPoint.x + 5, yPoint.y - 10);
		path.lineTo(yPoint.x, yPoint.y - 5);
		path.close();
		canvas.drawPath(path, paint);
		// 画单位（单位/瓶）位置由屏幕和原点来确定
		String unitName = "单位/瓶";
		int unitNameLength = unitName.length();
		Rect rect = new Rect();
		paint.setTextSize(yPitch / 2);// 单位/瓶的字体大小
		paint.getTextBounds(unitName, 0, unitNameLength, rect);
		int unitNameWidth = rect.width();
		int unitNameHeight = rect.height();
		canvas.drawText(unitName, xPoint.x - unitNameWidth * 3 / 2, xPoint.y
				- unitNameHeight / 3, paint);
		// 循环画图（柱状图、柱状图标题、销量、下面的标题）
		for (int i = 0; i < size; i++) {
			ReportEntity entity = reportList.get(i);
			paint.setColor(Color.rgb(255 - 30 * i, 30*i+100, 30*i+100));
			float yStart = mPoint.y + yPitch * (2 * i + 1);
			float yEnd = mPoint.y + yPitch * (2 * i + 2);// 2yPitch等于柱状图的宽度（最大销量）
			int salesVolume = entity.getSaleCount();
			float histogramEntityXEnd = salesVolume * xPitch;
			// 画柱状图()
			canvas.drawRect(mPoint.x, yStart, mPoint.x + histogramEntityXEnd,
					yEnd, paint);
			paint.reset();
			paint.setAntiAlias(true);// 去鋸齒
			paint.setColor(Color.GRAY);
			// 画柱状图下面的线
			canvas.drawLine(mPoint.x, yEnd, xPoint.x, yEnd, paint);
			// 画柱状图右边的销量(需要右对齐)
			String strSalesVolume = salesVolume + "";
			int strSalesVolumeNameLength = strSalesVolume.length();
			Rect rectVolume = new Rect();
			paint.setTextSize(yPitch - 10);// 颜色同柱状图高度同时变化
			paint.getTextBounds(strSalesVolume, 0, strSalesVolumeNameLength,rectVolume);
			int strSalesVolumeWidth = rectVolume.width();
			int strSalesVolumeHeight = rectVolume.height();
			canvas.drawText(strSalesVolume, xPoint.x - strSalesVolumeWidth - 3,yEnd - (yPitch / 2 - strSalesVolumeHeight / 2), paint);
			// 画柱状图的名字+净重:需要注意是否超长，再做处理
			String nameWeight = entity.getName() + "（" + entity.getVolume()+ "ml）";
			paint.setColor(Color.BLACK);
			canvas.drawText(nameWeight, mPoint.x + 4, yEnd- (yPitch / 2 - strSalesVolumeHeight / 2), paint);
			Rect rectId = new Rect();
			String itemID=(i+1)+"";
			paint.getTextBounds(itemID, 0, itemID.length(), rectId);
			int idWidth = rectId.width();
			canvas.drawText(itemID, mPoint.x - idWidth - 10, yEnd- (yPitch / 2 - strSalesVolumeHeight / 2), paint);
		}
	}
}
