package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.widget.Toast;

public class FCGetWork
{
	public static void FCReceive()
	{
		if (!UtilityTools.isForeground(UtilityTools.pkg + ".LogReader"))
		{
			UtilityTools.vibrate();
			NotificationManager nm= (NotificationManager) overallSituationContext.get().getSystemService(overallSituationContext.get().NOTIFICATION_SERVICE);
			nm.cancel("ForceCloseLogcat", 1);
			FCNotification.caught();
		}
		else
		{
			new Handler(Looper.getMainLooper()).post(new Runnable() 
				{
					@Override
					public void run()
					{
						Toast.makeText(overallSituationContext.get(), String.format("检测到你正在查看日志，所以最新的日志自动保存但不弹出通知栏通知。可自行打开文件管理器查看。\n\n崩溃时间:%s\n崩溃应用名称:%s(%s)\n日志保存位置:%s", NowTimeText.get(true), UtilityTools.getProgramNameByPackageName(FCGetWork.FCPackageName()), FCPackageName(), FCLogPath()), Toast.LENGTH_LONG).show();
					}
				});
		}
	}
	static String FCTime()
	{
		return FileGod.R("/sdcard/FClog/cache/FCTime");
	}
	static String FCPID()
	{
		return FileGod.R("/sdcard/FClog/cache/FCPID");
	}
	static String FCLogPath()
	{
		return "/sdcard/FClog/" + FCTime() + ".log";
	}
	static String FCLogView()
	{
		return FileGod.R("/sdcard/FClog/cache/" + FCTime() + "view");
	}
	static String FCLogBody()
	{
		return FileGod.R("/sdcard/FClog/" + FCTime() + ".log");
	}
	static String FCPackageName()
	{
		return FileGod.R("/sdcard/FClog/cache/FCPackage");
	}
	static String FCCrashTime()
	{
		return FileGod.R("/sdcard/FClog/cache/FCCrashTime");
	}
}
