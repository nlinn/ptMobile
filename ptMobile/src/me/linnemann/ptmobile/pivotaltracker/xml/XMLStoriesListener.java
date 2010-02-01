package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.StoryFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.fields.StoryData;

public class XMLStoriesListener implements XMLStackListener, StoryContext {

	private DBAdapter db;
	private IterationContext iteration;
	private StoryFromAPIBuilder storyBuilder;
	private Integer position=0;
	private Integer lastKnownIteration = 0;
	private Integer lastStoryId = 0;
	
	public XMLStoriesListener(DBAdapter db, IterationContext iteration) {
		this.db = db;
		this.iteration = iteration;
		this.storyBuilder = new StoryFromAPIBuilder();
		initStoryBuilder();
	}
	
	public void elementPoppedFromStack() {
		addMetaDataToStory();
		db.insertStory(storyBuilder.getStory());
		initStoryBuilder();
	}

	public void handleSubElement(String element, String data) {
		storyBuilder.add(element, data);
		
		if (element.equalsIgnoreCase("ID")) {
			lastStoryId = Integer.valueOf(data);
		}
	}

	private void initStoryBuilder() {
		storyBuilder.clear();
	}
	
	private void addMetaDataToStory() {
		resetOrIncrementPosition();
		storyBuilder.add(StoryData.PROJECT_ID, iteration.getProjectId().toString());
		storyBuilder.add(StoryData.ITERATION_GROUP, iteration.getIterationGroup());
		storyBuilder.add(StoryData.POSITION, position.toString());
		
		if (iteration.getIterationNumber() != null) {
			storyBuilder.add(StoryData.ITERATION_NUMBER, iteration.getIterationNumber().toString());
		}
	}
	
	private void resetOrIncrementPosition() {
		
		Integer iterationFromContext = iteration.getIterationNumber();
		
		if ((iterationFromContext != null) && (!lastKnownIteration.equals(iterationFromContext))) {
			lastKnownIteration = iterationFromContext;
			position = 1;
		} else {
			position++;
		}
	}

	public Integer getProjectId() {
		return iteration.getProjectId();
	}

	public Integer getStoryId() {
		return lastStoryId;
	}
}
