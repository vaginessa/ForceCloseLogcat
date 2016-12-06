package com.ryuunoakaihitomi.ForceCloseLogcat;

import android.app.*;
import java.util.*;

public class ActivityCollector
{
    public static List<Activity> as = new ArrayList<Activity>();
    public static void addActivity(Activity activity)
	{
		as.add(activity);
    }
    public static void removeActivity(Activity activity)
	{
		as.remove(activity);
    }
    public static void finishAll()
	{
		for (Activity activity : as)
		{
            if (!activity.isFinishing())
			{
                activity.finish();
            }
        }
    }
}

