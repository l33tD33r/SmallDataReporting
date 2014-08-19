package l33tD33r.app.database;


import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date extends DateTimeRoot implements Comparable<Date> {

	private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date valueOf(String dateString) {
		try {
			return new Date(dateFormater.parse(dateString));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String format(Date date) {
		return dateFormater.format(date.getRootDate());
	}
	
	public static Date today() {
		return new Date(new java.util.Date());
	}
	
	public static Date past() {
		return valueOf("1970-01-01");
	}
	
	public Date(java.util.Date rootDate) {
		super(rootDate);
	}

	@Override
	public String toString() {
		return format(this);
	}

	@Override
	public int compareTo(Date another) {
		return super.compareTo(another);
	}
}
