package com.ryuunoakaihitomi.ForceCloseLogcat;

import java.io.*;
import org.apache.http.util.*;

public class FileGod
{
	public static void D(String path)
	{
		delete(new File(path));
	}
	public static void W(String write_str, String fileName)
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
	public static String R(String fileName)
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
