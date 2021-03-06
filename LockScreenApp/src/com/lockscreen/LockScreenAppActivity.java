package com.lockscreen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class LockScreenAppActivity extends Activity {

	/** Called when the activity is first created. */
	KeyguardManager.KeyguardLock k1;
	boolean inDragMode;
	int selectedImageViewX;
	int selectedImageViewY;
	int windowwidth;
	int windowheight;
	ImageView droid, phone, home;
	int home_x, home_y;
	int[] droidpos;

	private LayoutParams layoutParams;

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onAttachedToWindow();
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		droid = (ImageView) findViewById(R.id.droid);

		System.out.println("measured width" + droid.getMeasuredWidth());
		System.out.println(" width" + droid.getWidth());

		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			finish();
		}

		try {
			startService(new Intent(this, MyService.class));

			StateListener phoneStateListener = new StateListener();
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);

			windowwidth = getWindowManager().getDefaultDisplay().getWidth();
			System.out.println("windowwidth" + windowwidth);
			windowheight = getWindowManager().getDefaultDisplay().getHeight();
			System.out.println("windowheight" + windowheight);

			MarginLayoutParams marginParams2 = new MarginLayoutParams(
					droid.getLayoutParams());

			marginParams2.setMargins((windowwidth / 24) * 10,
					((windowheight / 32) * 8), 0, 0);

			RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
					marginParams2);

			droid.setLayoutParams(layoutdroid);

			LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
			homelinear.setPadding(0, 0, 0, (windowheight / 32) * 3);
			home = (ImageView) findViewById(R.id.home);

			MarginLayoutParams marginParams1 = new MarginLayoutParams(
					home.getLayoutParams());

			marginParams1.setMargins((windowwidth / 24) * 10, 0,
					(windowheight / 32) * 8, 0);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					marginParams1);

			home.setLayoutParams(layout);

			droid.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					layoutParams = (LayoutParams) v.getLayoutParams();

					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:
						int[] hompos = new int[2];
						droidpos = new int[2];
						home.getLocationOnScreen(hompos);
						home_x = hompos[0];
						home_y = hompos[1];

						break;
					case MotionEvent.ACTION_MOVE:
						int x_cord = (int) event.getRawX();
						int y_cord = (int) event.getRawY();

						if (x_cord > windowwidth - (windowwidth / 24)) {
							x_cord = windowwidth - (windowwidth / 24) * 2;
						}
						if (y_cord > windowheight - (windowheight / 32)) {
							y_cord = windowheight - (windowheight / 32) * 2;
						}

						layoutParams.leftMargin = x_cord;
						layoutParams.topMargin = y_cord;

						droid.getLocationOnScreen(droidpos);
						v.setLayoutParams(layoutParams);

						if (((x_cord - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord) <= (windowwidth / 24) * 4)
								&& ((home_y - y_cord) <= (windowheight / 32) * 5)) {
							System.out.println("home overlapps");
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord
									+ " " + droidpos[1]);

							v.setVisibility(View.GONE);

							finish();
						} else {
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord
									+ " " + droidpos[1]);

							System.out.println("home notttt overlapps");
						}
						break;
					case MotionEvent.ACTION_UP:

						int x_cord1 = (int) event.getRawX();
						int y_cord2 = (int) event.getRawY();

						if (((x_cord1 - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord1) <= (windowwidth / 24) * 4)
								&& ((home_y - y_cord2) <= (windowheight / 32) * 5)) {
							System.out.println("home overlapps");
							System.out.println("homeee" + home_x + "  "
									+ (int) event.getRawX() + "  " + x_cord1
									+ " " + droidpos[0]);

							System.out.println("homeee" + home_y + "  "
									+ (int) event.getRawY() + "  " + y_cord2
									+ " " + droidpos[1]);
						} else {

							layoutParams.leftMargin = (windowwidth / 24) * 10;
							layoutParams.topMargin = (windowheight / 32) * 8;
							v.setLayoutParams(layoutParams);
						}
					}
					return true;
				}
			});
		} catch (Exception e) {
		}

	}

	class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("call Activity off hook");
				finish();

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	public void onSlideTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();

			if (x_cord > windowwidth) {
				x_cord = windowwidth;
			}
			if (y_cord > windowheight) {
				y_cord = windowheight;
			}

			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 75;

			view.setLayoutParams(layoutParams);
			break;
		default:
			break;

		}
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			return true;
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {
			System.out.println("OK");
			return true;
		}
		return false;
	}

	public void onDestroy() {
		super.onDestroy();
	}

}