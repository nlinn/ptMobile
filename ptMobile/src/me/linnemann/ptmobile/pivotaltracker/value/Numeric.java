package me.linnemann.ptmobile.pivotaltracker.value;

import android.util.Log;

public class Numeric implements TrackerValue {

	private Integer value;

	public static Numeric getEmptyValue() {
		return new Numeric();
	}
	
	private Numeric() {
		this.value = null;
	}
	
	public Numeric(Integer value) {
		this.value = value;
	}
	
	public Numeric(String value) {
		try {
			this.value = new Integer(value);
		} catch (NumberFormatException e) {
			Log.w("IntegerValue","NumberFormatException creating Value from String: "+value);
		}
	}
	
	public String getUIString() {
		return value.toString();
	}

	public String getValueAsString() {
		if (value == null)
			return null;
		else
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
		Numeric other = (Numeric) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
