package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.*;
import android.os.*;

public class FCGetWork
{
	public static void FCReceive()
	{
		NotificationManager nm= (NotificationManager) overallSituationContext.get().getSystemService(overallSituationContext.get().NOTIFICATION_SERVICE);
		nm.cancel(2);
		FCNotification.caught(overallSituationContext.get());
		vibrate();
	}

	public static String FCTime()
	{
		return FileGod.R("/sdcard/FClog/cache/FCTime.dat");
	}
	public static String FCLogPath()
	{
		return "/sdcard/FClog/" + FCTime() + ".log";
	}
	public static String FCLogView()
	{
		return FileGod.R("/sdcard/FClog/cache/" + FCTime() + "view.dat");
	}
	public static String FCLogBody()
	{
		return FileGod.R("/sdcard/FClog/" + FCTime() + ".log");
	}
	public static String FCPosition()
	{
		String s= FileGod.R("/sdcard/FClog/cache/where.dat");
		return s.substring(s.indexOf("Force") + 25 , s.length());
	}
	public static String FCPackageName()
	{
		String str=FCPosition();
		return str.subSequence(0, str.indexOf("/")).toString();
	}
	private static void vibrate()
	{
		Vibrator v=(Vibrator) overallSituationContext.get().getSystemService(overallSituationContext.get().VIBRATOR_SERVICE);
		v.vibrate(100);
	}
}
