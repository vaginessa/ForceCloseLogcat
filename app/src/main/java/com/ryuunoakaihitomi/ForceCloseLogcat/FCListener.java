package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.ryuunoakaihitomi.ForceCloseLogcat.FCGetWork;
import com.ryuunoakaihitomi.ForceCloseLogcat.FileGod;
import com.ryuunoakaihitomi.ForceCloseLogcat.UtilityTools;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class FCListener extends Service implements Runnable
{
	boolean RunState = false;
	static boolean isRoot=false;
	final String logcmd="logcat -v tag\n";
	Thread t;
	int offset_j=18;
	int offset_n=12;
	String whitelist;
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		RunState = true;
		new File("/sdcard/FClog").mkdir();
		new File("/sdcard/FClog/cache").mkdir();
		new File("/sdcard/FClog/config").mkdir();
		isRoot = UtilityTools.isRoot();
		startForeground(1, FCNotification.stateOn());
		QuickOperationBroadcast.regAll();
		t = new Thread(this);
		UtilityTools.cmd("logcat -c", isRoot);
		FileGod.D("/sdcard/FClog/cache");
		t.start();
	}
	@Override
	public void onDestroy()
	{
		QuickOperationBroadcast.unregAll();
		RunState = false;
		stopForeground(1);
		super.onDestroy();
	}
	@Override
	public void run()
	{
		Process p=null;
		DataOutputStream o=null;
		String line;
		try
		{
			if (isRoot)
			{
				p = Runtime.getRuntime().exec("su");
				o = new DataOutputStream(p.getOutputStream());
				o.writeBytes(logcmd);
				o.flush();
			}
			else
			{
				p = Runtime.getRuntime().exec(logcmd);
			}
		}
		catch (IOException e)
		{
		}
		DataInputStream dis = new DataInputStream(p.getInputStream());
		while (RunState)
		{
			try
			{
				while ((line = dis.readLine()) != null)
				{
					if (judgement(line))
					{
						String getCrashTime=NowTimeText.get(true);
						String getPackage = "";
						String	 getPID = "";
						String getTime="";
						String getLog="";
						String getView="";
						getTime = NowTimeText.get(false);
						String p1="/sdcard/FClog/" + getTime + ".log";
						String p21="/sdcard/FClog/cache/mode";
						boolean b=FileGod.R(p21).equals("jvm");
						if (b)
						{
							getLog = new String((line.substring(offset_j) + "\n").getBytes("iso-8859-1"), "UTF-8");
							getView = new String((LogDecorator.line(line.substring(offset_j)) + "<br><br>").getBytes("iso-8859-1"), "UTF-8");
							while ((line = dis.readLine()).contains("E/AndroidRuntime:"))
							{
								if (line.contains("Process: "))
								{
									getPackage = line.subSequence(9 + offset_j, line.indexOf(", PID:")).toString();
									if (getPackage.contains(":"))
									{
										getPackage = getPackage.split(":")[0];
									}
									getPID = line.subSequence(line.indexOf(", PID:") + 7, line.length()).toString();
								}
								getLog += new String((line.substring(offset_j) + "\n").getBytes("iso-8859-1"), "UTF-8");
								getView += new String(LogDecorator.line((line.substring(offset_j)) + "<br><br>").getBytes("iso-8859-1"), "UTF-8");
							}
						}
						else
						{
							while ((line = dis.readLine()).contains("F/DEBUG   :"))
							{
								if (!line.equals("F/DEBUG   : *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***"))
								{
									getLog += new String((line.substring(offset_n) + "\n").getBytes("iso-8859-1"), "UTF-8");
									getView += new String((LogDecorator.line(line.substring(offset_n)) + "<br><br>").getBytes("iso-8859-1"), "UTF-8");
								}
								if (line.contains(">>> ") && line.contains(" <<<"))
								{
									getPackage = line.subSequence(line.indexOf(">>> ") + 4, line.indexOf(" <<<")).toString();
									getPID = line.subSequence(line.indexOf(": pid: ") + ": pid: ".length(), line.indexOf(", tid:")).toString();
								}
							}
						}
						if (whitelist.contains(getPackage) || UtilityTools.getProgramNameByPackageName(getPackage).equals(null))
						{
							continue;
						}
						FileGod.W(getLog, p1);
						if (Config.G("quiet").equals("1"))
						{
							FileGod.W(getCrashTime, "/sdcard/FClog/cache/FCCrashTime");
							FileGod.W(getTime, "/sdcard/FClog/cache/FCTime");
							FileGod.W(getPackage, "/sdcard/FClog/cache/FCPackage");
							FileGod.W(getPID, "/sdcard/FClog/cache/FCPID");
							FileGod.W(getView.toString(), "/sdcard/FClog/cache/" + getTime + "view");
							FCGetWork.FCReceive();
						}
					}
				}
				exitWork(dis, p);
			}
			catch (Exception e)
			{}
		}
		exitWork(dis, p);
	}
	private void exitWork(DataInputStream b1, Process b2)
	{
		UtilityTools.cmd("logcat -c", isRoot);
		try
		{
			b1.close();
		}
		catch (IOException e)
		{}
		b2.destroy();
		stopSelf();
	}
	boolean judgement(String l)
	{
		whitelist = Config.G("whitelist");
		if (Config.G("quiet").equals("0"))
		{
			stopForeground(true);
		}
		else
		{
			startForeground(1, FCNotification.stateOn());
		}
		if (l.contains("FATAL EXCEPTION"))
		{
			if (l.contains("D/OkHttp"))
			{
				return false;
			}
			else
			{
				FileGod.W("jvm", "/sdcard/FClog/cache/mode");
				return true;
			}
		}
		if (l.equals("F/DEBUG   : *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***"))
		{
			FileGod.W("ndk", "/sdcard/FClog/cache/mode");
			return true;
		}
		return false;
	}
}
