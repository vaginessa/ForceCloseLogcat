package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.os.Build;

public class RuntimeEnvironmentInformation
{
	public static String r()
	{
		String infobody="";
		infobody+="crash time="+FCGetWork.FCCrashTime()+"\n";
		infobody += "model=" + Build.MODEL + "\n";
		infobody += "android version="  + Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")\n";
		infobody += "brand=" + Build.BRAND + "\n";
		infobody += "manufacturer=" + Build.MANUFACTURER + "\n";
		infobody += "board=" + Build.BOARD + "\n";
		infobody += "hardware=" + Build.HARDWARE + "\n";
		infobody += "device=" + Build.DEVICE + "\n";
		if (UtilityTools.getAppVersionName(FCGetWork.FCPackageName()) != "")
		{
			infobody += "version name=" + UtilityTools.getAppVersionName(FCGetWork.FCPackageName()) + "\n";
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			infobody += "supported_abis=" +UtilityTools.stringArrayToString(Build.SUPPORTED_ABIS, " & ") + "\n";
		}
		else
		{
			infobody += "cpu_abi=" + Build.CPU_ABI + "\n";
			infobody += "cpu_abi2=" + Build.CPU_ABI2 + "\n";
		}
		infobody += "display=" + Build.DISPLAY;
		return infobody;
	}
}
