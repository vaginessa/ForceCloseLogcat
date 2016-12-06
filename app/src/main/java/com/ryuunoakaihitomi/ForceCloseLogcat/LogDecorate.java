package com.ryuunoakaihitomi.ForceCloseLogcat;
public class LogDecorate
{
	public static String kazari(String s)
	{
		return s
			.replace(FCGetWork.FCPackageName(), "\n<font color =\"#0066FF\">" + FCGetWork.FCPackageName() + "</font>\n")
			.replace("FATAL EXCEPTION:", "\n<font color =\"#FF0000\">FATAL EXCEPTION:</font>\n")
			.replace("Caused by:", "\n<font color =\"#FF0000\">Caused by:</font>\n")
			.replace("Process:", "\n<font color =\"#FF0000\">Process:</font>\n");
	}
	public static String line(String s)
	{
		if (s.contains("java.lang") | s.contains("Caused by:"))
		{ 
			if (s.contains("Exception") | s.contains("Error"))
			{
				return "<b>" + s + "</b>";
			}
			else
			{
				return s;
			}
		}
		else
		{
			return s;
		}
	}
}
