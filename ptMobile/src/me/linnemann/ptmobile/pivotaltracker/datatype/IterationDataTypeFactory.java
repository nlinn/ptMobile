package me.linnemann.ptmobile.pivotaltracker.datatype;

import me.linnemann.ptmobile.pivotaltracker.Iteration;
import me.linnemann.ptmobile.pivotaltracker.TrackerEntity;

public class IterationDataTypeFactory extends DataTypeFactory {
	
	public IterationDataTypeFactory() {
		super(IterationDataType.KNOWNTYPES);
	}

	@Override
	public TrackerEntity getEmptyEntity() {
		return Iteration.emptyIteration();
	}
}
