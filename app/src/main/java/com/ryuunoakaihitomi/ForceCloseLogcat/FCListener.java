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
		FileGod.D("/sdcard/FClog/cache");
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
						String getLog=new String((line + "\r\n").getBytes("iso-8859-1"), "UTF-8");
						String getView=new String((line + "<br><br>").getBytes("iso-8859-1"), "UTF-8");
						String getTime="";
						String getPackage="";
						while ((line = dis.readLine()).contains("AndroidRuntime"))
						{
							if (line.contains("Process:"))
							{
								getPackage = line.subSequence(58, line.indexOf(",")).toString();
								if (getPackage.contains(":"))
								{
									getPackage = getPackage.subSequence(0, getPackage.indexOf(":")).toString();
								}
							}
							getView += new String(LogDecorate.line(line.subSequence(48, line.length()) + "<br><br>").getBytes("iso-8859-1"), "UTF-8");
							getLog += new String((line.subSequence(48, line.length()) + "\r\n").getBytes("iso-8859-1"), "UTF-8");
							Thread.yield();
						}
						getTime = NowTimeText.get(false);
						FileGod.W(getLog, "/sdcard/FClog/" + getTime + ".log");
						if (Boolean.valueOf(Config.G("mode")))
						{
							FileGod.W(getTime, "/sdcard/FClog/cache/FCtime");
							FileGod.W(getPackage, "/sdcard/FClog/cache/FCPackage");
							FileGod.W(getView, "/sdcard/FClog/cache/" + getTime + "view");
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
