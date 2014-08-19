package l33tD33r.app.database.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import l33tD33r.app.database.Date;
import l33tD33r.app.database.DateTime;
import l33tD33r.app.database.Time;
import l33tD33r.app.database.TimeSpan;


class DateTimeUtility {
	
	private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm:ss");
	
	public static Date parseDate(String dateString) {
		try {
			return new Date(dateFormater.parse(dateString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String formatDate(Date date) {
		return dateFormater.format(date.getRootDate());
	}
	
	public static Date getToday() {
		return new Date(new java.util.Date());
	}
	
	public static Date getPast() {
		return parseDate("1970-01-01");
	}
	
	public static Time parseTime(String timeString) {
		try {
			return new Time(timeFormater.parse(timeString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String formatTime(Time time) {
		return timeFormater.format(time.getRootDate());
	}
	
	public static Time getNow() {
		return new Time(Calendar.getInstance().getTime());
	}
	
	public static Time getMidnight() {
		return parseTime("00:00:00");
	}
	
	public static DateTime parseDateTime(String dateTimeString) {
		try {
			return new DateTime(dateTimeFormatter.parse(dateTimeString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String formatDateTime(DateTime dateTime) {
		return dateTimeFormatter.format(dateTime.getRootDate());
	}
	
	public static DateTime getTodayNow() {
		return new DateTime(new java.util.Date());
	}
	
	public static DateTime getPastMidnight() {
		return parseDateTime("1970-01-01 00:00:00");
	}
	
	public static TimeSpan getTimeSpan(Time startTime, Time endTime) {
		return new TimeSpan(startTime, endTime);
	}
	
	public static TimeSpan getTimeSpan(DateTime startDateTime, DateTime endDateTime) {
		return new TimeSpan(startDateTime, endDateTime);
	}
	
	public static String formatTimeSpanShort(Time startTime, Time endTime) {
		TimeSpan timeSpan = getTimeSpan(startTime, endTime);
		return timeSpan.formatShort();
	}
	
	public static String formatTimeSpanLong(Time startTime, Time endTime) {
		TimeSpan timeSpan = getTimeSpan(startTime, endTime);
		return timeSpan.formatLong();
	}
}
