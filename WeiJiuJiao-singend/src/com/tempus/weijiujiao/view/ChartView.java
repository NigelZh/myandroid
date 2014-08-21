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
 * customized chartsView �Զ����״ͼ
 * 
 */

public class ChartView extends View {
	private boolean mAa;//���
	private ArrayList<ChartProp> mChartProps = new ArrayList<ChartProp>();// ���εļ��ϡ�����
	private Point mCenterPoint;// ����λ��
	private int mR;// ���ñ�״ͼ�뾶
	private float mStartAngle;
	private int mWizardLineLength;// �����ߵĳ���
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
		p2.setName(stock+"ƿ");
		mChartProps.add(p2);
		ChartProp p1=new ChartProp(this);
		p1.setColor(color_default);
		p1.setPercent(1-(float)stock/totalSize);
		p1.setName(totalSize-stock+"ƿ");
		mChartProps.add(p1);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		w = measureWidth(widthMeasureSpec);
	}
	
	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// �õ�ģʽ
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// �õ��ߴ�
		switch (widthMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = widthSize;
			break;
		}
		return result;
	}

	/**
	 * set the first chart's start angle when draw ���õ�һ�����λ���ʱ����ʼ�Ƕ�
	 * 
	 * @param startAngle
	 *            the first chart's start angle ��һ�����λ���ʱ����ʼ�Ƕ�
	 */
	public void setStartAngle(float startAngle) {
		mStartAngle = startAngle;
		invalidate();
	}

	/**
	 * set the view anti alias. �����Ƿ񿹾�ݡ�
	 * 
	 * @param aa
	 *            true means will draw hightly; true ��ζ�Ÿ�������ͼ
	 */
	public void setAntiAlias(boolean aa) {
		mAa = aa;
		invalidate();
	}

	/**
	 * set chart's center point ���ñ�״ͼ�����ĵ�
	 * 
	 * @param centerPoint
	 *            chart's center point ��״ͼ�����ĵ�����
	 */
	public void setCenter(Point centerPoint) {
		mCenterPoint = centerPoint;
		invalidate();
	}

	/**
	 * set chart's radius ���ñ�״ͼ�뾶
	 * 
	 * @param r
	 *            chart's radius ��״ͼ�İ뾶
	 */
	public void setR(int r) {
		mR = r;
		invalidate();
	}

	/**
	 * set wizard line's length ���������ߵĳ��ȡ�б�ŵĺͺ��ŵ���һ�����ġ�
	 * 
	 * @param length
	 *            line's length �����ߵĳ���
	 */
	public void setWizardLineLength(int length) {
		mWizardLineLength = length;
		invalidate();
	}

	/**
	 * draw ������� TODO ����
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
			float sweepAngle = chartProp.getSweepAngle();// �����
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
			if (i == 0) {// ֻ����װ��������
				paint.setColor(Color.BLACK);
				canvas.drawLine(lineSplashStart.x, lineSplashStart.y,lineSplashEnd.x, lineSplashEnd.y, paint);
				// drawWizardLines -----horizonal line����Բ��߾ͻ���ߣ��ұ߾ͻ��ұ�
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

			// drawPercent:���Ӱٷֱ�
			if (i == 0) {
				String percent = Math.round(chartProp.getPercent() * 100) + "%";
				int percentLen = percent.length();
				paint.setColor(Color.rgb(71, 188, 255));//����Ϊ��ɫ
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
				startAngle += sweepAngle;// ��ʼ�Ƕ�+��Ҫ���ĽǶ�
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