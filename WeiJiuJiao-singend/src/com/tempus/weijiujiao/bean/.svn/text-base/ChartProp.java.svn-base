package com.tempus.weijiujiao.bean;

import com.tempus.weijiujiao.view.ChartView;

import android.graphics.Color;
import android.view.View;

/**
 * customized  chartsViewProp
 * 自定义饼状图属性  
 *
 */

public class ChartProp
{
	private float mPercent = 1.0f;
	private View mParent = null;
	private int mColor = Color.WHITE;
	private float mSweepAngle = 0f;//角度
	private String mName = "";//名字
	private int mFontSize = 20;//字体大小
	private float mStartAngle = 0f;//开始角度
	private float mEndAngle = 0f;//结束角度
	
	public ChartProp(ChartView chartView)
	{
		mParent = chartView;
	}

	/**
	 * the percent ,such as 0.5f,0.05f, NEIGHTER 50% NOR 50
	 * @param percent
	 * 在饼图中占的比例
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
