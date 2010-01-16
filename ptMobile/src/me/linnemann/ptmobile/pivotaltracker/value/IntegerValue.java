package me.linnemann.ptmobile.pivotaltracker.value;

public class IntegerValue implements TrackerValue {

	private Integer value;

	public IntegerValue(Integer value) {
		this.value = value;
	}
	
	public String getUIString() {
		return value.toString();
	}

	public String getValueAsString() {
		return value.toString();
	}

	public Integer getValue() {
		return value;
	}

	public boolean isEmpty() {
		return value == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntegerValue other = (IntegerValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
