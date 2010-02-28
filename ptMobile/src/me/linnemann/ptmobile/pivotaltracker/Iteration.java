package me.linnemann.ptmobile.pivotaltracker;

import me.linnemann.ptmobile.pivotaltracker.datatype.IterationDataType;
import me.linnemann.ptmobile.pivotaltracker.value.DateTime;
import me.linnemann.ptmobile.pivotaltracker.value.Numeric;
import me.linnemann.ptmobile.pivotaltracker.value.Text;

public class Iteration extends TrackerEntity {

	public static Iteration emptyIteration() {
		return new Iteration();
	}

	public Numeric getNumber() {
		return (Numeric) data.get(IterationDataType.NUMBER);
	}

	public Numeric getId() {
		return (Numeric) data.get(IterationDataType.ID);
	}

	public DateTime getStart() {
		return (DateTime) data.get(IterationDataType.START);
	}

	public DateTime getFinish() {
		return (DateTime) data.get(IterationDataType.FINISH);
	}

	public Text getIterationGroup() {
		return (Text) data.get(IterationDataType.ITERATION_GROUP);
	}

	public Numeric getProjectId() {
		return (Numeric) data.get(IterationDataType.PROJECT_ID);
	}

	@Override
	public String getTableName() {
		return "iterations";
	}

	public String toUIString() {
		StringBuilder s = new StringBuilder();
		s.append(getNumber().getUIString());
		s.append(" |  ");
		s.append(getStart().getUIStringShortDate());
		s.append(" - ");
		s.append(getFinish().getUIStringShortDate());		

		return s.toString();
	}

}
