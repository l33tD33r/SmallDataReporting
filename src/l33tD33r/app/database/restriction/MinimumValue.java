package l33tD33r.app.database.restriction;

public class MinimumValue extends Restriction<Integer> {

	private Integer minimum;
	
	public MinimumValue(Integer minimum) {
		this.minimum = minimum;
	}
	
	public Integer getMinimum() {
		return minimum;
	}

	@Override
	protected boolean isValueValid(Integer value) {
		return value >= minimum;
	}

}
