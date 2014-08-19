package l33tD33r.app.database.restriction;

public class MaximumValue extends Restriction<Integer> {
	
	private Integer maximum;
	
	public MaximumValue(Integer maximum) {
		this.maximum = maximum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	@Override
	protected boolean isValueValid(Integer value) {
		return value <= maximum;
	}
}
