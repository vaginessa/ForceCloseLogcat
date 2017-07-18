package com.ryuunoakaihitomi.ForceCloseLogcat;
public class LogDecorator
{
	static String kazari(String s)
	{
		return s
			.replace(FCGetWork.FCPackageName(), "<font color =\"#0066FF\">" + FCGetWork.FCPackageName() + "</font>")
			.replace("FATAL EXCEPTION:", "<font color =\"#FF0000\">FATAL EXCEPTION:</font>")
			.replace("Caused by:", "<font color =\"#FF0000\">Caused by:</font>")
			.replace("Process:", "<font color =\"#FF0000\">Process:</font>")
			.replace("backtrace:","<font color =\"#29AF00\"><i>backtrace:</i></font>");
	}
	static String line(String s)
	{
		if (s.contains("java.lang") | s.contains("Caused by:"))
		{ 
			if (s.contains("Exception") | s.contains("Error"))
			{
				return "<b>" + s + "</b>";
			}
		}
		if (s.contains("#"))
		{
			return "<b>" + s + "</b>";
		}
		return s;
	}
}
