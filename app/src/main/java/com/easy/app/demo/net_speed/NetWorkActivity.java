package com.easy.app.demo.net_speed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.title.TitleBar;

public class NetWorkActivity extends BaseActivity<TitleBar> {

	@Override
	protected Object initLayout() {
		return R.layout.activity_network;
	}

	@Override
	protected void initTitle(TitleBar title) {
		title.title = "网络监控";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
