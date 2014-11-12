package com.sb.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class SBString {

	/**
	 * 判断是否是IP地址，192.168.0.213
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIPAdress(String str) {
		Pattern pattern = Pattern
				.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}
	/**
	 * 验证是否手机号:15011571302
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }
	
	/** 
     * 电话号码验证 ：待验证
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) {   
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    } 
    
    /** 货币化：10000返回10,000 */
    public static String getCurrency(String raw){
		if(raw == null || raw.equals("")) return "0";
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		return currencyFormat.format(Double.parseDouble(raw));
	}
    
    /**
	 * 判断字符串是否为邮箱
	 * 
	 * @author 吕柳燕
	 * @param email
	 *            判断的字符串
	 * */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断字符串是否包含中文
	 * 
	 * @author lvliuyan
	 * @param strName
	 *            判断的字符串
	 * */
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是身份证号
	 */
	public static boolean isCard(String s_aStr) {
		String str = "\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z]";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(s_aStr);
		return m.matches();
	}
	public static String getUrl(String src) {
		if (TextUtils.isEmpty(src)) {
			return null;
		}
		Pattern p = Pattern.compile("http://[\\w\\.\\-/:]+",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(src);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	public static boolean pattern(String pattern, String content) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(content);
		return m.matches();
	}

	public static String replaceCardNumber(String str, int n) {
		String sub = "";
		try {
			sub = str.substring(str.length() - n, str.length());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < n; i++) {
				sb = sb.append("*");
			}
			sub = sb.toString() + sub;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sub;
	}

	/**
	 * 将流直接转换为字符串
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String ConvertStream2String(InputStream inputStream) {
		String jsonStr = "";
		// ByteArrayOutputStream相当于内存输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// 将输入流转移到内存输出流中
		try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			// 将内存流转换为字符串
			jsonStr = new String(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	/**
	 * 字符串转流
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 获取省市区详细地址
	 * 
	 * @return
	 */
	public static String[] getAdrress(String str) {
		String adrressLevel1 = "", adrressLevel2 = "", adrressLevel3 = "";
		int end1, end2;
		// 有省 有市既有区 有县即无市
		//
		// 无省即为自治区或者直辖市或者特别行政区（此时为市级单位）
		String[] address = new String[3];
		if (str != null && !"".equals(str)) {
			if (str.contains("省")) {
				end1 = str.lastIndexOf("省") + 1;
				adrressLevel1 = str.substring(0, end1);
				end2 = str.lastIndexOf("县") + 1;
				adrressLevel2 = str.substring(end1, end2);
				adrressLevel3 = str.substring(end2, str.length());
			} else // 无省即为自治区或者直辖市或者特别行政区（此时为市级单位）
			{
				if (str.contains("市")) {
					end1 = str.lastIndexOf("市") + 1;
					adrressLevel1 = str.substring(0, end1);
					end2 = str.lastIndexOf("区") + 1;
					adrressLevel2 = str.substring(end1, end2);
					adrressLevel3 = str.substring(end2, str.length());
				} else if (str.contains("自治区")) {
					end1 = str.lastIndexOf("自治区") + 1;
					adrressLevel1 = str.substring(0, end1);
					end2 = str.lastIndexOf("市") + 1;
					adrressLevel2 = str.substring(end1, end2);
					adrressLevel3 = str.substring(end2, str.length());
				}

			}
		}
		address[0] = adrressLevel1;
		address[1] = adrressLevel2;
		address[2] = adrressLevel3;
		return address;
	}
	
	

}
