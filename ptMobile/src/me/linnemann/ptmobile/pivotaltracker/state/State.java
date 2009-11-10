package me.linnemann.ptmobile.pivotaltracker.state;

import java.util.ArrayList;
import java.util.List;

public class State {

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
}
