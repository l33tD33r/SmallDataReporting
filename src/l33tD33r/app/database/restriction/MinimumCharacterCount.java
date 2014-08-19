package l33tD33r.app.database.restriction;

public class MinimumCharacterCount extends Restriction<String> {

	private Integer minimum;
	
	public MinimumCharacterCount(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getMinimum() {
		return minimum;
	}

	@Override
	protected boolean isValueValid(String value) {
		return value.length() >= minimum;
	}
}
