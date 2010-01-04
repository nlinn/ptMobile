package me.linnemann.ptmobile.pivotaltracker;

public enum StoryType {
	FEATURE, BUG, CHORE, RELEASE, UNKNOWN;
	
	public String toString() {
		return name().toLowerCase();
	}
}
