package l33tD33r.app.database;


public class TimeSpan {

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
	
	public static final int SHORT_FORMAT = 0;
	public static final int LONG_FORMAT = 1;
	
	private long milliseconds;
	
	private int seconds;
	private int minutes;
	private int hours;
	
	public TimeSpan(Time startTime, Time endTime) {
		this(startTime.getRootDate(), endTime.getRootDate());
	}
	
	public TimeSpan(DateTime startDateTime, DateTime endDateTime) {
		this(startDateTime.getRootDate(), endDateTime.getRootDate());
	}
	
	private TimeSpan(java.util.Date startDate, java.util.Date endDate) {
		this(startDate.getTime(), endDate.getTime());
	}
	
	public TimeSpan(long startTime, long endTime) {
		this(endTime - startTime);
	}
	
	public TimeSpan(long milliseconds) {
		this.milliseconds = milliseconds;
		init();
	}
	
	private void init() {
		long totalSeconds = milliseconds / 1000;
		long totalMinutes = totalSeconds / 60;
		long totalHours = totalMinutes / 60;
		
		seconds = (int)((totalSeconds) % 60);
		minutes = (int)((totalMinutes) % 60);
		hours = (int)((totalHours) % 60);
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getHours() {
		return hours;
	}
	
	public String formatShort() {
		return format(SHORT_FORMAT);
	}
	
	public String formatLong() {
		return format(LONG_FORMAT);
	}
	
	public String format(int type) {
		StringBuilder sb = new StringBuilder();
		
		boolean hasHours = hours > 0;
		boolean hasMinutes = minutes > 0;
		boolean hasSeconds = seconds > 0;
		
		if (hasHours) {
			formatHours(sb, hours, type);
		}
		if (hasMinutes) {
			if (hasHours) {
				sb.append(" ");
			}
			formatMinutes(sb, minutes, type);
		}
		if (hasSeconds) {
			if (hasHours || hasMinutes) {
				sb.append(" ");
			}
			formatSeconds(sb, seconds, type);
		}
		return sb.toString();
	}
	
	private static void formatHours(StringBuilder sb, int hours, int type) {
		String label;
		boolean seperateLabel;
		switch (type) {
			case SHORT_FORMAT:
				label = "h";
				seperateLabel = false;
				break;
			case LONG_FORMAT:
				label = hours > 1 ? "hours" : "hour";
				seperateLabel = true;
				break;
			default:
				throw new RuntimeException("Unknown format type:" + type);
		}
		formatTimeMetric(sb, hours, label, seperateLabel);
	}
	
	private static void formatMinutes(StringBuilder sb, int minutes, int type) {
		String label;
		boolean seperateLabel;
		switch (type) {
			case SHORT_FORMAT:
				label = "m";
				seperateLabel = false;
				break;
			case LONG_FORMAT:
				label = minutes > 1 ? "minutes" : "minute";
				seperateLabel = true;
				break;
			default:
				throw new RuntimeException("Unknown format type:" + type);
		}
		formatTimeMetric(sb, minutes, label, seperateLabel);
	}
	
	private static void formatSeconds(StringBuilder sb, int seconds, int type) {
		String label;
		boolean seperateLabel;
		switch (type) {
			case SHORT_FORMAT:
				label = "s";
				seperateLabel = false;
				break;
			case LONG_FORMAT:
				label = seconds > 1 ? "seconds" : "second";
				seperateLabel = true;
				break;
			default:
				throw new RuntimeException("Unknown format type:" + type);
		}
		formatTimeMetric(sb, seconds, label, seperateLabel);
	}
	
	private static void formatTimeMetric(StringBuilder sb, int timeMetric, String label, boolean seperateLabel) {
		sb.append(timeMetric);
		if (seperateLabel) {
			sb.append(" ");
		}
		sb.append(label);
	}
}
