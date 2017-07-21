package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UtilityTools
{
	static String pkg="com.ryuunoakaihitomi.ForceCloseLogcat";
	static Context c=overallSituationContext.get();
	static String getAppVersionName(String PackageName)
	{
        String versionName = "";
        try
		{
            PackageManager pm = overallSituationContext.get().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PackageName, 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0)
			{
                return null;
            }
        }
		catch (Exception e)
		{}
        return versionName;
    }
	static synchronized boolean isRoot()  
	{  
		Process process = null;  
		DataOutputStream os = null;  
		try  
		{  
			process = Runtime.getRuntime().exec("su");  
			os = new DataOutputStream(process.getOutputStream());  
			os.writeBytes("exit\n");  
			os.flush();  
			int exitValue = process.waitFor();  
			if (exitValue == 0)  
			{  
				return true;  
			}
			else  
			{  
				return false;  
			}  
		}
		catch (Exception e)  
		{  

			return false;  
		}
		finally  
		{  
			try  
			{  
				if (os != null)  
				{  
					os.close();  
				}  
				process.destroy();  
			}
			catch (Exception e)  
			{  
				e.printStackTrace();  
			}  
		}  
	}  
	static String stringArrayToString(String[] in, String dot)
	{
		String out = "";
		for (int i=0;i < in.length;i++)
		{
			if (i == in.length - 1)
			{
				out += in[i];
			}
			else
			{
				out += in[i] + dot;
			}
		}
		return out;
	}
	static boolean isForeground(String className)
	{
        if (c == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0)
		{
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
	static boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if (!f.exists())
            {
				return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
	static String getProgramNameByPackageName(String packageName)
	{
        PackageManager pm = c.getPackageManager();
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
	static String cmd(String command, boolean isRoot)
	{
		StringBuilder sb = new StringBuilder();
		String ret ;
		try
		{
			Process p ;
			if (isRoot)
			{
				p = Runtime.getRuntime().exec("su");
			}
			else
			{
				p = Runtime.getRuntime().exec("sh");
			}
			OutputStreamWriter osw = new OutputStreamWriter(p.getOutputStream(), "UTF-8");
			osw.write(command + "\n");
			osw.write("exit\n");
			osw.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String l = "";
			while ((l = br.readLine()) != null)
			{
				sb.append(l);
			}
			ret =sb.toString();
			p.getErrorStream().close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	static void vibrate()
	{
		Vibrator v=(Vibrator) c.getSystemService(overallSituationContext.get().VIBRATOR_SERVICE);
		v.vibrate(100);
	}
	public static String Inputstr2Str_Reader(InputStream in, String encode)  
	{  
		String str = "";  
		try  
		{  
			if (encode == null || encode.equals(""))  
			{  
				encode = "utf-8";  
			}  
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));  
			StringBuffer sb = new StringBuffer();  

			while ((str = reader.readLine()) != null)  
			{  
				sb.append(str).append("\n");  
			}  
			return sb.toString();  
		}  
		catch (UnsupportedEncodingException e1)  
		{  
			e1.printStackTrace();  
		}  
		catch (IOException e)  
		{  
			e.printStackTrace();  
		}  
		return str;  
	}  
}
