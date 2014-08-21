package com.tempus.weijiujiao.bean;

import com.tempus.weijiujiao.view.ChartView;

import android.graphics.Color;
import android.view.View;

/**
 * customized  chartsViewProp
 * �Զ����״ͼ����  
 *
 */

public class ChartProp
{
	private float mPercent = 1.0f;
	private View mParent = null;
	private int mColor = Color.WHITE;
	private float mSweepAngle = 0f;//�Ƕ�
	private String mName = "";//����
	private int mFontSize = 20;//�����С
	private float mStartAngle = 0f;//��ʼ�Ƕ�
	private float mEndAngle = 0f;//�����Ƕ�
	
	public ChartProp(ChartView chartView)
	{
		mParent = chartView;
	}

	/**
	 * the percent ,such as 0.5f,0.05f, NEIGHTER 50% NOR 50
	 * @param percent
	 * �ڱ�ͼ��ռ�ı���
	 */
	public void setPercent(float percent)
	{
		mPercent = percent;
		mSweepAngle = percent * 360;
		invalidate();
	}
	
	public float getPercent()
	{
		return mPercent;
	}
	
	public float getSweepAngle()
	{
		return mSweepAngle;
	}
	
	public void setColor(int color)
	{
		mColor = color;
		invalidate();
	}
	
	public int getColor()
	{
		return mColor;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		this.mName = name;
		invalidate();
	}
	
	public int getFontSize()
	{
		return mFontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.mFontSize = fontSize;
		invalidate();
	}
	
	private void invalidate()
	{
		if(mParent != null)
		{
			mParent.invalidate();
		}
	}

	public float getStartAngle()
	{
		return mStartAngle;
	}

	public void setStartAngle(float startAngle)
	{
		this.mStartAngle = startAngle;
	}

	public float getEndAngle()
	{
		return mEndAngle;
	}

	public void setEndAngle(float endAngle)
	{
		this.mEndAngle = endAngle;
	}



}
