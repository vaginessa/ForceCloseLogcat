package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.*;
import android.content.*;
import android.os.*;
import java.io.*;

import java.lang.Process;

public class FCListener extends Service implements Runnable
{
	boolean RunState = false;
	Thread t;
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		RunState = true;
		t = new Thread(this);
		try
		{
			java.lang.Runtime.getRuntime().exec("su -c logcat -c");
		}
		catch (IOException e)
		{}
		t.start();

	}
	@Override
	public void onDestroy()
	{
		RunState = false;
		super.onDestroy();
	}
	@Override
	public void run()
	{
		Process p=null;
		DataOutputStream o=null;
		try
		{
			p = Runtime.getRuntime().exec("su");
			o = new DataOutputStream(p.getOutputStream());
			o.writeBytes("logcat\n");
			o.flush();
		}
		catch (IOException e)
		{
		}
		DataInputStream dis = new DataInputStream(p.getInputStream());
		String line = null;
		while (RunState)
		{
			try
			{
				while ((line = dis.readLine()) != null)
				{
					if (line.contains("FATAL EXCEPTION"))
					{
						String getLog=line + "\r\n";
						String getView=line + "<br><br>";
						String getTime="";
						String getCrashPos="";
						while (!(line = dis.readLine()).contains("Force finishing"))
						{
							getView += LogDecorate.line(line.subSequence(48, line.length()) + "<br><br>");
							getLog += line.subSequence(48, line.length()) + "\r\n";
							Thread.yield();
						}
						getCrashPos = line;
						getTime = NowTimeText.get(false);
						FileGod.W(getLog, "/sdcard/FClog/" + getTime + ".log");
						if (Boolean.valueOf(Config.G("mode")))
						{
							FileGod.W(getTime, "/sdcard/FClog/cache/FCtime.dat");
							FileGod.W(getCrashPos, "/sdcard/FClog/cache/where.dat");
							FileGod.W(getView, "/sdcard/FClog/cache/" + getTime + "view.dat");
							FCGetWork.FCReceive();
						}
						java.lang.Runtime.getRuntime().exec("su -c logcat -c");
					}
					Thread.yield();
				}
				exitWork(dis, p);
			}
			catch (Exception e)
			{
			}
		}
		exitWork(dis, p);
	}
	private void exitWork(DataInputStream bridge1, Process bridge2)
	{
		try
		{
			java.lang.Runtime.getRuntime().exec("su -c logcat -c");
			bridge1.close();
		}
		catch (IOException e)
		{
		}
		bridge2.destroy();
		stopSelf();
	}
}
