package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class QuickOperationBroadcast
{
	static Context c=overallSituationContext.get();
	static BroadcastReceiver delete=new BroadcastReceiver(){
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			FileGod.D("/sdcard/FClog/" + FCGetWork.FCTime() + ".log");
			FileGod.D("/sdcard/FClog/cache");
			Toast.makeText(p1, "日志文件已删除", Toast.LENGTH_SHORT).show();
			NotificationManager nm= (NotificationManager) p1.getSystemService(p1.NOTIFICATION_SERVICE);
			nm.cancel("ForceCloseLogcat", 2);
		}
	};
	static BroadcastReceiver copy=new BroadcastReceiver(){
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			ClipboardManager cm = (ClipboardManager) p1.getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(log());
			Toast.makeText(p1, "已复制", Toast.LENGTH_SHORT).show();
		}
	};
	static BroadcastReceiver slide=new BroadcastReceiver(){
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			Toast.makeText(c, String.format("检测到你划掉了通知，所以最新的日志自动保存。可自行打开文件管理器查看。\n\n崩溃时间:%s\n崩溃应用名称:%s(%s)\n日志保存位置:%s", NowTimeText.get(true),UtilityTools.getProgramNameByPackageName(FCGetWork.FCPackageName()) , FCGetWork. FCPackageName(), FCGetWork. FCLogPath()), Toast.LENGTH_LONG).show();
			FileGod.D("/sdcard/FClog/cache");
		}
	};
	static BroadcastReceiver share=new BroadcastReceiver(){
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			Intent i=new Intent(Intent.ACTION_SEND)
				.setType("text/plain")
				.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃日志：")
				.putExtra(Intent.EXTRA_TEXT, log())
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			c.startActivity(Intent.createChooser(i, "应用崩溃日志记录器：将日志内容发送给开发者"));  
		}
	};

	static void regAll()
	{
		reg(delete, UtilityTools.pkg+".delete");
		reg(copy, UtilityTools.pkg+".copy");
		reg(slide, UtilityTools.pkg+".slide");
		reg(share,UtilityTools.pkg+".share");
	}
	static void unregAll()
	{
		unreg(delete);
		unreg(copy);
		unreg(slide);
		unreg(share);
	}
	private static void reg(BroadcastReceiver b, String action)
	{
		IntentFilter itfl=new IntentFilter();
		itfl.addAction(action);
		c.registerReceiver(b, itfl);
	}
	private static void unreg(BroadcastReceiver b)
	{
		c.unregisterReceiver(b);
	}
	private static String log()
	{
		return new StringBuilder()
			.append("#######RuntimeEnvironmentInformation#######\n")
			.append(RuntimeEnvironmentInformation.r())
			.append("\n#######ForceCloseCrashLog#######\n")
			.append(FCGetWork.FCLogBody())
			.toString();
	}
}
