import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtril {
	/**
	 * 匹配IP的正则表达式字符串（仅仅支持ipv4）
	 */
	public static final String IP_REGEX =  "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	public static final String IP_REGEX2 =  "((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))";
	
	
	public static boolean matchesIp(String ipV4){
		if(ipV4 == null){
			return false;
		}
		return ipV4.matches(IP_REGEX);
	}
	/**
	 * 找出字符串中符合给定正则表达式的字符
	 * @param str
	 * @param regex
	 * @return
	 */
	public static ArrayList<String> find(String str,String regex){
		if(str == null){
			return null;
		}
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));            
        }
        return strs;
	}
	
	/**
	 * 找出字符串中符合Ip格式的字符
	 * @param str
	 * @param regex
	 * @return
	 */
	public static ArrayList<String> findIp(String str){
		if(str == null){
			return null;
		}
        Pattern p = Pattern.compile(IP_REGEX2);
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));            
        }
        return strs;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "192.168.1.25";
		boolean r = matchesIp(str);
		ArrayList<String> list = findIp(str);
		System.out.println(r);
		System.out.println(list);
		

	}
}
