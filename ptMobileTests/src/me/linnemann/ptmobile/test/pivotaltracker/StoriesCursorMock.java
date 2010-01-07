package me.linnemann.ptmobile.test.pivotaltracker;

import me.linnemann.ptmobile.cursor.StoriesCursor;
import me.linnemann.ptmobile.pivotaltracker.Story;

public class StoriesCursorMock implements StoriesCursor {

	public String getCurrentState() {
		return TestData.ANY_STATE.toString(); // TODO remove string, State type
	}

	public String getDeadline() {
		return TestData.ANY_DEADLINE;
	}

	public String getDescription() {
		return TestData.ANY_DESCRIPTION;
	}

	public Integer getEstimate() {
		return TestData.ANY_ESTIMATE;
	}

	public Integer getId() {
		return TestData.ANY_ID;
	}

	public String getIterationFinish() {
		return null;
	}

	public Integer getIterationNumber() {
		return TestData.ANY_ITERATION_NUMBER;
	}

	public String getIterationStart() {
		return null;
	}

	public String getLabels() {
		return TestData.ANY_LABELS;
	}

	public String getName() {
		return TestData.ANY_NAME;
	}

	public String getOwnedBy() {
		return TestData.ANY_OWNED_BY;
	}

	public Integer getProjectId() {
		return TestData.ANY_PROJECT_ID;
	}

	public String getRequestedBy() {
		return TestData.ANY_REQUESTED_BY;
	}

	public Story getStory() {
		return null;
	}

	public String getStoryType() {
		return TestData.ANY_STORYTYPE.toString();
	}

	public boolean hasDeadline() {
		return true;
	}

	public boolean hasDescription() {
		return true;	}

	public boolean hasEstimate() {
		return true;	}

	public boolean hasLabels() {
		return true;	}

	public boolean hasOwnedBy() {
		return true;	}

	public boolean hasRequestedBy() {
		return true;	}

	public boolean isIterationStarter() {
		return true;
	}

	public String getAcceptedAt() {
		return TestData.ANY_ACCEPTED_AT;
	}

	public String getCreatedAt() {
		return TestData.ANY_CREATED_AT;
	}

	public String getIterationGroup() {
		return TestData.ANY_ITERATIONGROUP;
	}

}
