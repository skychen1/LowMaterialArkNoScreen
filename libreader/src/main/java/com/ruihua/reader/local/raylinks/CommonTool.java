package com.ruihua.reader.local.raylinks;

public class CommonTool {
	
	public static String bytesToHexString(byte[] src, int len){
	    StringBuilder stringBuilder = new StringBuilder("");
	    if (src == null || src.length <= 0 || src.length < len) {  
	        return null;  
	    }  
	    for (int i = 0; i < len; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }
	    
	    return stringBuilder.toString();  
	}
	
	public static String byteToHexString(byte src){
	    StringBuilder stringBuilder = new StringBuilder("");

        int v = src & 0xFF;  
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {  
            stringBuilder.append(0);  
        }  
        stringBuilder.append(hv);  
	    
	    return stringBuilder.toString();  
	}
	
	public static byte[] HexStringToBytes(String s)
	{
	     byte[] bytes;
	     bytes = new byte[s.length() / 2];
	 
	     for (int i = 0; i < bytes.length; i++) 
	     {
	         bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),16);
	     }
	 
	     return bytes;
	}
	
//	public static byte[] hexStringToBytes(String hexString) {  
//	    if (hexString == null || hexString.equals("")) {  
//	        return null;  
//	    }  
//	    hexString = hexString.toUpperCase();  
//	    int length = hexString.length() / 2;  
//	    char[] hexChars = hexString.toCharArray();  
//	    byte[] d = new byte[length];  
//	    for (int i = 0; i < length; i++) {  
//	        int pos = i * 2;  
//	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
//	    }  
//	    return d;  
//	}
//	
//	private static byte charToByte(char c) {  
//	    return (byte) "0123456789ABCDEF".indexOf(c);  
//	} 
	
	public static boolean isHex(String hex) {
        int len = hex.length();
		int i = 0;
		char ch;
	
		while (i < len) {
		     ch = hex.charAt(i++);
		     if (! ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') ||
		           (ch >= 'a' && ch <= 'f'))) return false;
		}
		return true;
    }
	
	public static boolean isDecimal(String decimal) {
        int len = decimal.length();
		int i = 0;
		char ch;
	
		while (i < len) {
		     ch = decimal.charAt(i++);
		     if (!(ch >= '0' && ch <= '9')) return false;
		}
		return true;
    }
	
	public static boolean LockGenCode(byte bkill, byte baccess, byte buii, byte btid, byte buser, byte[] LockCode)
	{
		LockCode[0] = 0x00;
		LockCode[1] = 0x00;
		LockCode[2] = 0x00;
		
		//KillPwd
		if (bkill == 1)//锁定
		{
			LockCode[0] |= 0x0c;
			LockCode[1] |= 0x02;
		}
		else if (bkill == 2)//解锁
		{
			LockCode[0] |= 0x0c;
			LockCode[1] |= 0x00;
		}
		else if (bkill == 3)//永久锁定
		{
			LockCode[0] |= 0x0c;
			LockCode[1] |= 0x03;
		}
		else if (bkill == 4)//永久解锁
		{
			LockCode[0] |= 0x0c;
			LockCode[1] |= 0x01;
		}
		else//保持状态
		{
			LockCode[0] &= 0x03;
			LockCode[1] &= 0xfc;
		}

		//AccessPwd
		if (baccess == 1)//锁定
		{
			LockCode[0] |= 0x03;
			LockCode[2] |= 0x80;
		}
		else if (baccess == 2)//解锁
		{
			LockCode[0] |= 0x03;
			LockCode[2] |= 0x00;
		}
		else if (baccess == 3)//永久锁定
		{
			LockCode[0] |= 0x03;
			LockCode[2] |= 0xC0;
		}
		else if (baccess == 4)//永久解锁
		{
			LockCode[0] |= 0x03;
			LockCode[2] |= 0x40;
		}
		else//保持状态
		{
			LockCode[0] &= 0x0c;
			LockCode[2] &= 0x3f;
		}

		//UII
		if (buii == 1)//锁定
		{
			LockCode[1] |= 0xc0;
			LockCode[2] |= 0x20;
		}
		else if (buii == 2)//解锁
		{
			LockCode[1] |= 0xc0;
			LockCode[2] |= 0x00;
		}
		else if (buii == 3)//永久锁定
		{
			LockCode[1] |= 0xc0;
			LockCode[2] |= 0x30;
		}
		else if (buii == 4)//永久解锁
		{
			LockCode[1] |= 0xc0;
			LockCode[2] |= 0x10;
		}
		else//保持状态
		{
			LockCode[1] &= 0x3f;
			LockCode[2] &= 0xcf;
		}

		//TID
		if (btid == 1)//锁定
		{
			LockCode[1] |= 0x30;
			LockCode[2] |= 0x08;
		}
		else if (btid == 2)//解锁
		{
			LockCode[1] |= 0x30;
			LockCode[2] |= 0x00;
		}
		else if (btid == 3)//永久锁定
		{
			LockCode[1] |= 0x30;
			LockCode[2] |= 0x0c;
		}
		else if (btid == 4)//永久解锁
		{
			LockCode[1] |= 0x30;
			LockCode[2] |= 0x04;
		}
		else//保持状态
		{
			LockCode[1] &= 0xcf;
			LockCode[2] &= 0xf3;
		}

		if (buser == 1)//锁定
		{
			LockCode[1] |= 0x0c;
			LockCode[2] |= 0x02;
		}
		else if (buser == 2)//解锁
		{
			LockCode[1] |= 0x0c;
			LockCode[2] |= 0x00;
		}
		else if (buser == 3)//永久锁定
		{
			LockCode[1] |= 0x0c;
			LockCode[2] |= 0x03;
		}
		else if (buser == 4)//永久解锁
		{
			LockCode[1] |= 0x0c;
			LockCode[2] |= 0x01;
		}
		else//保持状态
		{
			LockCode[1] &= 0xf3;
			LockCode[2] &= 0xfc;
		}

		return true;
	}
}
