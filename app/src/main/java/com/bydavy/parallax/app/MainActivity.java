package com.bydavy.parallax.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {

	private TrackingScrollView mScroller;
	private View mImageSpacer;
	private ImageView mImage;
	private View mPresentation;
	private int mImageHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageSpacer = findViewById(R.id.image_space);

		mImage = (ImageView) findViewById(R.id.image);
		mImageHeight = mImage.getLayoutParams().height;

		mScroller = (TrackingScrollView) findViewById(R.id.scroller);
		mScroller.setOnScrollChangedListener(new TrackingScrollView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged(TrackingScrollView source, int l, int t, int oldl, int oldt) {
				handleScroll(source, t);
			}
		});

		mPresentation = findViewById(R.id.presentation);
		centerViewVertically(mPresentation);
	}

	private void handleScroll(TrackingScrollView source, int top) {
		final float imageScrollingPercent = Math.min(mImageHeight, Math.max(0, top)) / (float) mImageHeight;

		// Increase spacer height on Y increase
		mImageSpacer.getLayoutParams().height = (int) (mImageHeight * imageScrollingPercent);
		mImageSpacer.setLayoutParams(mImageSpacer.getLayoutParams());

		// Shrink image height on Y increase
		mImage.getLayoutParams().height = (int) (mImageHeight * (1 - imageScrollingPercent));
		mImage.setLayoutParams(mImage.getLayoutParams());
	}

	private void centerViewVertically(View view) {
		view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				v.setTranslationY(-v.getHeight() / 2);
				v.removeOnLayoutChangeListener(this);
			}
		});
	}

}
