package me.linnemann.ptmobile.pivotaltracker.lifecycle;

public class Transition {

	private StateWithTransitions resultingState;
	private String name;
	
	public Transition(String name, StateWithTransitions resultingState) {
		this.name = name;
		this.resultingState = resultingState;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public StateWithTransitions resultingState() {
		return resultingState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	// transitions equal if name equals (resultingState does not matter)
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transition other = (Transition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
