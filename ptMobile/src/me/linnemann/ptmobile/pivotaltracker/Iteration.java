package me.linnemann.ptmobile.pivotaltracker;

import android.content.ContentValues;

public class Iteration {
	//<id type="integer">1</id>
    //<number type="integer">1</number>
    //<start type="datetime">2009/03/16 00:00:00 UTC</start>
    //<finish type="datetime">2009/03/23 00:00:00 UTC</finish>

	private StringBuilder id;	// pivotals own id
	private StringBuilder number;
	private StringBuilder start;
	private StringBuilder finish;
	private String project_id; // belongs to project
	private String iteration_group;

	public Iteration(String project_id, String iteration_group) {
		id = new StringBuilder();
		number = new StringBuilder();
		start = new StringBuilder();
		finish = new StringBuilder();
		this.project_id = project_id;
		this.iteration_group = iteration_group;
	}
	
	//public String getId() {
	//	return id.toString();
	//}

	public void addId(String ptId) {
		id.append(ptId);
	}
	
	public String getNumber() {
		return number.toString();
	}

	public void addNumber(String number) {
		this.number.append(number);
	}
	
	public void addStart(String start) {
		this.start.append(start);
	}
	
	public void addFinish(String finish) {
		this.finish.append(finish);
	}

	//public String getProject_id() {
	//	return project_id.toString();
	//}

	//public String getStart() {
	//	return start.toString();
	//}
	//public String getFinish() {
	//	return finish.toString();
	//}

	/**
	 * This Value is meant to be filled value by value, isDataComplete indicates whether all fields
	 * are filled.
	 * 
	 * @return
	 */
	public boolean isDataComplete() {
		return (	(id.length() > 0)
					&& (number.length() > 0)
					&& (start.length() > 0)
					&& (finish.length() > 0)
					&& (project_id.length() > 0) );
	}
	
	/**
	 * Get data in a db friendly way
	 * 
	 * @return
	 */
	public ContentValues getDataAsContentValues() {
		ContentValues v = new ContentValues();
	    v.put("id", id.toString());
	    v.put("number", number.toString());
	    v.put("start", start.toString().replaceAll("/", "-").replaceAll(" UTC",""));
	    v.put("finish", finish.toString().replaceAll("/", "-").replaceAll(" UTC",""));
	    v.put("project_id", project_id.toString());
	    v.put("iteration_group", iteration_group);
	    v.put("updatetimestamp", Long.toString(System.currentTimeMillis()));
	    return v;
	}
	
}
