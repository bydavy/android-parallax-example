package com.bydavy.parallax.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainActivity extends Activity {

	private ImageView mImage;
	private View mPresentation;
	private int mImageHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImage = (ImageView) findViewById(R.id.image);
		mImageHeight = mImage.getLayoutParams().height;

		((TrackingScrollView) findViewById(R.id.scroller)).setOnScrollChangedListener(
				new TrackingScrollView.OnScrollChangedListener() {
					@Override
					public void onScrollChanged(TrackingScrollView source, int l, int t, int oldl, int oldt) {
						handleScroll(source, t);
					}
				}
		);

		mPresentation = findViewById(R.id.presentation);
		centerViewVertically(mPresentation);
	}

	private void handleScroll(TrackingScrollView source, int top) {
		int scrolledImageHeight = Math.min(mImageHeight, Math.max(0, top));

		ViewGroup.MarginLayoutParams imageParams = (ViewGroup.MarginLayoutParams) mImage.getLayoutParams();
		int newImageHeight = mImageHeight - scrolledImageHeight;
		if (imageParams.height != newImageHeight) {
			// Transfer image height to margin top
			imageParams.height = newImageHeight;
			imageParams.topMargin = scrolledImageHeight;

			// Invalidate view
			mImage.setLayoutParams(imageParams);
		}
	}

	private static void centerViewVertically(View view) {
		view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				v.setTranslationY(-v.getHeight() / 2);
				v.removeOnLayoutChangeListener(this);
			}
		});
	}

}
