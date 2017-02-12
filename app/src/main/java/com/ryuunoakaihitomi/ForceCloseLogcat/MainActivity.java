package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

import java.lang.Process;

public class MainActivity extends Activity 
{
	boolean m;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Button clear=(Button) findViewById(R.id.mainButton4);
		Button help=(Button) findViewById(R.id.mainButton5);
		TextView version=(TextView) findViewById(R.id.mainTextView1);
		final Switch s=(Switch) findViewById(R.id.mainSwitch1);
		Boolean mode=Boolean.valueOf(Config.G(("mode")));
		s.setChecked(mode);
		ActivityCollector.addActivity(this);
		if (isRoot())
		{
			startService(new Intent(MainActivity.this, FCListener.class));
		}
		else
		{
			Toast.makeText(MainActivity.this, "root获取失败，本应用不具有可正常运行的基础", Toast.LENGTH_SHORT).show();
			finish();
		}

		if (mode)
		{
			FCNotification.stateOn(MainActivity.this);
		}
		s.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (s.isChecked())
					{
						Config.S("mode", "true");
						FCNotification.stateOn(MainActivity.this);
					}
					else
					{
						NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						nm.cancelAll();
						Config.S("mode", "false");
					}
				}
			});
		help.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i= new Intent(MainActivity.this, HelpView.class);
					startActivity(i);
				}
			});
		version.setText("版本号:" + getAppVersionName(this.getPackageName()));
		clear.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder tashikani=new AlertDialog.Builder(MainActivity.this);
					tashikani.setTitle("确认删除操作");
					tashikani.setMessage("确认删除所有日志文件？\n\n该操作不可恢复！");
					tashikani.setNegativeButton("否", null);
					tashikani.setPositiveButton("是", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								FileGod.D("/sdcard/FClog/");
								Toast.makeText(getApplicationContext(), "所有日志删除完成", Toast.LENGTH_SHORT).show();
								NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
								nm.cancelAll();
								ResetApp.r(MainActivity.this);
							}
						});
					tashikani.show();
				}
			});

    }

	public static String getAppVersionName(String PackageName)
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
	public static synchronized boolean isRoot()  
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
}
