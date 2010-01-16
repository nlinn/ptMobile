package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.ArrayList;
import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.value.State;

public class StateWithTransitions {
	
	private List<Transition> transitions;
	private State state;
	
	public StateWithTransitions(State state) {
		this.state = state;
		this.transitions = new ArrayList<Transition>();
	}
	
	public StateWithTransitions(State state, List<Transition> transitions) {
		this(state);
		this.transitions = transitions;
	}
	
	public StateWithTransitions(State state, Transition transition) {
		this(state);
		this.transitions.add(transition);
	}
	
	public State getState() {
		return state;
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public String toString() {
		return state.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		StateWithTransitions other = (StateWithTransitions) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}