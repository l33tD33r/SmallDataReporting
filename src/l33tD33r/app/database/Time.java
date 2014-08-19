package l33tD33r.app.database;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time extends DateTimeRoot implements Comparable<Time>{

	private static SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm:ss");
	
	public static Time valueOf(String timeString) {
		try {
			return new Time(timeFormater.parse(timeString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String format(Time time) {
		return timeFormater.format(time.getRootDate());
	}
	
	public static Time now() {
		return new Time(Calendar.getInstance().getTime());
	}
	
	public static Time midnight() {
		return valueOf("00:00:00");
	}
	
	public Time(java.util.Date rootDate) {
		super(rootDate);
	}

	@Override
	public String toString() {
		return format(this);
	}

	@Override
	public int compareTo(Time another) {
		return super.compareTo(another);
	}
}
