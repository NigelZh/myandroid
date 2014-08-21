package com.tempus.weijiujiao.view;

import java.util.ArrayList;

import com.tempus.weijiujiao.bean.ChartProp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * customized chartsView 自定义饼状图
 * 
 */

public class ChartView extends View {
	private boolean mAa;//锯齿
	private ArrayList<ChartProp> mChartProps = new ArrayList<ChartProp>();// 扇形的集合、个数
	private Point mCenterPoint;// 中心位置
	private int mR;// 设置饼状图半径
	private float mStartAngle;
	private int mWizardLineLength;// 引导线的长度
	private int w;
	private int color_default=Color.rgb(204, 204, 204);
	private int color_blue=Color.rgb(64, 139, 214);
	public ChartView(Context context) {
		super(context);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	public void init(int totalSize,int stock){
		mAa = true;
	
		ChartProp p2=new ChartProp(this);
		p2.setColor(color_blue);
		p2.setPercent((float)stock/totalSize);
		p2.setName(stock+"瓶");
		mChartProps.add(p2);
		ChartProp p1=new ChartProp(this);
		p1.setColor(color_default);
		p1.setPercent(1-(float)stock/totalSize);
		p1.setName(totalSize-stock+"瓶");
		mChartProps.add(p1);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		w = measureWidth(widthMeasureSpec);
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

	/**
	 * set the first chart's start angle when draw 设置第一个扇形绘制时的起始角度
	 * 
	 * @param startAngle
	 *            the first chart's start angle 第一个扇形绘制时的起始角度
	 */
	public void setStartAngle(float startAngle) {
		mStartAngle = startAngle;
		invalidate();
	}

	/**
	 * set the view anti alias. 设置是否抗锯齿。
	 * 
	 * @param aa
	 *            true means will draw hightly; true 意味着高质量绘图
	 */
	public void setAntiAlias(boolean aa) {
		mAa = aa;
		invalidate();
	}

	/**
	 * set chart's center point 设置饼状图的中心点
	 * 
	 * @param centerPoint
	 *            chart's center point 饼状图的中心点坐标
	 */
	public void setCenter(Point centerPoint) {
		mCenterPoint = centerPoint;
		invalidate();
	}

	/**
	 * set chart's radius 设置饼状图半径
	 * 
	 * @param r
	 *            chart's radius 饼状图的半径
	 */
	public void setR(int r) {
		mR = r;
		invalidate();
	}

	/**
	 * set wizard line's length 设置引导线的长度。斜着的和横着的是一样长的。
	 * 
	 * @param length
	 *            line's length 引导线的长度
	 */
	public void setWizardLineLength(int length) {
		mWizardLineLength = length;
		invalidate();
	}

	/**
	 * draw 具体绘制 TODO 绘制
	 */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		initParams();
		Paint paint = new Paint();
		paint.setAntiAlias(mAa);
		float startAngle = mStartAngle;
		int size = mChartProps.size();
		//
		RectF oval = new RectF(mCenterPoint.x - mR, mCenterPoint.y - mR,mCenterPoint.x + mR, mCenterPoint.y + mR);
		for (int i = 0; i < size; i++) {
			ChartProp chartProp = mChartProps.get(i);
			// drawArc
			paint.setColor(chartProp.getColor());
			float sweepAngle = chartProp.getSweepAngle();// 画多大
			canvas.drawArc(oval, startAngle, sweepAngle, true, paint);			
			// drawWizardLines -----splash line
			float wizardLineAngle = (float) ((startAngle + sweepAngle / 2)* Math.PI / 180);
			float deltaR = mR - mWizardLineLength / 2;
			double sinAngle = Math.sin(wizardLineAngle);
			double cosAngle = Math.cos(wizardLineAngle);
			int deltaXs = (int) (deltaR * cosAngle);
			int deltaYs = (int) (deltaR * sinAngle);
			int deltaXl = (int) (mWizardLineLength * cosAngle);
			int deltaYl = (int) (mWizardLineLength * sinAngle);
			Point lineSplashStart = new Point(mCenterPoint.x + deltaXs,
					mCenterPoint.y + deltaYs);
			Point lineSplashEnd = new Point(lineSplashStart.x + deltaXl,
					lineSplashStart.y + deltaYl);
			paint.setColor(Color.WHITE);
			if (i == 0) {// 只画已装数量引线
				paint.setColor(Color.BLACK);
				canvas.drawLine(lineSplashStart.x, lineSplashStart.y,lineSplashEnd.x, lineSplashEnd.y, paint);
				// drawWizardLines -----horizonal line，在圆左边就画左边，右边就画右边
				if (lineSplashEnd.x <= mCenterPoint.x) // in left of circle
				{
					canvas.drawLine(lineSplashEnd.x - mWizardLineLength,lineSplashEnd.y, lineSplashEnd.x, lineSplashEnd.y,paint);
				} else // in right of circle
				{
					canvas.drawLine(lineSplashEnd.x, lineSplashEnd.y,
							lineSplashEnd.x + mWizardLineLength,
							lineSplashEnd.y, paint);
				}
				paint.setColor(Color.WHITE);
			}

			// drawText
			if(chartProp.getPercent()!=0){
				String name = chartProp.getName();
				int nameLen = name.length();
				paint.setTextSize(mWizardLineLength/2);
				Rect rect = new Rect();
				paint.getTextBounds(name, 0, nameLen, rect);
				int endNameX = (mCenterPoint.x + lineSplashStart.x)/2;
				int endNameY = (mCenterPoint.y + lineSplashStart.y)/2;
				canvas.drawText(name, endNameX, endNameY, paint);
			}

			// drawPercent:添加百分比
			if (i == 0) {
				String percent = Math.round(chartProp.getPercent() * 100) + "%";
				int percentLen = percent.length();
				paint.setColor(Color.rgb(71, 188, 255));//设置为蓝色
				paint.setTextSize(mWizardLineLength/2);
				Rect rectPercent = new Rect();
				paint.getTextBounds(percent, 0, percentLen, rectPercent);
				int percentWidth = rectPercent.width();
				int percentHeight = rectPercent.height();
				if (lineSplashEnd.x <= mCenterPoint.x) // in left of circle
				{
					int endX = lineSplashEnd.x - mWizardLineLength - 15;
					int endY = (lineSplashEnd.y - percentHeight/2);
					canvas.drawText(percent, endX, endY, paint);
				} else {
					// in right of circle
					int endX = lineSplashEnd.x ;
					int endY = (lineSplashEnd.y - percentHeight/2);
					canvas.drawText(percent, endX, endY, paint);
				}
				// add startAngle
				chartProp.setStartAngle(startAngle);
				startAngle += sweepAngle;// 开始角度+需要画的角度
				chartProp.setEndAngle(startAngle);
			}
		}

	}

	private void initParams() {
		// TODO Auto-generated method stub
		mCenterPoint = new Point(w/2, w/2);
		mR = w/3;
		mStartAngle = -90;
		mWizardLineLength = mR/3;
	}
}
