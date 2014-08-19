package l33tD33r.app.database.restriction;

public abstract class Restriction<E> {
	protected abstract boolean isValueValid(E value); 
	
	public void validate(E value) throws RestrictionException {
		if (!isValueValid(value)) {
			throw new RestrictionException(this);
		}
	}
}
