package com.ryuunoakaihitomi.ForceCloseLogcat;

import java.text.*;
import java.util.*;

public class NowTimeText
{
	public static String get(boolean mode)
	{
		SimpleDateFormat spf;
		if (mode)
		{
			spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		else
		{
			spf = new SimpleDateFormat("yyyyMMddHHmmss");
		}
		Date d= new Date(System.currentTimeMillis());
		if (mode)
		{
			return spf.format(d);
		}
		else
		{
			return numberTo36type(Long.valueOf(spf.format(d)));
		}
	}

	private static String numberTo36type(Long n)
	{
		char[] ch = new char[20];
		int nIndex = 0;
		while (true)
		{
			Long m = n / 36;
			Long k = n % 36;
			if (k == 35)
				ch[nIndex] = 'Z';
			else if (k == 34)
				ch[nIndex] = 'Y';
			else if (k == 33)
				ch[nIndex] = 'X';
			else if (k == 32)
				ch[nIndex] = 'W';
			else if (k == 31)
				ch[nIndex] = 'V';
			else if (k == 30)
				ch[nIndex] = 'U';
			else if (k == 29)
				ch[nIndex] = 'T';
			else if (k == 28)
				ch[nIndex] = 'S';
			else if (k == 27)
				ch[nIndex] = 'R';
			else if (k == 26)
				ch[nIndex] = 'Q';
			else if (k == 25)
				ch[nIndex] = 'P';
			else if (k == 24)
				ch[nIndex] = 'O';
			else if (k == 23)
				ch[nIndex] = 'N';
			else if (k == 22)
				ch[nIndex] = 'M';
			else if (k == 21)
				ch[nIndex] = 'L';
			else if (k == 20)
				ch[nIndex] = 'K';
			else if (k == 19)
				ch[nIndex] = 'J';
			else if (k == 18)
				ch[nIndex] = 'I';
			else if (k == 17)
				ch[nIndex] = 'H';
			else if (k == 16)
				ch[nIndex] = 'G';
			else if (k == 15)
				ch[nIndex] = 'F';
			else if (k == 14)
				ch[nIndex] = 'E';
			else if (k == 13)
				ch[nIndex] = 'D';
			else if (k == 12)
				ch[nIndex] = 'C';
			else if (k == 11)
				ch[nIndex] = 'B';
			else if (k == 10)
				ch[nIndex] = 'A';
			else 
				ch[nIndex] = (char)('0' + k);
			nIndex++;
			if (m == 0)
				break;
			n = m;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(ch, 0, nIndex);
		sb.reverse();
		return sb.toString();
	}

}
