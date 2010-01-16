package me.linnemann.ptmobile.pivotaltracker.lifecycle;

import java.util.List;

import me.linnemann.ptmobile.pivotaltracker.value.State;

public interface Lifecycle {
	public List<Transition> getAvailableTransitions(State state);
}
