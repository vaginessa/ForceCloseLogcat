package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public class FCNotification
{
	static Context c=overallSituationContext.get();
	static Notification stateOn()
	{
		Notification n=new Notification.Builder(c)
			.setContentTitle("应用崩溃监听服务")
			.setContentText("启动中......")
			.setOngoing(true)
			.setContentIntent(PendingIntent
							  .getActivity(c, 0, new Intent(c, MainActivity
															.class), 0))
			.setSmallIcon(R.drawable.ic_launcher)
			.build();
		n.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONLY_ALERT_ONCE;
		return n;
	}
	static void caught()
	{
		Notification.BigTextStyle nbs=new Notification.BigTextStyle();
		nbs.bigText(FCGetWork.FCLogBody());
		Intent i = new Intent(c, LogReader.class);
		PendingIntent p = PendingIntent.getActivity(c, 0, i, 0);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		Notification.Builder nb=new Notification.Builder(c);
		boolean b=FileGod.R("/sdcard/FClog/cache/mode").equals("jvm");
		if (b)
		{
			nb.setContentTitle("JVM FC已被捕获");
		}
		else
		{
			nb.setContentTitle("NDK FC已被捕获");
		}
		nb.setWhen(0)
			.setDeleteIntent(PendingIntent
							 .getBroadcast(c, 0, new Intent(UtilityTools.pkg + ".slide"), 0))
			.setContentText("时刻 " + NowTimeText.get(true))
			.setSubText("来自 " + UtilityTools.getProgramNameByPackageName(FCGetWork.FCPackageName()) + "(" + FCGetWork.FCPID() + ")")
			.setContentIntent(p)
			.setStyle(nbs)
			.addAction(0, "复制", PendingIntent.getBroadcast(c, 0, new Intent(UtilityTools.pkg + ".copy"), 0))
			.addAction(0, "删除", PendingIntent.getBroadcast(c, 0, new Intent(UtilityTools.pkg + ".delete"), 0))
			.addAction(0, "分享", PendingIntent.getBroadcast(c, 0, new Intent(UtilityTools.pkg + ".share"), 0))
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.ic_launcher);
		if (Build.VERSION.SDK_INT >= 21)
		{
			nb.setColor(Color.RED)
				.setFullScreenIntent(p, true);
		}
		Notification n=nb.build();

		n.flags = Notification.FLAG_AUTO_CANCEL;
		n.priority = Notification.PRIORITY_MIN;
		NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify("ForceCloseLogcat", 2, n);
	}
}
