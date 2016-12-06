package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.content.*;

public class ResetApp
{
	public static void _(Context c)
	{
		Intent i = c.getPackageManager().getLaunchIntentForPackage(c.getPackageName());  
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		c.startActivity(i);
	}
}
