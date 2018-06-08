package com.lty.zgj.driver.core.tool;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @ClassName: MD5Util 
 * @Description: MD5加密
 * @date: 2018年4月18日 上午9:19:59
 */
public class MD5Util {
	

	// 十六进制下数字到字符的映射数组  
    private final static String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d","e", "f" };
	/**
	 * @Title: getSign
	 * @date: 2018年4月18日 上午9:49:41 
	 * @Description: 生成加密sign
	 * @param parameters
	 * @param signKey
	 * @return
	 * @return: String
	 */
	public static String getSign(Map<String, String> parameters,String signKey) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) it.next();
			String key =  entry.getKey();
			Object value = entry.getValue();// 去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		sb.append("key=" + signKey);
		String str = sb.toString().trim();
		// 注意sign转为大写
		str = MD5Util.getMD5(sb.toString().trim()).toUpperCase();
		return str;
	}
	
	
    public final static String getMD5(String str){  
        if (str != null) {  
            try {  
                // 创建具有指定算法名称的信息摘要  
                MessageDigest md = MessageDigest.getInstance("MD5");  
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算  
                byte[] results = md.digest(str.getBytes()); // 将得到的字节数组变成字符串返回  
                StringBuffer resultSb = new StringBuffer();  
                String a = "";  
                for (int i = 0; i < results.length; i++) {  
                    int n = results[i];  
                    if (n < 0)  
                        n = 256 + n;  
                    int d1 = n / 16;  
                    int d2 = n % 16;  
                    a = HEXDIGITS[d1] + HEXDIGITS[d2];  
                    resultSb.append(a);  
                }  
                return resultSb.toString().substring(8, 24);
            } catch (Exception ex) {  
                ex.printStackTrace();  
            }  
        }  
        return null;  
    } 

}
