package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.List;

import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.adapter.ProductInfoPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 商品信息
 * @author _blank :24611015@qq.com
 *
 */
public class ProductInfoFragment extends Fragment {

	private String[] TittleArray = { "基本信息", "酒庄介绍", "葡萄品种", "品鉴指南" };
	private TabPageIndicator indicator;
	private ViewPager vp;
	private FragmentManager fm;
	private List<Fragment> fList;
	private String code;
	private int codeType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		code = b.getString("code");
		codeType = b.getInt("codeType", -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		if (code != null && codeType != -1) {
			rootView = inflater.inflate(R.layout.product_info_layout, null);
			indicator = (TabPageIndicator) rootView.findViewById(R.id.product_info_indicator);
			vp = (ViewPager) rootView.findViewById(R.id.product_info_pager);
			vp.setOffscreenPageLimit(4);
			fm = ((DetailActivity) getActivity()).getSupportFragmentManager();
			initViewpager();
		} else {
			rootView = new TextView(getActivity());
			((TextView) rootView).setText("code||codeType 错误");
		}
		return rootView;
	}

	private void initViewpager() {
		// TODO Auto-generated method stub
		if (fList == null) {
			fList = new ArrayList<Fragment>();
		}
		if (fList.size() <= 0) {
			int i = 0;
			for (String s : TittleArray) {
				Fragment f = null;
				switch (i) {
				case 0:
					f = new ProductBasicInfoFragment();
					break;
				case 1:
					f = new ProductBrandInfoFrament();
					break;
				case 2:
					f = new ProductVarietyInfoFrament();
					break;
				case 3:
					f = new ProductTasteInfoFrament();
					break;
				default:
					break;
				}
				Bundle b = new Bundle();
				b.putString("code",code);
				b.putInt("codeType", codeType);
				f.setArguments(b);
				fList.add(f);
				i++;
			}
		}
		vp.setAdapter(new ProductInfoPagerAdapter(fList, TittleArray, fm));
		indicator.setViewPager(vp);
	}

}
