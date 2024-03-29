/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package djh.bzu.sport.pedometer;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.bzu.entity.sportData;
import cn.edu.bzu.walker.R;

public class Pedometer extends Activity {
	private static final String TAG = "Pedometer";
	private SharedPreferences mSettings;
	private PedometerSettings mPedometerSettings;
	private Utils mUtils;

	private boolean isStart;

	private TextView mStepValueView;
	private TextView mPaceValueView;
	// private TextView mDistanceValueView;
	private TextView mSpeedValueView;
	// private TextView mCaloriesValueView;
	TextView mDesiredPaceView;
	private int mStepValue;


	private int mPaceValue;
	private float mDistanceValue;
	private float mSpeedValue;
	private int mCaloriesValue;
	private float mDesiredPaceOrSpeed;
	private int mMaintain;
	@SuppressWarnings("unused")
	private boolean mIsMetric;
	private boolean mQuitting = false;
	private Chronometer timer;
	private TextView sport_start;
	private TextView shangchuan;// 用于跳转到上传activity

	private long recordingTime = 0;// 用来保存时间
	
	private ImageView title_bar_menu_back;
	private ImageView title_bar_menu_setting;

	public BmobUser bmobUser;

	/**
	 * True, when service is running.
	 */
	private boolean mIsRunning;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "[ACTIVITY] onCreate");
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题栏
		mStepValue = 0;
		mPaceValue = 0;

		setContentView(R.layout.djh_walk);

		mUtils = Utils.getInstance();
		
		Bmob.initialize(this, "ec474218c0f9d695c62f5084fb532e00");
		bmobUser=BmobUser.getCurrentUser(this);
		title_bar_menu_back = (ImageView) super.findViewById(R.id.title_bar_menu_back);
		title_bar_menu_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onBackPressed();
				
			}
		});
		title_bar_menu_setting = (ImageView) super.findViewById(R.id.title_bar_menu_setting);
		title_bar_menu_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent (Pedometer.this,Settings.class) );
				
			}
		});

	}

	
	@Override
	public void onBackPressed() {
		
		if(mStepValue == 0){
			finish();
			
		}else {
			//TODO  判断用户是否已经登录
			
			if(bmobUser!=null){
				sportData data = new sportData();
				String username=BmobUser.getCurrentUser(Pedometer.this).getUsername();
				data.setStep(String.valueOf(mStepValue));
				
				data.setUserName(username);
				data.setTime(String.valueOf(recordingTime / 1000));
				data.setDistance(String.valueOf(mDistanceValue * 1000));

				Date createdDate = new Date(System.currentTimeMillis());
				data.setCreateTime(createdDate);
				data.save(Pedometer.this, new SaveListener() {

					@Override
					public void onSuccess() {
						Toast.makeText(getApplicationContext(), "记录上传成功",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(getApplicationContext(),
								"网络故障，数据未能上传到网络", Toast.LENGTH_SHORT).show();

					}
				});
				resetValues(false);
				// unbindStepService();
				stopStepService();
				mQuitting = true;
				finish();
			}else {
				Toast.makeText(this, "亲，您还没有登录", Toast.LENGTH_SHORT).show();
			}
		}
		
		
		
		super.onBackPressed();
	}


	@Override
	protected void onStart() {
		Log.i(TAG, "[ACTIVITY] onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "[ACTIVITY] onResume");
		super.onResume();

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mPedometerSettings = new PedometerSettings(mSettings);

		mUtils.setSpeak(mSettings.getBoolean("speak", false));

		// Read from preferences if the service was running on the last onPause
		mIsRunning = mPedometerSettings.isServiceRunning();

		// Start the service if this is considered to be an application start
		// (last onPause was long ago)
		if (!mIsRunning && mPedometerSettings.isNewStart()) {

			// startStepService();
			// bindStepService();
		} else if (mIsRunning) {
			bindStepService();
		}

		mPedometerSettings.clearServiceRunning();

		shangchuan = (TextView) super.findViewById(R.id.shangchuan);

		timer = (Chronometer) this.findViewById(R.id.chronometer);
		mStepValueView = (TextView) findViewById(R.id.step_value);// 步数
		mPaceValueView = (TextView) findViewById(R.id.pace_value);// 步长
		// mDistanceValueView = (TextView)
		// findViewById(R.id.distance_value);//路程
		mSpeedValueView = (TextView) findViewById(R.id.speed_value);// 速度
		// mCaloriesValueView = (TextView)
		// findViewById(R.id.calories_value);//卡路里
		// mDesiredPaceView = (TextView) findViewById(R.id.desired_pace_value);

		sport_start = (TextView) findViewById(R.id.sport_start);
		mIsMetric = mPedometerSettings.isMetric();

		shangchuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(mStepValue!=0){
					if(bmobUser!=null){
						sportData data = new sportData();
						data.setStep(String.valueOf(mStepValue));
						
						
						
						String username=BmobUser.getCurrentUser(Pedometer.this).getUsername();
						
						
						data.setUserName(username);
						data.setTime(String.valueOf(recordingTime / 1000));
						data.setDistance(String.valueOf(mDistanceValue * 1000));

						Date createdDate = new Date(System.currentTimeMillis());
						data.setCreateTime(createdDate);
						data.save(Pedometer.this, new SaveListener() {

							@Override
							public void onSuccess() {
								timer.setBase(SystemClock.elapsedRealtime());
								Toast.makeText(getApplicationContext(), "成功",
										Toast.LENGTH_SHORT).show();
								resetValues(false);
								// unbindStepService();
								stopStepService();
								mQuitting = true;
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								// 网络数据库上传上传失败后会保存到本地的数据库

								Toast.makeText(getApplicationContext(),
										"网络故障，数据未能上传到网络", Toast.LENGTH_SHORT)
										.show();

							}
						});
					}else {
						Toast.makeText(Pedometer.this, "亲，您还没有登录",Toast.LENGTH_SHORT ).show();
					}
					
				}else{
					Toast.makeText(Pedometer.this, "亲，您还没有运动",Toast.LENGTH_SHORT ).show();
				}
		}
				
		});

		sport_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isStart) {
					// timer.setBase(SystemClock.elapsedRealtime());
					timer.setBase(SystemClock.elapsedRealtime() - recordingTime);
					// 开始计时
					timer.start();
					sport_start.setText("暂停");
					startStepService();
					bindStepService();
					//resetValues(isStart);
					sport_start
							.setBackgroundResource(R.drawable.sport_start_shape_after);

					isStart = true;
				} else {
					sport_start.setText("开始");
					recordingTime = SystemClock.elapsedRealtime()
							- timer.getBase();// 保存这次记录了的时间
					unbindStepService();
					stopStepService();
					sport_start
							.setBackgroundResource(R.drawable.sport_start_shape_before);
					isStart = false;
				}

			}
		});
		// ((TextView) findViewById(R.id.distance_units)).setText(getString(
		// mIsMetric
		// ? R.string.kilometers
		// : R.string.miles
		// ));
		// ((TextView) findViewById(R.id.speed_units)).setText(getString(
		// mIsMetric
		// ? R.string.kilometers_per_hour
		// : R.string.miles_per_hour
		// ));
		//
		// mMaintain = mPedometerSettings.getMaintainOption();
		// ((LinearLayout)
		// this.findViewById(R.id.desired_pace_control)).setVisibility(
		// mMaintain != PedometerSettings.M_NONE
		// ? View.VISIBLE
		// : View.GONE
		// );
		// if (mMaintain == PedometerSettings.M_PACE) {
		// mMaintainInc = 5f;
		// mDesiredPaceOrSpeed = (float)mPedometerSettings.getDesiredPace();
		// }
		// else
		// if (mMaintain == PedometerSettings.M_SPEED) {
		// mDesiredPaceOrSpeed = mPedometerSettings.getDesiredSpeed();
		// mMaintainInc = 0.1f;
		// }
		// Button button1 = (Button)
		// findViewById(R.id.button_desired_pace_lower);
		// button1.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// mDesiredPaceOrSpeed -= mMaintainInc;
		// mDesiredPaceOrSpeed = Math.round(mDesiredPaceOrSpeed * 10) / 10f;
		// displayDesiredPaceOrSpeed();
		// setDesiredPaceOrSpeed(mDesiredPaceOrSpeed);
		// }
		// });
		// Button button2 = (Button)
		// findViewById(R.id.button_desired_pace_raise);
		// button2.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// mDesiredPaceOrSpeed += mMaintainInc;
		// mDesiredPaceOrSpeed = Math.round(mDesiredPaceOrSpeed * 10) / 10f;
		// displayDesiredPaceOrSpeed();
		// setDesiredPaceOrSpeed(mDesiredPaceOrSpeed);
		// }
		// });
		// if (mMaintain != PedometerSettings.M_NONE) {
		// ((TextView) findViewById(R.id.desired_pace_label)).setText(
		// mMaintain == PedometerSettings.M_PACE
		// ? R.string.desired_pace
		// : R.string.desired_speed
		// );
		// }
		//

		// displayDesiredPaceOrSpeed();

	}

	@Override
	protected void onPause() {
		Log.i(TAG, "[ACTIVITY] onPause");
		if (mIsRunning) {
			unbindStepService();
		}
		if (mQuitting) {
			mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
		} else {
			mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
		}

		super.onPause();
		savePaceSetting();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "[ACTIVITY] onStop");
		super.onStop();
	}

	protected void onDestroy() {
		Log.i(TAG, "[ACTIVITY] onDestroy");
		super.onDestroy();
	}

	protected void onRestart() {
		Log.i(TAG, "[ACTIVITY] onRestart");
		super.onDestroy();
	}

	private void savePaceSetting() {
		mPedometerSettings.savePaceOrSpeedSetting(mMaintain,
				mDesiredPaceOrSpeed);
	}

	private StepService mService;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((StepService.StepBinder) service).getService();

			mService.registerCallback(mCallback);
			mService.reloadSettings();

		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	private void startStepService() {
		if (!mIsRunning) {
			Log.i(TAG, "[SERVICE] Start");
			mIsRunning = true;
			startService(new Intent(Pedometer.this, StepService.class));
		}
	}

	private void bindStepService() {
		Log.i(TAG, "[SERVICE] Bind");
		bindService(new Intent(Pedometer.this, StepService.class), mConnection,
				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
	}

	private void unbindStepService() {
		Log.i(TAG, "[SERVICE] Unbind");
		unbindService(mConnection);
	}

	private void stopStepService() {
		Log.i(TAG, "[SERVICE] Stop");
		if (mService != null) {
			Log.i(TAG, "[SERVICE] stopService");
			stopService(new Intent(Pedometer.this, StepService.class));
		}
		timer.stop();
		mIsRunning = false;
	}

	private void resetValues(boolean updateDisplay) {
		// if (mService != null && mIsRunning) {
		 mService.resetValues();
		// } else {
		mStepValueView.setText("0");
		mPaceValueView.setText("0");
		// mDistanceValueView.setText("0");
		mSpeedValueView.setText("0");
		// mCaloriesValueView.setText("0");
		SharedPreferences state = getSharedPreferences("state", 0);
		SharedPreferences.Editor stateEditor = state.edit();
		if (!updateDisplay) {
			stateEditor.putInt("steps", 0);
			stateEditor.putInt("pace", 0);
			stateEditor.putFloat("distance", 0);
			stateEditor.putFloat("speed", 0);
			stateEditor.putFloat("calories", 0);
			stateEditor.commit();
		}
		// }
		// }
	}



	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
//		if (mIsRunning) {
//			menu.add(0, MENU_PAUSE, 0, R.string.pause)
//					.setIcon(android.R.drawable.ic_media_pause)
//					.setShortcut('1', 'p');
//		} else {
//			menu.add(0, MENU_RESUME, 0, R.string.resume)
//					.setIcon(android.R.drawable.ic_media_play)
//					.setShortcut('1', 'p');
//		}
//		menu.add(0, MENU_RESET, 0, R.string.reset)
//				.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
//				.setShortcut('2', 'r');
//		menu.add(0, MENU_SETTINGS, 0, R.string.settings)
//				.setIcon(android.R.drawable.ic_menu_preferences)
//				.setShortcut('8', 's')
//				.setIntent(new Intent(Pedometer.this, Settings.class));
//		menu.add(0, MENU_QUIT, 0, R.string.quit)
//				.setIcon(android.R.drawable.ic_lock_power_off)
//				.setShortcut('9', 'q');
		return true;
	}


	private StepService.ICallback mCallback = new StepService.ICallback() {
		public void stepsChanged(int value) {
			mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
		}

		public void paceChanged(int value) {
			mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
		}

		public void distanceChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG,
					(int) (value * 1000), 0));
		}

		public void speedChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG,
					(int) (value * 1000), 0));
		}

		public void caloriesChanged(float value) {
			mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG,
					(int) (value), 0));
		}
	};

	private static final int STEPS_MSG = 1;
	private static final int PACE_MSG = 2;
	private static final int DISTANCE_MSG = 3;
	private static final int SPEED_MSG = 4;
	private static final int CALORIES_MSG = 5;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STEPS_MSG:
				mStepValue = (int) msg.arg1;
				mStepValueView.setText("" + mStepValue);
				break;
			case PACE_MSG:
				mPaceValue = msg.arg1;
				if (mPaceValue <= 0) {
					mPaceValueView.setText("0");
				} else {
					mPaceValueView.setText("" + (int) mPaceValue);
				}
				break;
			case DISTANCE_MSG:
				mDistanceValue = ((int) msg.arg1) / 1000f;
				if (mDistanceValue <= 0) {
					// mDistanceValueView.setText("0");
				} else {
					// mDistanceValueView.setText(
					// ("" + (mDistanceValue + 0.000001f)).substring(0, 5)
					// );
				}
				break;
			case SPEED_MSG:
				mSpeedValue = ((int) msg.arg1) / 1000f;
				if (mSpeedValue <= 0) {
					mSpeedValueView.setText("0");
				} else {
					mSpeedValueView.setText(("" + (mSpeedValue + 0.000001f))
							.substring(0, 4));
				}
				break;
			case CALORIES_MSG:
				mCaloriesValue = msg.arg1;
				if (mCaloriesValue <= 0) {
					// mCaloriesValueView.setText("0");
				} else {
					// mCaloriesValueView.setText("" + (int)mCaloriesValue);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}

	};

}