package com.easyandroid.demo.net_speed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;

public class NetWorkActivity extends BaseActivity {

	@Override
	protected int initLayout() {
		return R.layout.activity_network;
	}

	@Override
	protected void initTitle(TitleBar title) {
		title.title = "网络监控";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);
		Intent intent = new Intent(NetWorkActivity.this, NetSpeedService.class);
		// startService(intent);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);

	}

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}
}
