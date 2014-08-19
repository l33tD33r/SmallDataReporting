package l33tD33r.app.database.restriction;

public class MaximumCharacterCount extends Restriction<String> {

	private Integer maximum;
	
	public MaximumCharacterCount(Integer maximum) {
		this.maximum = maximum;
	}
	
	public Integer getMaximum() {
		return maximum;
	}

	@Override
	protected boolean isValueValid(String value) {
		return value.length() <= maximum;
	}
}
