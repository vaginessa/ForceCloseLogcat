package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.net.Uri;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Button clear=(Button) findViewById(R.id.mainButton4);
		Button help=(Button) findViewById(R.id.mainButton5);
		Button seoff=(Button) findViewById(R.id.mainButton1);
		Button wl=(Button) findViewById(R.id.mainButton2);
		TextView version=(TextView) findViewById(R.id.mainTextView1);
		Switch s=(Switch) findViewById(R.id.mainSwitch1);
		final Switch quiet=(Switch) findViewById(R.id.mainSwitch2);
		if (!UtilityTools.isRoot())
		{
			Toast.makeText(MainActivity.this, "root获取失败，仅可以使用adb调用模式", Toast.LENGTH_SHORT).show();
			seoff.setVisibility(View.GONE);
		}
		if(!UtilityTools.fileIsExists("/sdcard/FClog/config/quiet"))
		{
			Config.S("quiet","1");
		}
		quiet.setChecked(Config.G("quiet").equals("1"));
		startService(new Intent(overallSituationContext.get(), FCListener.class));
		s.setChecked(true);
		s.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					{
						stopService(new Intent(overallSituationContext.get(), FCListener.class));
						NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						nm.cancelAll();
						new Timer().schedule(new TimerTask(){
							public void run()
							{
								Process.killProcess(Process.myPid());
							}
						},1000);
						finish();
					}
				}
			});
		quiet.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(quiet.isChecked())
						Config.S("quiet","1");
					else
						Config.S("quiet","0");
				}
			});
		help.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					LayoutInflater li=LayoutInflater.from(MainActivity.this);
					View v=li.inflate(R.layout.help, null);
					WebView wv=(WebView) v.findViewById(R.id.helpWebView1);
					try
					{
						wv.loadData(UtilityTools.Inputstr2Str_Reader(getResources().getAssets().open("h.htm"),"UTF-8"), "text/html", "UTF-8");
					}
					catch (IOException e)
					{}
					AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
					ab.setTitle("帮助")
					.setView(v)
						.setPositiveButton("复制adb命令", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
								cm.setText("adb shell pm grant com.ryuunoakaihitomi.ForceCloseLogcat android.permission.READ_LOGS");
								Toast.makeText(getApplicationContext(), "已复制", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("开源地址", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/ryuunoakaihitomi/ForceCloseLogcat")));
							}
						})
						.setNeutralButton("Crash测试", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								int i=0/0;
							}
						});
					ab.create().show();
				}
			});
		version.setText("版本号:" + UtilityTools. getAppVersionName(this.getPackageName()));
		seoff.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					UtilityTools.cmd("setenforce 0", true);
					Toast.makeText(overallSituationContext.get(), "selinux已关闭", Toast.LENGTH_SHORT).show();
				}
			});
		seoff.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					UtilityTools.cmd("setenforce 1", true);
					Toast.makeText(overallSituationContext.get(), "selinux已打开", Toast.LENGTH_SHORT).show();
					return true;
				}
			});
		wl.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					LayoutInflater li=LayoutInflater.from(MainActivity.this);
					View v=li.inflate(R.layout.whitelist, null);
					final EditText et=(EditText) v.findViewById(R.id.whitelistEditText1);
					Button clean=(Button) v.findViewById(R.id.whitelistButton1);
					Button save=(Button) v.findViewById(R.id.whitelistButton2);
					et.setText(Config.G("whitelist"));
					et.requestFocus();
					final AlertDialog.Builder ab=new AlertDialog.Builder(MainActivity.this);
					ab.setTitle("白名单编辑器");
					ab.setView(v);
					final AlertDialog a=ab.create();
					clean.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								et.setText("");
							}
						});
					save.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								Config.S("whitelist", et.getText().toString());
								Toast.makeText(overallSituationContext.get(), "白名单已保存", Toast.LENGTH_SHORT).show();
								a.dismiss();
							}
						});
					a.show();
				}
			});
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
								FileGod.D("/sdcard/FClog");
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
}
