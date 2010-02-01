package me.linnemann.ptmobile.pivotaltracker.value;

public class Text implements TrackerValue{

	private String value;
	
	public Text(String value) {
		this.value = value;
	}
	
	public String getUIString() {
		return value;
	}

	public String getValueAsString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public boolean isEmpty() {
		return ( (value == null) || (value.length() < 1) );
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
		Text other = (Text) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public static TrackerValue getEmptyValue() {
		return new Text("");
	}
}
