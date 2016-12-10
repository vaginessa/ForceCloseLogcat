package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.*;

public class FCNotification
{
	public static void stateOn(Context c)
	{
		NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, "应用崩溃监听服务启动", System.currentTimeMillis());
		Intent i = new Intent();
		PendingIntent p = PendingIntent.getActivity(c, 0, i, 0);
		n.setLatestEventInfo(c, "应用崩溃监听服务", "启动中......", p);
		n.flags = Notification.FLAG_NO_CLEAR;
		nm.notify(1, n);
	}
	public static void caught(Context c)
	{

		Intent intent = new Intent(c, LogReader.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		Notification n = new Notification(R.drawable.ic_launcher, "本应用已经捕获一个FC", System.currentTimeMillis());
		NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent p = PendingIntent.getActivity(c, 0, intent, 0);
		n.setLatestEventInfo(c, "时刻 " + NowTimeText.get(true) , "来自 " + getProgramNameByPackageName(overallSituationContext.get(), FCGetWork.FCPackageName()), p);
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(2, n);
	}

	public static String getProgramNameByPackageName(Context context,
													 String packageName)
	{
        PackageManager pm = context.getPackageManager();
        String name = null;
        try
		{
            name = pm.getApplicationLabel(
				pm.getApplicationInfo(packageName,
									  PackageManager.GET_META_DATA)).toString();
        }
		catch (NameNotFoundException e)
		{
            e.printStackTrace();
        }
        return name;
    }

}
