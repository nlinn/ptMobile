package me.linnemann.ptmobile.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.EntityFromAPIBuilder;
import me.linnemann.ptmobile.pivotaltracker.Story;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;
import me.linnemann.ptmobile.pivotaltracker.datatype.DataTypeFactory;
import me.linnemann.ptmobile.pivotaltracker.datatype.StoryDataTypeFactory;

public class XMLStoriesListener implements XMLStackListener, StoryContext {

	private DBAdapter db;
	private IterationContext iteration;
	//private StoryFromAPIBuilder storyBuilder;
	
	private EntityFromAPIBuilder builder;
	private DataTypeFactory factory;
	private Integer position=0;
	private Integer lastKnownIteration = 0;
	private Integer lastStoryId = 0;
	
	public XMLStoriesListener(DBAdapter db, IterationContext iteration) {
		this.db = db;
		this.iteration = iteration;
		this.factory = new StoryDataTypeFactory();
		initBuilder();
	}
	
	private void initBuilder() {
		this.builder = new EntityFromAPIBuilder(factory);
	}
	
	
	public void elementPoppedFromStack() {
		Story story = (Story) builder.getEntity();
		addMetaDataToStory(story);
		db.insertEntity(story);
		initBuilder();
	}

	public void handleSubElement(String element, String data) {
		builder.add(element, data);
		
		if (element.equalsIgnoreCase("ID")) {
			lastStoryId = Integer.valueOf(data);
		}
	}
	
	private void addMetaDataToStory(Story story) {
		resetOrIncrementPosition();
		story.changeProjectId(iteration.getProjectId());
		story.changeIterationGroup(iteration.getIterationGroup());
		story.changePosition(position);
		
		if (iteration.getIterationNumber() != null) {
			story.changeIterationNumber(iteration.getIterationNumber());
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
