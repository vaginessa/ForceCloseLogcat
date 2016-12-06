package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class HelpView extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
		ProgressBar egg=(ProgressBar) findViewById(R.id.helpProgressBar1);
		egg.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					Toast.makeText(getApplicationContext(), "クルクルと回って。。。", Toast.LENGTH_LONG).show();
					Intent i = new Intent();
					i.setClassName("com.android.settings", "com.android.settings.TestingSettings");
					startActivity(i);
					return false;
				}
			});
	}
}
