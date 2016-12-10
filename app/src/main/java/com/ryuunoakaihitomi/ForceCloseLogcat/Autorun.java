package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.content.*;
import android.widget.*;

public class Autorun extends BroadcastReceiver
{
	@Override
	public void onReceive(Context p1, Intent p2)
	{
		if (!MainActivity.isRoot())
		{
			Toast.makeText(p1, "root获取失败，本应用不具有可正常运行的基础", Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			Intent i=new Intent(p1, FCListener.class);
			p1.startService(i);
			if (Boolean.valueOf(Config.G("mode")))
			{
				FCNotification.stateOn(overallSituationContext.get());
			}
			Toast.makeText(p1, "应用崩溃监听服务已运行", Toast.LENGTH_SHORT).show();
		}
	}   

}
