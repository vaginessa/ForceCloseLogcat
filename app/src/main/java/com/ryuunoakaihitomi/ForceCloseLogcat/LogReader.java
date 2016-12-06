package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

import android.content.ClipboardManager;

public class LogReader extends Activity
{
	String filePath ;
	String logBody;
	String	logView;
	CheckBox filesave;
	Boolean s=false;
	@Override
	protected void onDestroy()
	{
		FileGod.D("/sdcard/FClog/cache");
		super.onDestroy();
	}

	@Override
	protected void onStop()
	{

		if (!s)
		{
			Toast.makeText(LogReader.this, "日志已保存", Toast.LENGTH_SHORT).show();
			ActivityCollector.finishAll();
		}
		super.onStop();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{

			Toast.makeText(LogReader.this, "已退出", Toast.LENGTH_SHORT).show();
			s = true;
			if (!filesave.isChecked())
			{
				FileGod.D(filePath);
				Toast.makeText(LogReader.this, "日志文件已删除", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(LogReader.this, "日志文件已保留至 " + filePath, Toast.LENGTH_SHORT).show();
			}
			ActivityCollector.finishAll();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader);
		Button copy=(Button) findViewById(R.id.readerButton1);
		Button send=(Button) findViewById(R.id.readerButton2);
		EditText read=(EditText) findViewById(R.id.readerEditText1);
		final CheckBox environment=(CheckBox) findViewById(R.id.readerCheckBox1);
		filesave = (CheckBox) findViewById(R.id.readerCheckBox2);
		filePath = "/sdcard/FClog/" + FCGetWork.FCTime() + ".log";
		ActivityCollector.addActivity(this);
		logView = FCGetWork.FCLogView();
		logBody = FCGetWork.FCLogBody();
		if (fileIsExists(filePath))
		{
			environment.setChecked(true);
			read.setText(Html.fromHtml(LogDecorate.kazari(logView)));
		}
		else
		{
			read.setText(Html.fromHtml("<font color=\"#FF0000\">读取失败！找不到文件！</font>"));
			read.setHorizontalScrollBarEnabled(false);
			filesave.setEnabled(false);
			copy.setEnabled(false);
			send.setEnabled(false);
			environment.setEnabled(false);
		}
		filesave.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					AlertDialog.Builder ab=new AlertDialog.Builder(LogReader.this);
					ab.setTitle("Log文件保存位置:");
					ab.setMessage(filePath);
					ab.setPositiveButton("复制进剪贴板", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
								cm.setText(filePath);
								Toast.makeText(getApplicationContext(), "已复制Log文件保存位置", Toast.LENGTH_SHORT).show();
							}
						});
					ab.show();
					return false;
				}
			});
		environment.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					AlertDialog.Builder tashikani=new AlertDialog.Builder(LogReader.this);
					tashikani.setTitle("运行时刻环境信息显示:");
					tashikani.setMessage(RuntimeEnvironmentInformation._());
					tashikani.setPositiveButton("复制进剪贴板", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
								cm.setText(RuntimeEnvironmentInformation._());
								Toast.makeText(getApplicationContext(), "已复制运行时刻环境信息", Toast.LENGTH_SHORT).show();

							}
						});
					tashikani.show();
					return false;
				}
			});
		copy.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String copyout=infoAdd(environment.isChecked()) + logBody;
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setText(copyout);
					Toast.makeText(getApplicationContext(), "已复制，可以粘贴给开发者了", Toast.LENGTH_SHORT).show();
				}
			});
		send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String sendout=infoAdd(environment.isChecked()) + logBody;
					Intent i=new Intent(Intent.ACTION_SEND);  
					i.setType("text/plain");  
					if (environment.isChecked())
					{
						i.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃日志：");  
					}
					else
					{
						i.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃日志：来自" + FCGetWork.FCPosition());
					}
					i.putExtra(Intent.EXTRA_TEXT, sendout);  
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(Intent.createChooser(i, getTitle() + "：将日志内容发送给开发者"));  
				}
			});
	}

	private static boolean fileIsExists(String strFile)
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


	private String infoAdd(boolean b)
	{
		String out = "";
		if (b)
		{
			out = "#######RuntimeEnvironmentInformation#######\n" + RuntimeEnvironmentInformation._() + "\n#######ForceCloseCrashLog#######\n";
		}
		out += logBody;
		return out;
	}


}
