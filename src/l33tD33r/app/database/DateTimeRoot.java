package l33tD33r.app.database;


import java.util.Date;

public abstract class DateTimeRoot {

	private Date rootDate;
	
	protected DateTimeRoot(Date rootDate) {
		this.rootDate = rootDate;
	}
	
	public Date getRootDate() {
		return rootDate;
	}
	
	protected int compareTo(DateTimeRoot another) {
		return this.getRootDate().compareTo(another.getRootDate());
	}
	
}
