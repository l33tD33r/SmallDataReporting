package l33tD33r.app.database;


import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime extends DateTimeRoot implements Comparable<DateTime> {

	private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static DateTime valueOf(String dateTimeString) {
		try {
			return new DateTime(dateTimeFormatter.parse(dateTimeString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String format(DateTime dateTime) {
		return dateTimeFormatter.format(dateTime.getRootDate());
	}
	
	public static DateTime now() {
		return new DateTime(new java.util.Date());
	}
	
	public static DateTime past() {
		return valueOf("1970-01-01 00:00:00");
	}
	
	public DateTime(java.util.Date rootDate) {
		super(rootDate);
	}

	@Override
	public String toString() {
		return format(this);
	}

	@Override
	public int compareTo(DateTime another) {
		return super.compareTo(another);
	}
}
