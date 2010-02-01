package me.linnemann.ptmobile.cursor;

import me.linnemann.ptmobile.pivotaltracker.Story;

public interface StoriesCursor {

	public abstract String getIterationStart();

	public abstract String getIterationFinish();

	public abstract Story getStory();
}