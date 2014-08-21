package com.tempus.weijiujiao;



import com.tempus.weijiujiao.view.TraceBackTouchImageView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


public class TraceBackImageActivity extends Activity {

	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private TraceBackTouchImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.traceback_image_detail);
		String url = getIntent().getStringExtra(EXTRA_IMAGE_URLS);
		mImageView = (TraceBackTouchImageView) this.findViewById(R.id.image);
		mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				finish();
			}
		});	
		MyApplication.getinstance().getImageLoader().displayImage(url, mImageView);
	}
}