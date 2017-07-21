package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.content.Context;
import android.content.Intent;

public class ResetApp
{
	public static void r(Context c)
	{
		Intent i = c.getPackageManager().getLaunchIntentForPackage(c.getPackageName());  
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		c.startActivity(i);
	}
}
