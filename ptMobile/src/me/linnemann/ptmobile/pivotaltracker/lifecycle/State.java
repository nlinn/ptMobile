package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class State {

	public static final State UNSTARTED = new State("unstarted");
	public static final State STARTED = new State("started");
	public static final State FINISHED = new State("finished");
	public static final State DELIVERED = new State("delivered");
	public static final State ACCEPTED = new State("accepted");
	public static final State REJECTED = new State("rejected");
	
	private List<Transition> transitions;
	private String name;
	
	public State(String name) {
		this.name = name;
		this.transitions = new ArrayList<Transition>();
	}
	
	public State(String name, List<Transition> transitions) {
		this(name);
		this.transitions = transitions;
	}
	
	public State(String name, Transition transition) {
		this(name);
		this.transitions.add(transition);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * a state equals another state if the name equals, transitions have nothing to do with this comparision
	 *
	 * this makes it possible to compare a feature in state "unstarted" and a release in state "unstarted"
	 * they are meant to be equal, even if their unstarted states allow different transitions
	 */
	@Override	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
