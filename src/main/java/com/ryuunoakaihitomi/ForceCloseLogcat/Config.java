package com.ryuunoakaihitomi.ForceCloseLogcat;

public class Config
{
	static void S(String key1, String key2)
	{
		FileGod.W(key2, "/sdcard/FClog/config/" + key1);
	}
	static String G(String key1)
	{
		return FileGod.R("/sdcard/FClog/config/" + key1);
	}
}
