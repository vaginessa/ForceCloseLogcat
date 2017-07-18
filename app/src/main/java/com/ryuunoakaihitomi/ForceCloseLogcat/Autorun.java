package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Autorun extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Intent i=new Intent(p1, FCListener.class);
		p1.startService(i);
		Toast.makeText(p1, "应用崩溃监听服务已运行", Toast.LENGTH_SHORT).show();
	}
}
