package com.ryuunoakaihitomi.ForceCloseLogcat;
import android.os.*;

public class RuntimeEnvironmentInformation
{
	public static String _()
	{
		String infobody="";
		infobody += "send time=" + NowTimeText.get(true) + "\n";
		infobody += "model=" + android.os.Build.MODEL + "\n";
		infobody += "android version="  + android.os.Build.VERSION.RELEASE + "\n";
		infobody += "android api level="  + android.os.Build.VERSION.SDK_INT + "\n";
		infobody += "brand=" + android.os.Build.BRAND + "\n";
		infobody += "manufacturer=" + android.os.Build.MANUFACTURER + "\n";
		infobody += "board=" + android.os.Build.BOARD + "\n";
		infobody += "hardware=" + android.os.Build.HARDWARE + "\n";
		infobody += "device=" + android.os.Build.DEVICE + "\n";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			infobody += "support_abis=" + stringArrayToString(android.os.Build.SUPPORTED_ABIS, " & ") + "\n";
		}
		else
		{
			infobody += "cpu_abi=" + android.os.Build.CPU_ABI + "\n";
			infobody += "cpu_abi2=" + android.os.Build.CPU_ABI2 + "\n";
		}
		infobody += "display=" + android.os.Build.DISPLAY;
		return infobody;
	}
	private  static String stringArrayToString(String[] in, String dot)
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
}
