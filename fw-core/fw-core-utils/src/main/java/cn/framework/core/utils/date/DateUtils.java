package cn.framework.core.utils.date;


import cn.framework.core.utils.date.enums.DateCircle;
import cn.framework.core.utils.date.enums.DateDimensions;
import cn.framework.core.utils.utils.TextUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 * @author LiuJingWei
 * @version 1.0
 */
public class DateUtils {

	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/** 一秒钟的毫秒数 */
	public static final long MS_SECOND = 1000;

	/** 一分钟的毫秒数 */
	public static final long MS_MINUTE = 60 * MS_SECOND;

	/** 一小时的毫秒数 */
	public static final long MS_HOUR = 60 * MS_MINUTE;

	/** 一天的毫秒数 */
	public static final long MS_DAY = 24 * MS_HOUR;

	private static final String ZH_SATURDAY = "星期六";

	private static final String ZH_FRIDAY = "星期五";

	private static final String ZH_THURSDAY = "星期四";

	private static final String ZH_WEDNESDAY = "星期三";

	private static final String ZH_TUESDAY = "星期二";

	private static final String ZH_MONDAY = "星期一";

	private static final String ZH_SUNDAY = "星期日";

	private static final String SATURDAY = "Saturday";

	private static final String FRIDAY = "Friday";

	private static final String THURSDAY = "Thursday";

	private static final String WEDNESDAY = "Wednesday";

	private static final String TUESDAY = "Tuesday";

	private static final String MONDAY = "Monday";

	private static final String SUNDAY = "Sunday";

	/** 获取当前年月日 */
	public static String getCurrentDate() {
		return getCurrentDateTime("yyyyMMdd");
	}

	/** 获取当前时分秒 */
	public static String getCurrentTime() {
		return getCurrentDateTime("HHmmss");
	}

	/** 获取GMT格式时间 */
	public static String getGMTtime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区
		return df.format(new Date());
	}

	/** 获取当前年月日时分秒 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime(YYYYMMDDHHMMSS);
	}

	/**
	 * 获得当前月份
	 */
	public static String getMonthDate() {
		return getCurrentDateTime("MM");
	}

	/**
	 * 获取当前时间
	 *
	 * @param aFmt 格式
	 */
	public static String getCurrentDateTime(String aFmt) {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(aFmt);
		return formatter.format(date);
	}

	/** 时间戳转年月日时分秒 */
	public static String getDateTimeByLong(long dateTime) {
		return getDateTimeByLong(dateTime, YYYYMMDDHHMMSS);
	}

	/** 时间戳转指定格式时间 */
	public static String getDateTimeByLong(long dateTime, String aFmt) {
		Date date = new Date(dateTime);
		SimpleDateFormat formatter = new SimpleDateFormat(aFmt);
		return formatter.format(date);
	}

	/**
	 * 获取开始时间
	 * 
	 * @param type {@link DateDimensions} 维度
	 */
	public static String getStartDate(String type) {
		String startDate = "";
		// 今天
		if (DateDimensions.TIME_TODAY.getCode().equals(type)) {
			startDate = DateUtils.getCurrentDate();
		}
		// 昨天
		if (DateDimensions.TIME_YESTERDAY.getCode().equals(type)) {
			startDate = DateUtils.getDateAdjust(-1, DateCircle.DAY);
		}
		// 本周
		if (DateDimensions.TIME_THISWEEK.getCode().equals(type)) {
			startDate = DateUtils.getWeekOfMondayDate("yyyyMMdd", 0);
		}
		// 上周
		if (DateDimensions.TIME_LASTWEEK.getCode().equals(type)) {
			startDate = DateUtils.getWeekOfMondayDate("yyyyMMdd", -1);
		}
		// 本月
		if (DateDimensions.TIME_THISMONTH.getCode().equals(type)) {
			startDate = DateUtils.getMonthOfFirstDate("yyyyMMdd", 0);
		}
		// 上月
		if (DateDimensions.TIME_LASTMONTH.getCode().equals(type)) {
			startDate = DateUtils.getMonthOfFirstDate("yyyyMMdd", -1);
		}
		return startDate;
	}

	/**
	 * 获取结束时间
	 * 
	 * @param type {@link DateDimensions} 维度
	 */
	public static String getEndDate(String type) {
		String endDate = "";
		// 今天
		if (DateDimensions.TIME_TODAY.getCode().equals(type)) {
			endDate = DateUtils.getCurrentDate();
		}
		// 昨天
		if (DateDimensions.TIME_YESTERDAY.getCode().equals(type)) {
			endDate = DateUtils.getDateAdjust(-1, DateCircle.DAY);
		}
		// 本周
		if (DateDimensions.TIME_THISWEEK.getCode().equals(type)) {
			endDate = DateUtils.getDateAdjust(-1, DateCircle.WEEK);
		}
		// 上周
		if (DateDimensions.TIME_LASTWEEK.getCode().equals(type)) {
			endDate = DateUtils.getWeekOfSundayDate("yyyyMMdd", -1);
		}
		// 本月
		if (DateDimensions.TIME_THISMONTH.getCode().equals(type)) {
			endDate = DateUtils.getDateAdjust(-1, DateCircle.MONTH);
		}
		// 上月
		if (DateDimensions.TIME_LASTMONTH.getCode().equals(type)) {
			endDate = DateUtils.getMonthOfLastDate("yyyyMMdd", -1);
		}
		return endDate;
	}

	/**
	 * 日期格式转换
	 */
	public static String dateFormatConvert(String srcStr, String srcFormat, String destFormat) {
		if (TextUtils.isEmpty(srcStr)) {
			return "";
		}

		String str;
		try {
			SimpleDateFormat srcformat = new SimpleDateFormat(srcFormat);
			Date date = srcformat.parse(srcStr);
			srcformat.applyPattern(destFormat);
			str = srcformat.format(date);
			return str;
		} catch (ParseException e) {
			return srcStr;
		}
	}

	/**
	 * 获取当前日期调整时间
	 * 
	 * @param num 调整数目
	 */
	public static String getDateAdjust(int num) {
		String currDate = getCurrentDate();

		try {
			return getDateAdjust(num, DateCircle.MONTH);
		} catch (Exception e) {
			return currDate;
		}
	}

	/**
	 * 获取当前日期调整时间
	 * 
	 * @param num 调整数目
	 */
	public static String getDateAdjust(int num, DateCircle dateCircle) {
		return getDateAdjust(num, dateCircle, "yyyyMMdd");
	}

	/**
	 * 获取当前日期调整时间
	 */
	public static String getDateAdjust(int num, DateCircle dateCircle, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(dateCircle.getAjust(), num);

		return format.format(calendar.getTime());
	}

	/**
	 * 获取指定日期调整时间
	 */
	public static String getDateAdjust(String date, int num, String dateFormat, DateCircle dateCircle) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);

		Date formatDate;
		try {
			formatDate = format.parse(date);
		} catch (ParseException e) {
			formatDate = new Date();
		}
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(formatDate);
		calendar.add(dateCircle.getAjust(), num);

		return format.format(calendar.getTime());
	}

	/**
	 * 获取指定日期调整天数
	 * 
	 * @param currDate 指定日期
	 * @param dateFormat 输出日期格式
	 * @param num 指定提前天数:正数为延后，负数为提前
	 * @return
	 */
	public static String getNDayAdjust(String currDate, int num, String dateFormat) {
		SimpleDateFormat format;
		Date date;
		try {
			format = new SimpleDateFormat(dateFormat);
			date = format.parse(currDate);
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			calendar.add(GregorianCalendar.DAY_OF_MONTH, num);
			date = calendar.getTime();
		} catch (ParseException e) {
			return currDate;
		}

		return format.format(date);
	}

	/**
	 * 获取指定日期调整月数
	 * 
	 * @param currDate 指定日期
	 * @param dateFormat 输出日期格式
	 * @param num 指定提前月:正数为延后，负数为提前
	 * @return
	 */
	public static String getNMonthAdjust(String currDate, int num, String dateFormat) {
		SimpleDateFormat format;
		Date date;
		try {
			format = new SimpleDateFormat(dateFormat);
			date = format.parse(currDate);
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			calendar.add(GregorianCalendar.MONTH, num);
			date = calendar.getTime();
		} catch (ParseException e) {
			return currDate;
		}

		return format.format(date);
	}

	/**
	 * 获取当前日期调整天数
	 * 
	 * @param dateFormat 输出日期格式
	 * @param num 调整数目
	 * @return
	 */
	public static String getNDayAdjust(int num, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String currDate = format.format(new Date());

		try {
			return getNDayAdjust(currDate, num, dateFormat);
		} catch (Exception e) {
			return currDate;
		}
	}

	/**
	 * 获取当前日期调整天数
	 * 
	 * @param num 调整数目
	 * @return
	 */
	public static String getNDayAdjust(int num) {
		// 日期格式为默认
		String dateFormat = "yyyyMMdd";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String currDate = format.format(new Date());

		try {
			return getNDayAdjust(currDate, num, dateFormat);
		} catch (Exception e) {
			return currDate;
		}
	}

	/**
	 * 增加N天
	 */
	public static String getNDayAdjust(String dataStr, int num) {
		try {
			return getNDayAdjust(dataStr, num, "yyyyMMdd");
		} catch (Exception e) {
			return dataStr;
		}
	}

	/**
	 * <pre>
	 * 将日期转换成字符串
	 * </pre>
	 */
	public static String dateFormat(Date date) {
		return dateFormat(date, "yyyyMMdd");
	}

	public static String dateFormat(Date date, String destFormat) {
		SimpleDateFormat srcformat = new SimpleDateFormat(destFormat);
		return srcformat.format(date);
	}

	public static String dateFormat(Timestamp timestamp) {
		return dateFormat(timestamp, "yyyyMMdd");
	}

	public static String dateFormat(Timestamp timestamp, String destFormat) {
		SimpleDateFormat srcformat = new SimpleDateFormat(destFormat);
		return srcformat.format(timestamp);
	}

	public static String datetimeFormat(Date date, String dateFormat) {
		return dateFormat(date, dateFormat);
	}

	public static String datetimeFormat(Date date) {
		return dateFormat(date, YYYYMMDDHHMMSS);
	}

	public static Timestamp getCurrTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Date parseDate(String dataStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			sdf.setLenient(false);
			date = sdf.parse(dataStr);
		} catch (ParseException e) {
			throw new Exception("日期格式不正确");
		}
		return date;
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String formatDate(String date, String format) {
		Date dt = null;
		SimpleDateFormat inFmt = null, outFmt = null;
		ParsePosition pos = new ParsePosition(0);
		if ((date == null) || ("".equals(date.trim()))) {
			return "";
		}
		try {
			if (Long.parseLong(date) == 0) {
				return "";
			}
		} catch (Exception nume) {
			return date;
		}
		try {
			switch (date.trim().length()) {
			case 14:
				inFmt = new SimpleDateFormat(YYYYMMDDHHMMSS);
				break;
			case 12:
				inFmt = new SimpleDateFormat("yyyyMMddHHmm");
				break;
			case 10:
				inFmt = new SimpleDateFormat("yyyyMMddHH");
				break;
			case 8:
				inFmt = new SimpleDateFormat("yyyyMMdd");
				break;
			case 6:
				inFmt = new SimpleDateFormat("yyyyMM");
				break;
			default:
				return date;
			}
			if ((dt = inFmt.parse(date, pos)) == null) {
				return date;
			}
			if ((format == null) || ("".equals(format.trim()))) {
				outFmt = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				outFmt = new SimpleDateFormat(format);
			}
			return outFmt.format(dt);
		} catch (Exception ex) {
			return date;
		}
	}

	/**
	 * 获取当前时间是星期几
	 */
	public static String getWeekOfDate(Date date) {
		return dateFormat(date, "EEEE");
	}

	/**
	 * 转换字符串星期为数字
	 * 
	 * @param str 字符串星期几，如：星期日
	 * @return 7
	 */
	public static String getWeekOfNumber(String str) {
		if (ZH_SUNDAY.equals(str) || SUNDAY.equals(str)) {
			str = "7";
		} else if (ZH_MONDAY.equals(str) || MONDAY.equals(str)) {
			str = "1";
		} else if (ZH_TUESDAY.equals(str) || TUESDAY.equals(str)) {
			str = "2";
		} else if (ZH_WEDNESDAY.equals(str) || WEDNESDAY.equals(str)) {
			str = "3";
		} else if (ZH_THURSDAY.equals(str) || THURSDAY.equals(str)) {
			str = "4";
		} else if (ZH_FRIDAY.equals(str) || FRIDAY.equals(str)) {
			str = "5";
		} else if (ZH_SATURDAY.equals(str) || SATURDAY.equals(str)) {
			str = "6";
		}
		return str;
	}
	
	/**
	 * 转换数字为字符串星期
	 * 
	 * @return  字符串星期几，如：星期日
	 */
	public static String getNumberOfWeek(String str) {
		if (str == null) {
			return null;
		}
		if ("7".equals(str)) {
			str = ZH_SUNDAY;
		} else if ("1".equals(str)) {
			str = ZH_MONDAY;
		} else if ("2".equals(str)) {
			str = ZH_TUESDAY;
		} else if ("3".equals(str)) {
			str = ZH_WEDNESDAY;
		} else if ("4".equals(str)) {
			str = ZH_THURSDAY;
		} else if ("5".equals(str)) {
			str = ZH_FRIDAY;
		} else if ("6".equals(str)) {
			str = ZH_SATURDAY;
		}
		return str;
	}

	/**
	 * 获取N周一日期
	 * 
	 * @param format
	 * @param week -1:上周，0：本周
	 * @return
	 */
	public static String getWeekOfMondayDate(String format, int week) {
		SimpleDateFormat outFmt = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DATE, (week - 1) * 7);
		} else {
			cal.add(Calendar.DATE, week * 7);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = cal.getTime();
		return outFmt.format(date);
	}

	/**
	 * 获取N周日日期
	 * 
	 * @param format
	 * @param week -1:上周，0：本周
	 * @return
	 */
	public static String getWeekOfSundayDate(String format, int week) {
		SimpleDateFormat outFmt = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, (week+1)*7);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DATE, (week) * 7);
		} else {
			cal.add(Calendar.DATE, (week + 1) * 7);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date date = cal.getTime();
		return outFmt.format(date);
	}

	/**
	 * 获取N月第一天
	 * 
	 * @param format 格式
	 * @param month -1：前一个月，0：本月
	 * @return
	 */
	public static String getMonthOfFirstDate(String format, int month) {
		SimpleDateFormat outFmt = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);
		// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date date = cal.getTime();
		return outFmt.format(date);
	}

	/**
	 * 获取N月最后天
	 * 
	 * @param format
	 * @param month -1：前一个月，0：本月
	 * @return
	 */
	public static String getMonthOfLastDate(String format, int month) {
		SimpleDateFormat outFmt = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month + 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.DATE, -1);
		// cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		Date date = cal.getTime();
		return outFmt.format(date);
	}

	/**
	 * 获取当月的 天数
	 **/
	public static int getCurrentMonthDay() {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 **/
	public static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 验证是否为周末
	 * 
	 * @param dateStr
	 * @return
	 */
	public static boolean isWeekEnd(String dateStr) {
		boolean valid = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (cal.get(Calendar.DAY_OF_WEEK) != 1) {
				valid = false;
			}
		} catch (ParseException e) {
			valid = false;
		}
		return valid;
	}

	/**
	 * 获取两个日期之间的全部日期
	 * 
	 * @param format 日期格式 如yyyyMMdd
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return
	 * @throws Exception
	 */
	public static Set<String> dateSplit(String format, String start, String end) {
		if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
			return null;
		}
		if (start.compareTo(end) > 0) {
			return null;
		}

		Set<String> dateSet = new HashSet<String>();
		String temp = start;
		while (true) {
			dateSet.add(temp);
			temp = getNDayAdjust(temp, 1, format);
			if (temp.compareTo(end) > 0) {
				break;
			}
			continue;
		}
		return dateSet;
	}

	/**
	 * 比较两个时间相差数
	 * 
	 * @param dateTime1 日期
	 * @param dateTime2 日期
	 * @return
	 */
	public static long getTimeInterval(String dateTime1, String dateTime2) {
		return getTimeInterval(dateTime1, dateTime2, DateCircle.DAY, "yyyyMMdd");
	}

	/**
	 * 比较与当前时间相差数
	 */
	public static long getTimeInterval(String dateTime) {
		return getTimeInterval(getCurrentDateTime("yyyyMMdd"), dateTime, DateCircle.DAY, "yyyyMMdd");
	}

	/**
	 * 比较两个时间相差数
	 * 
	 * @param dateTime1 日期
	 * @param dateTime2 日期
	 * @param format 日期格式
	 * @return
	 */
	public static long getTimeInterval(String dateTime1, String dateTime2, DateCircle dateCircle, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date1 = sdf.parse(dateTime1);
			Date date2 = sdf.parse(dateTime2);

			long intervalMill = date1.getTime() - date2.getTime();
			if (DateCircle.SECOND == dateCircle) {
				return intervalMill / (1000);
			}
			if (DateCircle.MINUTE == dateCircle) {
				return intervalMill / (1000 * 60);
			}
			if (DateCircle.HOUR == dateCircle) {
				return intervalMill / (1000 * 60 * 60);
			}
			if (DateCircle.DAY == dateCircle) {
				return intervalMill / (1000 * 60 * 60 * 24);
			}

		} catch (ParseException e) {
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * 获取差月
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthSpace(String date1, String date2) {

		int result = 0;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();

			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
			result = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		} catch (ParseException e) {
		}

		return result == 0 ? 1 : Math.abs(result);

	}

	/**
	 * 格式化long时间转换为：*天*时*分*秒*毫秒
	 */
	public static String formatUptime(long uptime) {
		StringBuilder buf = new StringBuilder();
		if (uptime > MS_DAY) {
			long days = (uptime - uptime % MS_DAY) / MS_DAY;
			buf.append(days);
			buf.append("天 ");
			uptime = uptime % MS_DAY;
		}
		if (uptime > MS_HOUR) {
			long hours = (uptime - uptime % MS_HOUR) / MS_HOUR;
			buf.append(hours);
			buf.append("时 ");
			uptime = uptime % MS_HOUR;
		}
		if (uptime > MS_MINUTE) {
			long minutes = (uptime - uptime % MS_MINUTE) / MS_MINUTE;
			buf.append(minutes);
			buf.append("分 ");
			uptime = uptime % MS_MINUTE;
		}
		if (uptime > MS_SECOND) {
			long seconds = (uptime - uptime % MS_SECOND) / MS_SECOND;
			buf.append(seconds);
			buf.append("秒 ");
			uptime = uptime % MS_SECOND;
		}
		if (uptime > 0) {
			buf.append(uptime);
			buf.append("毫秒 ");
		}
		return buf.toString();
	}

	/**
	 * 秒 转 固定格式时间(00:00:00)
	 * 
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return "00:00:00";
		}
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99) {
					return "99:59:59";
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) {
			retStr = "0" + Integer.toString(i);
		}else {
			retStr = "" + i;
		}
		return retStr;
	}

	/**
	 * @param dateTime
	 * @param format
	 * @return
	 */
	public static boolean inspectDateFormat(String dateTime, String format) {
		boolean flag = true;
		try {
			if (TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(format)) {
				return false;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			sdf.parse(dateTime);
		} catch (ParseException e) {
			flag = false;
		}
		return flag;
	}



	public static void main(String[] args) throws ParseException {
		System.out.println(getTimeInterval("20180101", "20170101"));
	}

}
