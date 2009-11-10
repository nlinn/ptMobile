package me.linnemann.ptmobile.pivotaltracker.state;

public class Transition {

	private State resultingState;
	private String name;
	
	public Transition(String name, State resultingState) {
		this.name = name;
		this.resultingState = resultingState;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public State resultingState() {
		return resultingState;
	}
}
