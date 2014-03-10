package com.bydavy.parallax.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {

	private TrackingScrollView mScroller;
	private ImageView mImage;
	private View mPresentation;
	private View mContent;
	private Drawable mWindowDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWindowDrawable = getWindow().getDecorView().getBackground();

		mImage = (ImageView) findViewById(R.id.image);

		mScroller = (TrackingScrollView) findViewById(R.id.scroller);
		mScroller.setOnScrollChangedListener(new TrackingScrollView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged(TrackingScrollView source, int l, int t, int oldl, int oldt) {
				handleScroll(source, t);
			}
		});

		mContent = findViewById(R.id.content);

		mPresentation = findViewById(R.id.presentation);
		centerViewVertically(mPresentation);
	}

	private void handleScroll(TrackingScrollView source, int top) {
		final float actionBarHeight = getActionBar().getHeight();
		final float scrollerVisibleHeight = mScroller.getHeight() - actionBarHeight;
		final float scrollingPercent = Math.min(scrollerVisibleHeight, Math.max(0, top)) / scrollerVisibleHeight;

		mImage.setTranslationY(-scrollerVisibleHeight / 3.0f * scrollingPercent);

		optimizeDrawing(source, scrollingPercent);
	}

	/**
	 * Optimize drawing by removing Image View when completely covered.
	 *
	 * @param source
	 * @param scrollingPercent
	 */
	private void optimizeDrawing(TrackingScrollView source, float scrollingPercent) {
		final int newVisibility;
		final Drawable newBackground;
		// FIXME It sounds scary to have a strict comparison of floats
		if (scrollingPercent < 1.0f) {
			newVisibility = View.VISIBLE;
			newBackground = mWindowDrawable;
		} else {
			// Don't draw the image if it's completely hidden by other view above it
			newVisibility = View.GONE;
			// Don't draw the window background image
			newBackground = null;
		}

		final View decorView = getWindow().getDecorView();
		Drawable currentDrawable = decorView.getBackground();

		if (currentDrawable != newBackground) {
			decorView.setBackgroundDrawable(newBackground);
		}

		if (mImage.getVisibility() != newVisibility) {
			mImage.setVisibility(newVisibility);
		}
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
