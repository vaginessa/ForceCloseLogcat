package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.widget.*;
import java.util.*;

public class FCGetWork
{
	public static void FCReceive()
	{
		vibrate();
		if (!isForeground(overallSituationContext.get(), "com.ryuunoakaihitomi.ForceCloseLogcat.LogReader"))
		{
			NotificationManager nm= (NotificationManager) overallSituationContext.get().getSystemService(overallSituationContext.get().NOTIFICATION_SERVICE);
			nm.cancel(2);
			FCNotification.caught(overallSituationContext.get());
		}
		else
		{
			new Handler(Looper.getMainLooper()).post(new Runnable() 
				{
					@Override
					public void run()
					{
						Toast.makeText(overallSituationContext.get(), String.format("检测到你正在查看日志，所以最新的日志自动保存但不弹出通知栏通知。可自行打开文件管理器查看。\n\n崩溃时间:%s\n崩溃应用名称:%s(%s)\n日志保存位置:%s", NowTimeText.get(true), FCNotification.getProgramNameByPackageName(overallSituationContext.get(), FCPackageName()), FCPackageName(), FCLogPath()), Toast.LENGTH_LONG).show();
					}
				});
		}
	}
	public static String FCTime()
	{
		return FileGod.R("/sdcard/FClog/cache/FCTime");
	}
	public static String FCLogPath()
	{
		return "/sdcard/FClog/" + FCTime() + ".log";
	}
	public static String FCLogView()
	{
		return FileGod.R("/sdcard/FClog/cache/" + FCTime() + "view");
	}
	public static String FCLogBody()
	{
		return FileGod.R("/sdcard/FClog/" + FCTime() + ".log");
	}
	public static String FCPackageName()
	{
		return FileGod.R("/sdcard/FClog/cache/FCPackage");
	}
	private static void vibrate()
	{
		Vibrator v=(Vibrator) overallSituationContext.get().getSystemService(overallSituationContext.get().VIBRATOR_SERVICE);
		v.vibrate(100);
	}
	public static boolean isForeground(Context context, String className)
	{
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0)
		{
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
