package l33tD33r.app.database.restriction;

public class RestrictionException extends Exception {

	private Restriction restriction;
	
	public RestrictionException(Restriction restriction) {
		this.restriction = restriction;
	}
	
	public Restriction getRestriction() {
		return this.restriction;
	}
}
