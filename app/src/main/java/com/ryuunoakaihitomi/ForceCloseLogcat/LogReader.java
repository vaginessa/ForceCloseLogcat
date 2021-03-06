package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebView;

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
			FileGod.D("/sdcard/FClog/cache");
			Toast.makeText(LogReader.this, "日志已保存", Toast.LENGTH_SHORT).show();
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
		filePath ="/sdcard/FClog/"+ FCGetWork.FCTime() + ".log";
		logView = FCGetWork.FCLogView();
		logBody = FCGetWork.FCLogBody();
		if (UtilityTools.fileIsExists(filePath))
		{
			environment.setChecked(true);
				read.setText(Html.fromHtml(LogDecorator.kazari(logView)));
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
					tashikani.setMessage(RuntimeEnvironmentInformation.r());
					tashikani.setPositiveButton("复制进剪贴板", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
								cm.setText(RuntimeEnvironmentInformation.r());
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
					String copyout=infoAdd(environment.isChecked());
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setText(copyout);
					Toast.makeText(getApplicationContext(), "已复制，可以粘贴给开发者了", Toast.LENGTH_SHORT).show();
				}
			});
		send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String sendout=infoAdd(environment.isChecked());
					Intent i=new Intent(Intent.ACTION_SEND);  
					i.setType("text/plain");  
					if (environment.isChecked())
					{
						i.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃日志：");  
					}
					else
					{
						i.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃日志：来自" + FCGetWork.FCPackageName());
					}
					i.putExtra(Intent.EXTRA_TEXT, sendout);  
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(Intent.createChooser(i, getTitle() + "：将日志内容发送给开发者"));  
				}
			});
	}
	private String infoAdd(boolean b)
	{
		String out = "";
		if (b)
		{
			out = "#######RuntimeEnvironmentInformation#######\n" + RuntimeEnvironmentInformation.r() + "\n#######ForceCloseCrashLog#######\n";
		}
		out += logBody;
		return out;
	}
}
