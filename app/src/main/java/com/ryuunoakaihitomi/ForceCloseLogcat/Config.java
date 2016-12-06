package com.ryuunoakaihitomi.ForceCloseLogcat;

public class Config
{
	static String configDir="/sdcard/FClog/config/";
	public static void S(String key1, String key2)
	{
		FileGod.W(key2, configDir  + key1);
	}
	public static String G(String key1)
	{
		return FileGod.R(configDir + key1);
	}
}
