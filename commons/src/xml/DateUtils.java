/**
 * 
 */
package xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wuzbin
 * @date 2011-9-9
 * @type DateUtils
 * 
 * @description
 */
public class DateUtils {

	public static final String YEAR_MONTH_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM-dd";
	public static final String YEAR_MONTH_DATE_FORMAT_NOSEPERATOR = "yyyyMMdd";
	public static final String TIME_FORMATE = "HH:mm:ss";
	public static final String MONTH_DATE_YEAR_FORMAT = "MM/dd/yyyy HH:mm:ss";

	public static final SimpleDateFormat yearMonthDateTimeFormater = new SimpleDateFormat(
			YEAR_MONTH_DATE_TIME_FORMAT);
	public static final SimpleDateFormat yearMonthDateFormater = new SimpleDateFormat(
			YEAR_MONTH_DATE_FORMAT);
	public static final SimpleDateFormat timeFormater = new SimpleDateFormat(
			TIME_FORMATE);

	public static void main(String[] args) {
		Date d = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat(MONTH_DATE_YEAR_FORMAT);
		System.out.println(sf.format(d));
	}

	/**
	 * 将字符串形式的time,按照format的格式转换成Calendar对象
	 * 
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Calendar convertToCalendar(String time, String format)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date date = sf.parse(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 将字符串形式的time,按照format的格式转换成Calendar对象
	 * 
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date convertToDate(String time, String format)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date date = sf.parse(time);
		return date;
	}

	public static String convertToString(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	public static String convertToString(Calendar calendar, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(calendar.getTime());
	}

	public static String convert(String time, String format)
			throws ParseException {
		return convertToString(convertToDate(time, format), format);
	}

	/**
	 * 计算两个日期相差天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dateDiff(Date date1, Date date2) {
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff = Math.abs(time1 - time2);
		diff /= 1000 * 60 * 60 * 24;
		return (int) diff;
	}

	/**
	 * 第二天
	 * @param date
	 * @return
	 */
	public static Date getNextDate(Date date) {
		return getDateAddHour(date, 24);
	}

	/**
	 * 当前日期相差几小时（hour可以为负数）
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateAddHour(Date date, int hour) {
		long l = date.getTime();
		long n = l + (long) (hour * 60 * 60 * 1000);
		Date dateAddHour = new Date(n);
		return dateAddHour;
	}

	/**
	 * 前一天
	 * @param date
	 * @return
	 */
	public static Date getPreviousDate(Date date) {
		return getDateAddHour(date, -24);
	}
}
