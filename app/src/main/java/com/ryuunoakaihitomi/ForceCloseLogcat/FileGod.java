package com.ryuunoakaihitomi.ForceCloseLogcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

public class FileGod
{
	static void D(String path)
	{
		delete(new File(path));
	}
	static void W(String write_str, String fileName)
	{  
		if (fileName.contains("/sdcard/FClog"))
		{
			Wk("这个是应用崩溃日志记录器("+UtilityTools.pkg+")创建并使用的目录，如果不再使用利用本应用及相关数据可以删除","/sdcard/FClog/说明.txt");
		}
		Wk(write_str,fileName);
	}
	static void Wk(String write_str, String fileName)
	{  
        try
		{
			File file = new File(fileName);  
			File dir=new File(fileName.subSequence(0, fileName.lastIndexOf("/")).toString());
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);  
			byte [] bytes = write_str.getBytes(); 
			fos.write(bytes); 
			fos.close();
		}
		catch (IOException e)
		{} 
	} 
	static String R(String fileName)
	{  
		String res="";
        try
		{
			File file = new File(fileName);  
			FileInputStream fis = new FileInputStream(file);  
			int length = fis.available(); 
			byte [] buffer = new byte[length]; 
			fis.read(buffer);     
			res = EncodingUtils.getString(buffer, "UTF-8"); 
			fis.close();     
		}
		catch (IOException e)
		{}  
		return res;
	}  
	private static void delete(File f)
	{   
		if (f.isFile())
		{  
			f.delete();  
			return;  
		}  
		if (f.isDirectory())
		{  
			File[] childFiles = f.listFiles();  
			if (childFiles == null || childFiles.length == 0)
			{  
				f.delete();  
				return;  
			}  
			for (int i = 0; i < childFiles.length; i++)
			{  
				delete(childFiles[i]);  
			}  
			f.delete();  
		}  
	} 
}
